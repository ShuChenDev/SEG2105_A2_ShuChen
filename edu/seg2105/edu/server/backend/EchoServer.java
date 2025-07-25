package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {
			String id = "";
		
			if (client.getInfo("loginId") == null) {
				
				String message[] = msg.toString().trim().split(" ");
				
				if(!message[0].equals("#login") || message[1].trim().isEmpty()) {
					System.out.println("ERROR: You must have an login id as first message, login failed");
					client.close();
					return;
				}
				
				id = message[1].trim();
				client.setInfo("loginId", id);
				System.out.println("User " + id + " loged in");
			} 
			else {
				id = client.getInfo("loginId").toString();
				System.out.println("Message received from " + id + ": " + msg + " from " + client);
				this.sendToAllClients(id + ": " + msg);
			}
		
		}
		catch(Exception e) {
			System.out.println("ERROR: Failed to handle message from client");
		}
	
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * Excise 1
	 */
	protected void clientConnected(ConnectionToClient client) {
		System.out.println(client.toString() + " has connected");
	}

	/**
	 * Excise 1
	 */
	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		System.out.println(client.toString() + " has disconnected");
	}

	/**
	 * Excise 1
	 */
	@Override
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		System.out.println(client.toString() + " has disconnected");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class
