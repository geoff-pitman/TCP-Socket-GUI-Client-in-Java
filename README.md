# TCP-Socket-GUI-Client-in-Java-and-Python
TCP GUI client front end written in Java, server back-end written in Python.
#// Geoffrey Pitman
#// CSC464 - HCI
#// 6/29/16
#// Iteration 2
#// TCP/IP Socket Client
#// readme.txt
#// The purpose of this application is to demonstrate tcp/ip socket APIs
#//      by transferring files from the server's working directory,
#//      into the client's download directory.

::NOTES::
Tested only in Windows, but should run on anything that
Python 2.7 and Java jre8 can be installed on.
Assuming the above are installed...
1. double click 'server.py' file to execute (on nix: 'bash$: python server.py')
2. compile and execute java application

Server hardcoded address = "127.0.0.1"
Server hardcoded port    =  55536

Java source files:
Client.java (includes main), ClientGUI.java, ListEntry.java, 
ListEntryCellRenderer.java, AnimatedIcon.java

The initial version of this project was three files, including a picture file.
The project has grown SUBSTANTIALLY. Therefore, it is now structured
in the following ways: 
(I specify '<extension>' because the application identifies 15 [ascii] file extensions)
Assuming we are in the root project directory, 'TCP_Client':
- <artifact>        :  "<path>"
- source code       :  "src/<source>.java"
- image files       :  "resources/<image file>.png"
- server file       :  "server/server.py"
- test downloads    :  "server/<test file>.<extension>"
- dummy downloaded  :  "downloads/<dummy downloaded file>.<extension>"


::UPDATES::
 - Checks for ascii-ness server side. 
 	^no need for initial warning message when switching to connected layout
	^no unrecoverable errors or bogus successful transfer indications!!!
- Prevented server.py from showing up in listing to avoid any chance of overwrite
	^user no longer needs to know about this detail 
- Added aesthetically pleasing KU logo to discon framee
- Added aesthetically pleasing logo to conn frame
- Removed directory listing button
 	^dir listing populates automatically for user upon successful 
		connection and dir change
 	^dir listing automatically cleared when switching back to discon frame 
- Added icon labels to match the following file types...
 	^...c, cpp, cs, py, rb, css, html, xml, csv, txt, json, pl, java, js, php 
	^makes the application look much nicer
- Both frames are now set to auto focus when they are set to visible
	^user will no longer lose the application behind other windows
- Current working directory field is now highlightable, but not editable
	^path can be copied by user for convenience
- Dialog box icons changed to more appropriately match the message content
	^user will not confuse a warning message with an information message
- Made the background of the frames a lighter gray
- Added tiny arrow image pointing to server current path
	^extra guidance for user
- Removed instruction message from disconnected frame - it looked tacky
	^replaced with tool tip
- With the cursor, if you split the word 'Connect' on the connect button,
  down the middle between the two n's, the 'OK' button for the dialog box
  (that appears on successful connection) should appear perfectly centered with 
  he cursor (at least on my machine).  Meaning you can click connect and ok without moving the cursor.
  Same goes for the disconnect click, confirm exit click, and re-connect click sequence
- Added app icon to window frame/task bar
	^the user won't see the generic java logo which looks unprofessional
- Added second listbox for client's download directory listing
	^user can now see what is already in their download directory
	^user will be able to see the files they have transfered from the server
- Added tool tips with instructions corresponding to the respective UI component
	^very helpful for guiding user without having to plaster tacky instructions all over the UI
- Added animated directional arrow indicating file transfer direction (server->client)
	^helps the user to immediately recognize the files are coming from server, going to client
	^it also makes the application look a little fancier I think.

- Overall I'm extremely pleased with the project.  I learned a heck of a lot, and 
  the UI looks so much better than I ever thought it would.
  Also, I'm really happy with my error handling.  In terms of the Severity Scale,
  I think I've reduced everything to cosmetic - as far as I can tell anyway.

- If there were to be a third iteration of the project I would include a Help button,
 that opens a new modal frame with a full explanation of the application.  There were some
 features I wanted to include but couldn't think of a straight forward way of instructing the
 user on how to use them without pasting tacky looking text labels everywhere.  
 Adding a help frame would solve that.
- The number one thing I would include in a 3rd iteration of the project would be that the user
 could select as many files as he/she wants to download at one time.
- I would also add drag-and-drop and double-click to initiate downloads
 as well
