// Geoffrey Pitman
// CSC464 - HCI
// 6/30/16
// Iteration 2
// ClientGUI.java
// ClientGUI Class Purpose: This class' purpose is to provide the GUI
//                front end for the TCP client application.

//  ****note**** this file will look for images in "resources/<image file>"
// ****note*** compiled using jre-8
//


import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ClientGUI implements ActionListener{
	
    Client client; // reference to client
    BufferedWriter writer = null; // file writer
    
    // the purpose for the following variables will be explained with comments upon their use
    JLabel portLabel, addrLabel, dirLabel, dlfileLabel, statusLabel, arrowgif;
    JLabel welcomeLabel, listLabel, iconLabel, arrowLabel, sboxLabel, cboxLabel, dlarrowLabel;
    JList<ListEntry> dirList, clientList;
    ImageIcon logoImage;
    AnimatedIcon iconArrow;
    DefaultListModel<ListEntry> listModel, clientModel;
    JCheckBox portCheck;
    JScrollPane listScroller, clientScroller;
    JButton connButton, dlButton, discButton, cdButton, dirButton;
    JTextField addrText, portText, dirText, cdText;
    private static Vector<String> filelist, iconlist;
    private static JFrame discFrame, connFrame;
    private static boolean flag = false;
    private static String address, homepath = null;
    private static int port;

    // init gui layouts
    public ClientGUI(Client clientarg)
    {
    	super();
    	client = clientarg;
    	discFrame = new JFrame();
    	discFrameInit();
    	connFrame = new JFrame();
    	connFrameInit();
    }
    
    // initialize connected mode UI layout
    public void discFrameInit()
    {
    	 //general JFrame settings
    	 discFrame.setAutoRequestFocus(true);
         discFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
         discFrame.setBounds(475, 210, 460, 295);
         discFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         discFrame.getContentPane().setLayout(null);
         logoImage = new ImageIcon("resources/applogo.png");
         discFrame.setIconImage(logoImage.getImage());
         
         // kutztown logo
         welcomeLabel = new JLabel("");
         welcomeLabel.setIcon(new ImageIcon("resources/main_logo.png"));
         welcomeLabel.setBounds(50, 25, 350, 84);         
         discFrame.getContentPane().add(welcomeLabel);
         
         // label for host text field
         addrLabel = new JLabel("Host:");
         addrLabel.setBounds(95, 130, 80, 20);
         discFrame.getContentPane().add(addrLabel);
         
         // address 
         addrText = new JTextField(40);
         addrText.setToolTipText("Enter host name/address");
         addrText.setBounds(135, 130, 220, 20);
         // allows user to press enter key instead of clicking connect button
         addrText.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 connButton.doClick();
             }
         });
         discFrame.getContentPane().add(addrText);
        
         // check box to enable/disable port text field
         portCheck = new JCheckBox();
         portCheck.setToolTipText("Click to enable/disable port option" );
         portCheck.setBounds(68, 170, 20, 20);
         portCheck.addItemListener(new ItemListener() {
             @Override
             public void itemStateChanged(ItemEvent e) {
                 if (e.getStateChange() == ItemEvent.SELECTED) {
                     portText.setEnabled(true);
                     portText.setText("");
                     portText.repaint();
                     discFrame.getContentPane().repaint();

                 } else {
                     portText.setEnabled(false);
                     portText.setDisabledTextColor(Color.gray);
                     // display the default port number used
                     portText.setText("55536");
                     portText.repaint();
                     discFrame.getContentPane().repaint();
                 }
             }
         });
         discFrame.getContentPane().add(portCheck);
         
         // label for port text field
         portLabel = new JLabel("Port:");
         portLabel.setBounds(95, 170, 80, 20);
         discFrame.getContentPane().add(portLabel);
         
         // port text field
         portText = new JTextField(40);
         portText.setToolTipText("Click check box to enable/disable this field");
         portText.setBounds(135, 170, 220, 20);
         portText.setDisabledTextColor(Color.gray);
         portText.setText("55536");
         discFrame.getContentPane().add(portText);
         portText.setEnabled(false);
         // allows user to press enter key instead of clicking connect button
         portText.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 connButton.doClick();
             }
         });
         
         // connect button
         connButton = new JButton("Connect");
         connButton.addActionListener(this);
         connButton.setBounds(160, 210, 150, 30);
         discFrame.getContentPane().add(connButton);
         discFrame.setTitle("TCP/IP Socket Client: disconnected");
         discFrame.setVisible(true);
         discFrame.setResizable(false);
    }
    
    // initialize connected mode UI layout
    public void connFrameInit()
    {
    	 // general JFrame settings
    	 connFrame.setAutoRequestFocus(true);
         connFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
         connFrame.setBounds(250, 100, 800, 540);
         connFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         connFrame.getContentPane().setLayout(null);
         logoImage = new ImageIcon("resources/applogo.png");
         connFrame.setIconImage(logoImage.getImage());
         
         // welcome label for connected UI
         statusLabel = new JLabel("");
         statusLabel.setIcon(new ImageIcon("resources/main_logo.png"));
         statusLabel.setBounds(125, 10, 350, 84);
         connFrame.getContentPane().add(statusLabel);
         
         // extra visual help to indicate where cwd is on the UI
         arrowLabel = new JLabel("");
         arrowLabel.setIcon(new ImageIcon("resources/arrow.png"));
         arrowLabel.setBounds(240, 120, 64, 30);
         connFrame.getContentPane().add(arrowLabel);
         
         // label for current directory path
         dirLabel = new JLabel("Server current directory");
         dirLabel.setBounds(97, 120, 160, 30);
         connFrame.getContentPane().add(dirLabel);
         
         // text field displaying current directory path
         // text field is not editable, but can use to copy/paste path
         dirText = new JTextField(50);
         dirText.setText("");
         dirText.setToolTipText("Highlight path and press ctrl-C to copy");
         dirText.setEditable(false);
         dirText.setBounds(325, 120, 400, 30);
         connFrame.getContentPane().add(dirText);
         
         // button to change directory
         cdButton = new JButton("Change Directory");
         cdButton.addActionListener(this);
         cdButton.setBounds(75, 170, 200, 30);
         connFrame.getContentPane().add(cdButton);
         
         // text field to enter directory path for CD
         cdText = new JTextField();
         cdText.setToolTipText("Enter path of desired directory");
         cdText.setBounds(325, 170, 400, 30);
         // allows user to press enter key instead of clicking
         cdText.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 cdButton.doClick();
             }
         });
         connFrame.getContentPane().add(cdText);
         
         // this is way I ultimately had to define and initialize the list box.
         // it seems overly complicated.
         // It required...
         //	...two separate class definitions (one for list object, one for list graphics)
         // ...embedding the default list model in the JList object
         // ...embedding the JList object in the JScrollPane
         // I know there is a more eloquent way to add list items with icons BUT,
         // it did not work when I tried to dynamically add and remove items.
         // If I had more time I would have done more exploration.
         //
         // server side working directory
         listModel = new DefaultListModel<ListEntry>();
         dirList = new JList<ListEntry>(listModel);
         dirList.setCellRenderer(new ListEntryCellRenderer());
         dirList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         dirList.setSelectedIndex(0);
         dirList.setVisibleRowCount(3);
         listScroller = new JScrollPane(dirList,  ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         listScroller.setBounds(75, 230, 200, 250);
         connFrame.getContentPane().add(listScroller);
         
         // client side download directory
         clientModel = new DefaultListModel<ListEntry>();
         clientList = new JList<ListEntry>(clientModel);
         clientList.setCellRenderer(new ListEntryCellRenderer());
         clientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         clientList.setSelectedIndex(0);
         clientList.setVisibleRowCount(3);
         clientScroller = new JScrollPane(clientList,  ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         clientScroller.setBounds(525, 230, 200, 250);
         connFrame.getContentPane().add(clientScroller);
         
         // identify server side directory
         sboxLabel = new JLabel("Server directory contents:");
         sboxLabel.setBounds(100, 205, 150, 30);
         connFrame.getContentPane().add(sboxLabel);
         
         // identify client side directory
         cboxLabel = new JLabel("Client directory contents:");
         cboxLabel.setBounds(547, 205, 150, 30);
         connFrame.getContentPane().add(cboxLabel);
         
         // user instructions for downloading
         dlfileLabel = new JLabel("Select file from Server, click 'Download'");
         dlfileLabel.setBounds(288, 260, 300, 30);
         connFrame.getContentPane().add(dlfileLabel);
        
         // download button
         dlButton = new JButton("Download");
         dlButton.addActionListener(this);
         dlButton.setBounds(325, 300, 150, 30);
         connFrame.getContentPane().add(dlButton);
         
         // arrow indicator to show direction of download
         //dlarrowLabel = new JLabel("");
         //dlarrowLabel.setIcon(new ImageIcon("resources/dlarrow.png"));
         //dlarrowLabel.setBounds(300, 350, 250, 100);
         //connFrame.getContentPane().add(dlarrowLabel);
         
         // uses animated icon class to play a gif
         // the arrow images signal the direction of file transfer
         arrowgif = new JLabel("");
         iconArrow = new AnimatedIcon( arrowgif );
         iconArrow.addIcon(new ImageIcon("resources/dlarrow.png"));
         iconArrow.addIcon(new ImageIcon("resources/dlarrowY1.png"));
         iconArrow.addIcon(new ImageIcon("resources/dlarrowY2.png"));
         iconArrow.addIcon(new ImageIcon("resources/dlarrowY3.png"));
         iconArrow.addIcon(new ImageIcon("resources/dlarrowY4.png"));
         iconArrow.addIcon(new ImageIcon("resources/dlarrowR1.png"));
         iconArrow.addIcon(new ImageIcon("resources/dlarrowR2.png"));
         iconArrow.addIcon(new ImageIcon("resources/dlarrowR3.png"));
         arrowgif.setIcon(iconArrow);
         arrowgif.setToolTipText("Transfer a file from the current server directory to the client's download directory!!");
         arrowgif.setBounds(300, 350, 250, 100);
         connFrame.getContentPane().add(arrowgif);
         iconArrow.setDelay(1250);
         
         // disconnect button
         discButton = new JButton("Disconnect");
         discButton.addActionListener(this);
         discButton.setBounds(545, 35, 150, 30);
         connFrame.getContentPane().add(discButton);
    }
    
    // button click action listeners
    // responding events are defined outside of this method
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	        Object source = e.getSource();
	        flag = false;
	        if (source == connButton) {
	        	this.connButtonClick();
	        }
	        else if(source == discButton){
	        	this.discButtonClick();
	        }
	        else if(source == cdButton){
	        	this.cdButtonClick();
	        }
	        else if(source == dlButton){
	        	this.dlButtonClick();
	        }
	}
    
    // responding event for connect button click.
    // asks client to connect to user specified host.
    // will transform connected UI layout to disconnected layout,
    //		upon successful connection.
    public void connButtonClick()
    {
    	address = addrText.getText();
        // null field check
    	if (addrText.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,
                    "You must enter a host to connect!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        else if (portCheck.isSelected()) 
        {
        	// null field check
            if (portText.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null,
                        "You checked the port option...\nnow you must enter a port!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else
            {   
            	// parse port input
                String temp = new String(portText.getText().toString());
                for (int idx =0; idx < portText.getText().length(); idx++)
                {
                    if(!Character.isDigit(temp.charAt(idx)))
                    {
                        JOptionPane.showMessageDialog(null,
                                "The port field takes numbers only!",
                                "ERROR",
                                JOptionPane.WARNING_MESSAGE);
                        flag = true;
                        break;
                    }
                }
                if (!flag)// won't run if non numerics. will prevent int exceptions
                {
                    port = Integer.parseInt(portText.getText());
                    homepath = new String(client.getServer(address, port));
                    if (homepath != null) {
                    	// transform layout from disconnected UI to connected
                        discToConn();
                     // populate server list box with current directory listing
                        doServerListing();
                        // populate client list box download directory listing
                        doClientListing();
                    }
                }
            }
        }
        else {
        	// port defaults to 55536 if user does not opt to enter one
            port = 55536;
            try{
            	// ask the client to connect to desired host
            	homepath = new String(client.getServer(address, port));
                if (homepath != null)
                {
                	// transform layout from disconnected UI to connected
                    discToConn();
                    // populate server list box with current directory listing
                    doServerListing();
                    // populate client list box with download directory listing
                    doClientListing();

                }
            }
            catch (NullPointerException ex)
            {
               // input is validated, not possible
            }
        }
    }
    
    // responding event for download button click
    // asks client to transfer a file line by line
    public void dlButtonClick()
    {
        boolean selection = dirList.isSelectionEmpty();

        if (!selection)
        {	
        	boolean exists = false;
        	// ask client to download specified file
            client.getOutStream().flush();
            client.getOutStream().println("DOWNLOAD " + dirList.getSelectedValue().toString());
            try {
                int res = 1;
                // handle existing file / over write case
                boolean check = new File("downloads/", dirList.getSelectedValue().toString()).exists();
                if (check)
                {
                	exists = true;
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "File exists. Overwrite?", "Warning!", dialogButton);
                    if (dialogResult != 0)
                        res = 0;
                }
                // ready to start downloading
                if (res == 1)
                {
                	// ask client for first line of file
                    String line = client.getInStream().readLine();
                    if (!line.contains("@@@dlfailure@@@"))
                    {
                    	// open file and out stream
                    	writer = new BufferedWriter(new FileWriter("downloads/" + dirList.getSelectedValue().toString()));
                    	// while there is still more to read...
                        while (!line.contains("@@@dlfailure@@@") && !line.contains("@@@dlsuccess@@@"))
                        {
                            try
                            {
                            	if (!line.isEmpty() && line != null && !line.contains("@@@blank@@@"))
                            	{
                            		// write file line by line
                            		writer.write(line);
                            		writer.newLine();
                            		// ask client for next line of file
                            		line = client.getInStream().readLine();
                            	}
                            	else if (line.contains("@@@blank@@@"))
                            	{
                            		// write in blank line
                            		writer.newLine();
                            		line = client.getInStream().readLine();
                            	}
                            }
                            catch (IOException e)
                            {
                                System.err.println("Error: " + e.getMessage());
                                writer.close();
                                break;
                            }
                        }
                        // close file and buffer stream
                        writer.close();
                    }
                    // alert user that download has failed
                    if (line.contains("@@@dlfailure@@@"))
                    {
                    	String err = new String(client.getInStream().readLine());
                        JOptionPane.showMessageDialog(null,
                                err,
                                "Failure",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    // alert user that download has succeeded
                    else if (line.contains("@@@dlsuccess@@@")) {
                    	if (!exists)
                    	{
                    		// I may have been blowing off false negatives when I was trying to do this
                    		//         with the class defined methods.  
                    		// I think there is a much more eloquent way to do this
                    		clientModel.addElement(new ListEntry(filelist.elementAt(dirList.getSelectedIndex()), new ImageIcon(iconlist.elementAt(dirList.getSelectedIndex()))));
                    		connFrame.getContentPane().repaint();
                    	}
                        JOptionPane.showMessageDialog(null,
                                "File transferred successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Connection has been dropped!",
                        "ERROR",
                        JOptionPane.WARNING_MESSAGE);
                connToDisc();
            }
        } else {
            // null field check
        	JOptionPane.showMessageDialog(null,
                    "You must select a file first!",
                    "ERROR",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // responding event to disconnect button click.
    // will transform connected UI layout to disconnected layout.
    public void discButtonClick()
    {
    	// confirm user's intent to exit
    	int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect?", "Confirm", dialogButton);
        if(dialogResult == 0) {
              	client.getOutStream().println("BYE");
              	// transform layout back to disconnected UI
              	connToDisc();
                listModel.clear();
                clientModel.clear();
                connFrame.getContentPane().repaint();
                // tell client we don't need its connection anymore
                client.closeConnection();
        }
    }
    
    // responding event to change directory button click
    // ask client to change current directory to user's entered path
    public void cdButtonClick()
    {
    	// check for empty text field
    	if (cdText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Enter a path first!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        else{
        	// ask client to change directory
            try {
            	client.getOutStream().flush();
                client.getOutStream().println("CD " + cdText.getText().toString());
                String line = new String(client.getInStream().readLine());
                // if change is successful, set new homepath and let user know
                if (line.contains("cdsuccess")) 
                {
                    dirText.setText(getHomepath());
                    cdText.setText("");
                    listModel.clear();
                    connFrame.getContentPane().repaint();
                    JOptionPane.showMessageDialog(null,
                            "Directory changed successfully!",
                            "SUCCESS!!",
                            JOptionPane.INFORMATION_MESSAGE);
                    // get new directory's listing
                    doServerListing();
                } else {
                	// display dialog box if directory change fails
                    JOptionPane.showMessageDialog(null,
                            line,
                            "ERROR",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Connection has been dropped!",
                        "ERROR",
                        JOptionPane.WARNING_MESSAGE);
                connToDisc();
            }
        }
    }
    
    // populate server list box with file objects generated from current directory listing
	public void doServerListing()
    {
    	 try {
    		 // ask for list from client
             client.getOutStream().flush();
             client.getOutStream().println("DIR\n");
             // get list from client
             String temp = client.getInStream().readLine().trim();
             String line = new String(temp.trim());
             //parse python list string
             // ex: ['physics', 'chemistry', 'cs']
             if (!line.contains("@@@empty@@@")) // if directory has files to list
             {
                 filelist = new Vector<String>(); //keep track file and icon location in listbox
                 iconlist = new Vector<String>();
                 int count = 0, apos = 0, endapos = 0; // apostrophe count and position;
                 for(int idx = 1; idx < line.length()-1; idx++)
                 {
                     if (line.charAt(idx) == '\'')
                     {
                         if (idx == 0) {
                             count = 1;
                             apos = idx;
                         } else if (count > 0) {
                             endapos = idx;
                             count = 0;
                             filelist.add(line.substring(apos+1, endapos));
                         } else {
                             apos = idx;
                             count = 1;
                         }
                     }
                 }
                 // clear items, add items, and repaint listbox
                 listModel.clear();
                 for (int idx =0; idx < filelist.size(); idx++)
                 {
                	 if (!filelist.elementAt(idx).contains("server.py")) // prevent possiblity of server code overwrite
                	 { 
                		 //python file type
                		 if (filelist.elementAt(idx).contains(".py"))
                		 {
                			 iconlist.addElement("resources/py.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/py.png")));
                		 }
                		 //ruby file type
                		 else if (filelist.elementAt(idx).contains(".rb"))
                		 {
                			 iconlist.addElement("resources/ruby.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/ruby.png")));
                		 }
                		 //xml file type
                		 else if (filelist.elementAt(idx).contains(".xml"))
                		 {
                			 iconlist.addElement("resources/xml.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/xml.png")));
                		 }
                		 //html file type
                		 else if (filelist.elementAt(idx).contains(".html"))
                		 {
                			 iconlist.addElement("resources/html.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/html.png")));
                		 }
                		 //php file type
                		 else if (filelist.elementAt(idx).contains(".php"))
                		 {
                			 iconlist.addElement("resources/php.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/php.png")));
                		 }
                		 //txt file type
                		 else if (filelist.elementAt(idx).contains(".txt"))
                		 {
                			 iconlist.addElement("resources/txt.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/txt.png")));
                		 }
                		 //perl file type
                		 else if (filelist.elementAt(idx).contains(".pl"))
                		 {
                			 iconlist.addElement("resources/perl.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/perl.png")));
                		 }
                		 //cpp file type
                		 else if (filelist.elementAt(idx).contains(".cpp"))
                		 {
                			 iconlist.addElement("resources/cpp.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/cpp.png")));
                		 }
                		 //css file type
                		 else if (filelist.elementAt(idx).contains(".css"))
                		 {
                			 iconlist.addElement("resources/css.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/css.png")));
                		 }
                		 //java file type
                		 else if (filelist.elementAt(idx).contains(".java"))
                		 {
                			 iconlist.addElement("resources/java.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/java.png")));
                		 }
                		 //csv file type
                		 else if (filelist.elementAt(idx).contains(".csv"))
                		 {
                			 iconlist.addElement("resources/csv.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/csv.png")));
                		 }
                		 //json file type
                		 else if (filelist.elementAt(idx).contains(".json"))
                		 {
                			 iconlist.addElement("resources/json.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/json.png")));
                		 }
                		 //javascript file type
                		 else if (filelist.elementAt(idx).contains(".js") && !filelist.elementAt(idx).contains(".json"))
                		 {
                			 iconlist.addElement("resources/js.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/js.png")));
                		 }
                		 //c sharp file type
                		 else if (filelist.elementAt(idx).contains(".cs") && !filelist.elementAt(idx).contains(".css")  && !filelist.elementAt(idx).contains(".csv"))
                		 {
                			 iconlist.addElement("resources/csharp.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/csharp.png")));
                		 }
                		 //c file type
                		 else if (filelist.elementAt(idx).contains(".c") && !filelist.elementAt(idx).contains(".cp") && !filelist.elementAt(idx).contains(".cs") && !filelist.elementAt(idx).contains(".cl"))
                		 {
                			 iconlist.addElement("resources/c.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/c.png")));
                		 }
                		 //ascii file of unknown type
                		 else
                		 {
                		 	 iconlist.addElement("resources/ascii.png");
                			 listModel.addElement(new ListEntry(filelist.elementAt(idx), new ImageIcon("resources/ascii.png")));
                		 }
                	 } 
                 }
                 connFrame.getContentPane().repaint();
             }
             // display dialog box if directory has no files to list
             else {
                 listModel.clear();
                 connFrame.getContentPane().repaint();
                 JOptionPane.showMessageDialog(null,
                         "Directory has no files to list",
                         "Warning",
                         JOptionPane.WARNING_MESSAGE);
             }
         } catch (IOException e) {
             JOptionPane.showMessageDialog(null,
                     "Connection has been dropped!",
                     "ERROR",
                     JOptionPane.WARNING_MESSAGE);
             connToDisc();
             listModel.clear();
             clientModel.clear();
             connFrame.getContentPane().repaint();
         }
    }
    
    // populate client list box with file objects generated from download directory listing
    public void doClientListing()
    {
    	File f = new File("downloads/");
    	ArrayList<String> clientdir = new ArrayList<String>(Arrays.asList(f.list()));
    	 for (int idx =0; idx < clientdir.size(); idx++)
         {
        	 if (!clientdir.get(idx).contains("server.py")) // prevent possiblity of server code overwrite
        	 { 
        		 //python file type
        		 if (clientdir.get(idx).contains(".py"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/py.png")));
        		 //ruby file type
        		 else if (clientdir.get(idx).contains(".rb"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/ruby.png")));
        		 //xml file type
        		 else if (clientdir.get(idx).contains(".xml"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/xml.png")));
        		 //html file type
        		 else if (clientdir.get(idx).contains(".html"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/html.png")));
        		 //php file type
        		 else if (clientdir.get(idx).contains(".php"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/php.png")));
        		 //txt file type
        		 else if (clientdir.get(idx).contains(".txt"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/txt.png")));
        		 //perl file type
        		 else if (clientdir.get(idx).contains(".pl"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/perl.png")));
        		 //cpp file type
        		 else if (clientdir.get(idx).contains(".cpp"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/cpp.png")));
        		 //css file type
        		 else if (clientdir.get(idx).contains(".css"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/css.png")));
        		 //java file type
        		 else if (clientdir.get(idx).contains(".java"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/java.png")));        		 
        		 //csv file type
        		 else if (clientdir.get(idx).contains(".csv"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/csv.png")));
        		 //json file type
        		 else if (clientdir.get(idx).contains(".json"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/json.png")));
        		 //javascript file type
        		 else if (clientdir.get(idx).contains(".js") && !clientdir.get(idx).contains(".json"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/js.png")));
        		 //c sharp file type
        		 else if (clientdir.get(idx).contains(".cs") && !clientdir.get(idx).contains(".csv") && !clientdir.get(idx).contains(".css"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/csharp.png")));
        		 //c file type
        		 else if (clientdir.get(idx).contains(".c") && !clientdir.get(idx).contains(".cs") && !clientdir.get(idx).contains(".cp"))
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/c.png")));
        		 // unknown file type
        		 else
        			 clientModel.addElement(new ListEntry(clientdir.get(idx), new ImageIcon("resources/question.png")));
        	 } 
         }
    	connFrame.getContentPane().repaint();
    }
    
	// ask the client for current homepath
	// RETURNS: String - if successful server homepath
    //			null - if unsuccessfulS
    public String getHomepath()
    {
    	client.getOutStream().flush();
    	client.getOutStream().println("HOMEPATH");
    	try {
			homepath = client.getInStream().readLine();
			return homepath;
		} catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Connection has been dropped!",
                    "ERROR",
                    JOptionPane.WARNING_MESSAGE);
            connToDisc();
            return null;
		}
    }
    
    // transform connected UI layout into disconnected layout
    public void connToDisc()
    {
    	iconArrow.restart();
    	iconArrow.stop();
        discFrame.setTitle("TCP/IP Socket Client: disconnected");
        connFrame.setVisible(false);
        discFrame.setVisible(true);
        discFrame.setResizable(false);
        
    }
    
    // transform disconnected UI layout into connected layout
    public void discToConn()
    {
        connFrame.setTitle("TCP/IP Socket Client: connected to <" + address + "> on port <" + port + ">");
        dirText.setText(homepath);
        discFrame.setVisible(false);
        connFrame.setVisible(true);
        connFrame.setResizable(false);
        JOptionPane.showMessageDialog(null,
        		"Welcome to the new TCP File Transfer Client! **ASCII files only**",
                "Connected to " + addrText.getText() + " on port " + portText.getText(),
                JOptionPane.INFORMATION_MESSAGE);
		iconArrow.restart();
		iconArrow.start();
    }
    
}
