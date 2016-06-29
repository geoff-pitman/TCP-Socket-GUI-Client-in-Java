// Geoffrey Pitman
// CSC464 - HCI
// 6/30/16
// Iteration 2
// Client.java
// Client Class Purpose: This class' purpose is handle the networking aspect
//				  of the application.  It is an attempt to abstract
//				  the networking details from the UI.

//  ****WARNING**** Do not run this file in the same directory as the server!!
//                              It will wipe everything in the file!!!!
//  ****note**** this app will look for images in "src/<image file>"
// ****note*** compiled using jre-8
//


import java.net.*;
import java.io.*;
import javax.swing.*;

public class Client {

    private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static ClientGUI gui = null;
    
    // init gui
    public Client()
    {    	
    	gui = new ClientGUI(this);
    }
    
    // run app
    public static void main(String[] args)
    {
        Client app = new Client();
    }
    
    // connect client to server
    // IMPORT: Sring addr - host to connect to
    //		   int port - port to connect on
	// RETURNS: String path - server home path on success
    //          null - on failure
    public String getServer(String addr, int port)
    {  //Create socket connection
        try{
            socket = new Socket(addr, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // begin server "handshake"
            String path = in.readLine();
            out.flush();
            out.println("HI");
            in.readLine();
            return path;
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null,
                   "Unknown Host",
                   "ERROR",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        } catch  (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Host Unavailable",
                    "ERROR",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
    
    // provides way for UI to indicate when user is done with connection
    public void closeConnection()
    {
    	try {
			socket.close();
		} catch (IOException e) {
			//doesn't matter because we're done with it
		}
    }
    
    //provides access to socket out stream
    public PrintWriter getOutStream()
    {
    	return out;
    }
    
    //provides access to socket in stream
    public BufferedReader getInStream()
    {
    	return in;
    }
    
}