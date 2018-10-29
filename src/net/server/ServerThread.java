package net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import entity.Snake;
import game.Game;
import net.request.PUTRequest;

public class ServerThread implements Runnable {

	/************************************************************************
	 * 
	 * ATTRBUTES
	 * 
	 ************************************************************************/
	
	/** store reference to the client's snake */
	private Snake snake;
	
	/** client communication socket */
	private Socket clientComSocket;
	
	/** client ID */
	private int ID;
	
	/** client communication input stream */
	private BufferedReader inFromClient;
	
	/** client communication output stream */
	private PrintWriter outToClient;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	public ServerThread(Socket clientComSocket, Snake snake, int ID) throws IOException {
		
		this.clientComSocket = clientComSocket;
		
		this.snake = snake;
		
		this.ID = ID;
		
		this.inFromClient = new BufferedReader(new InputStreamReader(
				clientComSocket.getInputStream()
				)) ;
		
		this.outToClient = new PrintWriter(clientComSocket.getOutputStream());
		
	}
	
	/************************************************************************
	 * 
	 * RUNNABLE METHOD
	 * 
	 ************************************************************************/
	
	@Override
	public void run() {
		
		// TODO Put all snakes in game
		outToClient.println(new PUTRequest(snake).createRequest());
		outToClient.flush();
		
		System.out.println("Client " + ID + "successfully initialized game.");
		
		while(true) {
			try {
				handleRequest(inFromClient.readLine());
			} catch (IOException e1) {
				System.err.println("IOException occured with Client " + ID + " closing socket.");
				try {
				closeConnection();
				} catch (IOException e2) {
					System.err.println("IOException occured with Client " + ID + " while closing socket.");
					System.err.println("Ignoring error and moving on to end thread");
					return;
				}
				
				return;
			}
		}
	}
	
	/************************************************************************
	 * 
	 * PRIVATE METHODS
	 * 
	 ************************************************************************/
	
	private void handleRequest(String clientInputString) {
		
		// Identify requestType
		StringTokenizer st = new StringTokenizer(clientInputString);
		
		// Handle request
		Server.getRequest(st.nextToken()).handleRequest(st);
	}
	
	private void closeConnection() throws IOException{
		clientComSocket.close();
	}

}
