import java.net.*;
import java.io.*;
import javax.swing.*;

class Client {

    private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static ClientGUI gui = null;
    
    public Client()
    {    	
    	gui = new ClientGUI(this);
    }
    
    public static void main(String[] args)
    {
        Client app = new Client();
    }
    
	// returns home path on success, null on failure
    public String getServer(String addr, int port)
    {  //Create socket connection
        try{
            socket = new Socket(addr, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
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
    
    public void closeConnection()
    {
    	try {
			socket.close();
		} catch (IOException e) {
			//doesn't matter becasue we're done with it
		}
    }
    
    //provide public access to client out stream
    public PrintWriter getOutStream()
    {
    	return out;
    }
    //provide public access to client in stream
    
    
    public BufferedReader getInStream()
    {
    	return in;
    }
    
}