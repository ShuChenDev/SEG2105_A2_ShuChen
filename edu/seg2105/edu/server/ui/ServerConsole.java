package edu.seg2105.edu.server.ui;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;
import java.io.*;
import java.util.Scanner;

import edu.seg2105.edu.server.backend.*;
import edu.seg2105.client.common.*;



import edu.seg2105.edu.server.backend.EchoServer;
/**
 * This class constructs the UI for a chat server.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the server that created this ConsoleChat.
   */
  EchoServer server;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
    try 
    {
      server = new EchoServer(port);
      server.listen();
    } 
    catch(Exception exception) 
    {
      System.out.println("Error: Can't start server!"
                + " Terminating server.");
      System.exit(1);
    }
    
    // Create scanner object to read from server
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the server's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();

        if(message.length() > 0 && message.charAt(0) == '#') {
        	String cmdMsg = message.trim();
	  		  if(cmdMsg.length() >= 0) {
	  			  if (cmdMsg.charAt(0) == '#') { //its a command #
	  				  int msgIndex = 0;
	  				  for(;msgIndex < cmdMsg.length(); msgIndex++) {
	  					  if(cmdMsg.charAt(msgIndex) == ' ') {
	  						  break;
	  					  }
	  				  }
	  				  handleServerUICommand(cmdMsg.substring(0, msgIndex), cmdMsg.substring(msgIndex).trim());
	  			  }
	  		  }        	
        }
        else {
        	display(message);
            server.sendToAllClients("SERVER MSG> " + message);
        }
      
      
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  
  public void handleServerUICommand(String command, String parameter) {
	  switch (command) {
	  	case "#quit":
	  		try
	  		{
		  		System.exit(0);
	  		}
	  		catch(Exception e) {
	  			System.exit(0);
	  		}
	  		break;
	  	case "#stop":
	  		try
	  		{
		  		server.stopListening();
	  		}
	  		catch(Exception e) {
	  			System.out.println("ERROR STOP SERVER LISTENING");
	  		}
	  		break;
	  	case "#close":
	  		try
	  		{
		  		server.close();
	  		}
	  		catch(Exception e) {
	  			System.out.println("ERROR CLOSING SERVER");
	  		}
	  		break;
	  	case "#setport":
		  		try {
		  			if(server.isListening() || server.getNumberOfClients() > 0) {
		  				System.out.println("ERROR: Can not change port is server is not closed");
		  			}
		  			else {
		  				server.setPort(Integer.parseInt(parameter));
		  			}		  		}
		  		catch(Exception e) {
	  				System.out.println("ERROR: Set Port Failed");
		  		}
	  		break;
	  	case "#start":
	  		try {
	  			if(server.isListening()) {
	  				System.out.println("Server is already listening");
	  			}
	  			else {
	  				server.listen();
	  				System.out.println("Server start listening");
	  			}
	  		}
	  		catch(Exception e) {
  				System.out.println("ERROR: Set Port Failed");
	  		}
	  		break;
	  	case "#getport":
	  		try {
		  		System.out.println("Current Port: " + server.getPort());
	  		}
	  		catch(Exception e) {
  				System.out.println("ERROR: Get Port Failed");
	  		}
	  		break;
	  }
  }
  
  
  
  
  
  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("SERVER MSG> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the server UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";

    int port;
    
    try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    
    try
    {
    	port = Integer.parseInt(args[1]);
    }
    catch(Exception e) {
    	port = DEFAULT_PORT;
    }
    
    ServerConsole chat= new ServerConsole(port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class

