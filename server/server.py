#// Geoffrey Pitman
#// CSC464 - HCI
#// 6/17/16
#// Itera#// Geoffrey Pitman
#// CSC464 - HCI
#// 6/17/16
#// Iteration 1
#// server.py
#// The purpose of this file is to serve up text files, line by line,
#    to a connected tcp/ip client.
#  ****note**** The port and host are hardcoded around lines 92-94
#       host = "127.0.0.1"
#       port = 55536
#  ****note**** ctrl-c will clean everything up and exit

import os
import socket
import sys
import subprocess
from sys import argv
from thread import *
import os.path

#callback for worker thread when client connects
def clientthread(conn):

    homedirectory = os.getcwd().strip()
    conn.sendall(homedirectory + '\n')
    information = ""
    while True:
        information = conn.recv(10000)
        information = information.rstrip('\r\n')
        print information
        if information[:3] == 'BYE':
            print str(addr[1])+': Client disconnected, closing socket.'
            conn.close()
            break
        elif information [:2] == 'HI':
            try:
                conn.sendall('WELCOME\n')
            except OSError:
                conn.sendall('Weclome failed' + '\n')
        elif information[:2] == 'CD':
            try:
                information = information.split()
                os.listdir(information[1])
                homedirectory = information[1]
                conn.sendall('cdsuccess\n')
            except OSError, msg:
                conn.sendall('Directory Error: ' + msg[1]+ '\n')
        elif information[:8] == 'HOMEPATH':
            conn.sendall(homedirectory + '\n')
        elif information[:3] == 'DIR' :
            dirlist = os.listdir(homedirectory.rstrip('\r\n'))
            dirtemp = []
            for i in dirlist[0:]:
                if os.path.isfile(homedirectory.strip() + '/' + i.strip()) == True:
                    try:
                        f = open(homedirectory.strip() + '/' + i.strip(), 'rb')
                    except IOError:
                        print "ioerror"
                    else:
                        try:
                            for line in f:
                                line = line.decode('ascii')
                            dirtemp.append(i)
                        except UnicodeDecodeError:
                            print "not ascii"
            if not dirtemp:
                conn.sendall('@@@empty@@@\n')
            else:
                conn.sendall(str(dirtemp) + '\n')
        elif information[:8] == 'DOWNLOAD':
            information = information.split()
            dlpath = str(homedirectory.strip() + '/' + information[1].strip())
            try:
                f = open(dlpath, 'rb')
            except IOError, msg:
                conn.sendall('@@@dlfailure@@@\n')
                conn.sendall('Transfer Failure: ' + msg[1]+ '\n')
            else:
                try:
                    for line in f:
                        line = line.decode('ascii')
                        if line != '\n' and line != '\r' and line != '\r\n' and line != '\n\r' and line!= "" and line != None:
                            conn.sendall(line.rstrip('\r\n') + '\n')
                        else:
                            conn.sendall('@@@blank@@@\n')
                    conn.sendall('@@@dlsuccess@@@\n')
                except UnicodeDecodeError:
                    conn.sendall('@@@dlfailure@@@\n')
                    conn.sendall('Transfer Failre: Non-ASCII file!\n')
    conn.close()

if len(sys.argv)==2:
    script, PORT = argv
elif len(sys.argv)>2:
    print >>sys.stderr, 'Please use the format: %s <[port]>\n' % sys.argv[0]
    sys.exit()
else:
    PORT = '55536'

HOST = "127.0.0.1"
try :
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print 'Socket created'
except socket.error, msg :
    print 'Failed to create socket. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
try:
    s.bind((HOST, int(PORT)))
except socket.error, msg :
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
try:
    s.listen(5)
    print 'Socket is listening.  \nHit Ctrl-C to close server.'
except socket.error, msg :
    print 'Listen failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
try:
    while 1:
        conn, addr = s.accept()
        print 'Connected with ' + addr[0] + ':' + str(addr[1])
        start_new_thread(clientthread ,(conn,))
except KeyboardInterrupt:
    s.close()
    print '\nClosing sockets and exiting'
    sys.exit()

