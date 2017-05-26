package encryptdecryptchat;

import java.net.*;
import java.io.*;
import java.util.*;

//Creating ChatServer
public class ChatServer {
	private Vector<PrintWriter> writers = new Vector<PrintWriter>();

	// Creating main that requests one parameter -> port number
	public static void main(String[] args) {
		new ChatServer(args[0]);
	}

	// Creating ChatServer constructor
	public ChatServer(String socketNum) {
		ServerSocket serverSocket = null;
		Socket clientSocket;

		try {
			// setting ServerSocket with users choosen socket number
			serverSocket = new ServerSocket(Integer.parseInt(socketNum));
			System.out.println("getLocalHost: " + InetAddress.getLocalHost()); // IP
																				// address
																				// of
																				// this
																				// server
			System.out.println("getByName   : "
					+ InetAddress.getByName("localhost")); // IP address of the
															// localhost
			System.out.println("Port Number : " + serverSocket.getLocalPort());
			// Starting server
			while (true) {
				clientSocket = serverSocket.accept();
				MyServer client = new MyServer(clientSocket);
				client.start();
			}
		}// end -> try
		catch (IOException ioe) {
			System.out.println("Something went wrong!");
			ioe.printStackTrace();
		}// end -> catch
		try {
			// Closing socket
			serverSocket.close();
		} catch (IOException ioe) {
			System.out.println("Could not close socket!");
			System.exit(-1);
		}
	}// end -> ChatServer()
		// Creating MyServer thread class

	class MyServer extends Thread {
		// initializing variables for MyServer
		Socket client;
		String clientMsg;
		BufferedReader reader;
		PrintWriter writer;

		// Creating MyServer constructor thread class
		public MyServer(Socket client) {
			this.client = client;
			try {
				reader = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				writer = new PrintWriter(new OutputStreamWriter(
						client.getOutputStream()), true);
				writers.add(writer);
			} catch (IOException ioe) {
				System.out.println("Something went wrong");
				ioe.printStackTrace();
			}
		}// end -> MyServer(Socket)

		public void run() {
			try {
				while ((clientMsg = reader.readLine()) != null) {
					for (PrintWriter pw : writers) {
						pw.println(clientMsg);
						pw.flush();
					}
				}// end -> while
			}// end -> try
			catch (SocketException se) {
				System.out.println("User has disconnected.");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}// end -> run()
	}// end -> MyClientThread
}// end -> ChatServer