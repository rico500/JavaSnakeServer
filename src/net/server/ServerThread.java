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
import net.request.EXITRequest;
import net.request.PUTRequest;
import net.request.Request;
import net.request.SETRequest;

public class ServerThread implements Runnable, GameListener {

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
	boolean isRunning = true;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	public ServerThread(Socket clientComSocket, Game game, Snake snake) throws IOException {
		
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
	
	@Override
	public void gameStepJob(Game game) {
		for(Entity e : game.getEntityMap().values()) {
			if(e.getClass() == Snake.class) {
				Snake snakeToSet = (Snake) e;
				outToClient.println(new SETRequest(game,snakeToSet, snakeToSet.getMouvement().getDirection()).createRequest());
				outToClient.flush();
			}
		}
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
		
		// TODO Put all snakes in game
		outToClient.println(new PUTRequest(snake).createRequest());
		outToClient.flush();
		
		System.out.println("Client " + snake.getID() + " successfully initialized game.");
		
		try {
			while(isRunning) {
				if(inFromClient.ready()) {
					handleRequest(inFromClient.readLine());
				}
			}
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
