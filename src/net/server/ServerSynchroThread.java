package net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

import entity.Entity;
import entity.Snake;
import game.Game;
import game.GameListener;
import net.request.DELRequest;
import net.request.EXITRequest;
import net.request.PUTRequest;
import net.request.Request;
import net.request.SETRequest;
import net.request.TICKRequest;

public class ServerSynchroThread implements Runnable, GameListener {

	/************************************************************************
	 * 
	 * ATTRBUTES
	 * 
	 ************************************************************************/
	
	/** store reference to the client's snake */
	private Snake snake;
	
	/** store reference to the game */
	private Game game;
	
	/** client communication socket */
	private Socket clientComSocket;
	
	/** client communication input stream */
	private BufferedReader inFromClient;
	
	/** client communication output stream */
	private PrintWriter outToClient;
	
	/** Store request types in HashMap */
	private HashMap<String, Request> requestMap = new HashMap<String, Request>();
	
	/** flag to kill Client Thread */
	private boolean isRunning = true;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	public ServerSynchroThread(Socket clientComSocket, Game game, Snake snake) throws IOException {
		
		this.clientComSocket = clientComSocket;
		
		this.snake = snake;
		
		this.game = game;
		
		this.inFromClient = new BufferedReader(new InputStreamReader(
				clientComSocket.getInputStream()
				)) ;
		
		this.outToClient = new PrintWriter(clientComSocket.getOutputStream());
		
	}
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS (GAME EVENT)
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Callback method called each time the game has finished computing a step.
	 * The game stores any entities that have been added/deleted/modified such that
	 * the client may be informed of these changes thanks to the appropriate request.
	 * 
	 */
	@Override
	public void gameStepJob(Game game) {
		
		// Clients are informed of any modified entities by a set request
		for(Entity e : game.getModifiedEntity()) {
			if(e.getClass() == Snake.class) {
				Snake snakeToSet = (Snake) e;
				outToClient.println(new SETRequest(game,snakeToSet, snakeToSet.getMouvement().getDirection()).createRequest());
				outToClient.flush();
			}
		}
		
		// Clients are informed of any added entities by a put request
		for(Entity e : game.getAddedEntity()) {

			outToClient.println(new PUTRequest(e).createRequest());
			outToClient.flush();

		}
		
		// Clients are informed of any removed entities by a DELRequest
		for(Entity e : game.getRemovedEntity()) {
			
			// In case one of the removed entities is the client's snake itself,
			// the client is asked to exit the game.
			if(e.equals(snake)) {
				outToClient.println(new EXITRequest().createRequest());
				outToClient.flush();
				isRunning = false;
			} else {
				outToClient.println(new DELRequest(e).createRequest());
				outToClient.flush();
			}
		}
		
		outToClient.println(new TICKRequest().createRequest());
		outToClient.flush();
	}
	
	/************************************************************************
	 * 
	 * RUNNABLE METHOD
	 * 
	 ************************************************************************/
	
	@Override
	public void run() {
		
		// Initialize request map
		initializeRequestMap(game);
		
		System.out.println("Client " + snake.getID() + " successfully initialized game.");
		
		try {
			// Enter server thread loop where requests are received and handled
			while(isRunning) {
				if(inFromClient.ready()) {
					handleRequest(inFromClient.readLine());
				}
			}
			// When the client has finished speaking with the server, the communication
			// socket is closed and game listener detached
			closeConnection();
			
		} catch (IOException e1) {
			System.err.println("IOException occured with Client " + snake.getID() + " closing socket.");
			try {
				closeConnection();
			} catch (IOException e2) {
				System.err.println("IOException occured with Client " + snake.getID() + " while closing socket.");
				System.err.println("Ignoring error and moving on to end thread");
				return;
			}

			return;
		}
		
		System.out.println("Client " + snake.getID() + " left the game.");
		
	}
	
	/************************************************************************
	 * 
	 * PRIVATE METHODS
	 * 
	 ************************************************************************/
	
	private void handleRequest(String clientInputString) {
		// Identify requestType
		StringTokenizer st = new StringTokenizer(clientInputString);
		String requestType = st.nextToken();
		System.out.println( requestType.toUpperCase() + " request received from Client " + snake.getID() + ".");
		
		// Handle request
		getRequest(requestType).handleRequest(st);
	}
	
	private void closeConnection() throws IOException{
		clientComSocket.close();
	}

	private void initializeRequestMap(Game game) {
		requestMap.put(PUTRequest.KEY, new PUTRequest(game));
		requestMap.put(EXITRequest.KEY, new EXITRequest(new exitCallbackClass()));
		requestMap.put(SETRequest.KEY, new SETRequest(game));
	}
	
	private Request getRequest(String key) {
		return requestMap.get(key);
	}
	
	private class exitCallbackClass implements Runnable {

		@Override
		public void run() {		
			isRunning = false;
			game.removeEntity(snake);
		}
		
	}
	
}
