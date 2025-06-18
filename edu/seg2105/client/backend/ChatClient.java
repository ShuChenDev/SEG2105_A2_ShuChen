// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	boolean logedin = true;
	String id;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String id, String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.id = id;
		this.clientUI = clientUI;
		
		openConnection();
		
		
		
		sendToServer("#login " + id);
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		clientUI.display(msg.toString());

	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {

		try {
			String cmdMsg = message.trim();
			if (cmdMsg.length() >= 0) {
				if (cmdMsg.charAt(0) == '#') { // its a command #
					int msgIndex = 0;
					for (; msgIndex < cmdMsg.length(); msgIndex++) {
						if (cmdMsg.charAt(msgIndex) == ' ') {
							break;
						}
					}
					handleClientUICommand(cmdMsg.substring(0, msgIndex), cmdMsg.substring(msgIndex).trim());
				}
			}

			sendToServer(message);
		} catch (IOException e) {
			if (logedin) {
				clientUI.display("Could not send message to server, terminating");
				quit();
			}
		}
	}

	/**
	 * Excise 2
	 */
	public void handleClientUICommand(String command, String parameter) {
		switch (command) {
		case "#quit":
			try {
				closeConnection();
				System.exit(0);
			} catch (Exception e) {
				System.exit(0);
			}
			break;
		case "#logoff":
			if (!isConnected()) {
				System.out.println("Already disconnected.");
				break;
			}

			try {
				logedin = false;
				closeConnection();
			} catch (Exception e) {
				System.out.println("ERROR: close connection failed");
			}
			break;
		case "#sethost":
			try {
				if (isConnected()) {
					System.out.println("ERROR: Client is connected to server");
				} else {
					setHost(parameter);
				}
			} catch (Exception e) {
				System.out.println("ERROR: Invalid Parameter");
			}
			break;
		case "#setport":
			try {
				if (isConnected()) {
					System.out.println("ERROR: Client is connected to server");
				} else {
					setPort(Integer.parseInt(parameter));
				}
				break;
			} catch (Exception e) {
				System.out.println("ERROR: Invalid Parameter");
			}
			break;
		case "#login":
			try {
				logedin = true;
				if (isConnected()) {
					System.out.println("ERROR: Client is already connected to server");
				} else {
					System.out.println("You can not mannuelly login to server");
				}
			} catch (Exception e) {
				System.out.println("ERROR: login failed");
			}
			break;
		case "#gethost":
			try {
				System.out.println("Current Host: " + getHost());
			} catch (Exception e) {
				System.out.println("ERROR: Get Host Failed");
			}
			break;
		case "#getport":
			try {
				System.out.println("Current Port: " + getPort());
			} catch (Exception e) {
				System.out.println("ERROR: Get Port Failed");
			}
			break;
		}
	}

	/**
	 * Excise 1
	 * 
	 */
	@Override
	public void connectionClosed() {
		clientUI.display("Closed Connection With Server");
		if (logedin) {
			clientUI.display("Server has shut down, terminating client.");
			System.exit(0);
		} else {
			clientUI.display("Logged off from server.");
			logedin = false; // reset flag
		}
	}

	/**
	 * Excise 1
	 * 
	 */
	@Override
	public void connectionException(Exception exception) {
		clientUI.display("Server has shut down, terminating clientd");
		// System.exit(0);
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
