package net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

import entity.Snake;
import game.Game;
import mouvement.StraightMouvement;
import net.request.DELRequest;
import net.request.EXITRequest;
import net.request.PUTRequest;
import net.request.Request;
import net.request.SETRequest;
import net.request.TICKRequest;
import net.request.TOCKRequest;

/**
 * 
 * A client thread is created each time a client connects to the server. The 
 * client thread takes care of handling requests from the server asynchronously.
 * 
 * @author ebrunner
 *
 */
public class ClientThread implements Runnable{

	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	/** communication socket with server */	
	private Socket serverComSocket;
	
	/** Snake attributed by the server to the client */
	private Snake snake;
	
	/** Reference to the game which is beeing played */
	private Game game;
	
	/** socket reader */
	private BufferedReader reader;
	
	/** map of requests which the client knows how to handle */
	private static HashMap<String, Request> requestMap = new HashMap<String, Request>();
	
	/** flag indicating that the thread is still running */
	private boolean isRunning = true;
	
	/** reference to the class which will handle tick requests */
	private Runnable tickCallbackClass;
	
	/** reference to the class which will handle tock requests */
	private Runnable tockCallbackClass;
	
	/** key indicating the snake's movement type */
	private String mouvementType;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * The constructor takes a reference to the communication socket and to the game
	 * that will be modified by the server's requests. The snake's movement is set to straight by default.
	 * 
	 * @param serverComSocket - communication socket with the server
	 * @param game - reference to the game
	 * @throws IOException thrown if the reader couldn't be created from the provided socket.
	 * 
	 */
	public ClientThread(Socket serverComSocket, Game game) throws IOException {
		this.serverComSocket = serverComSocket;
		this.game = game;
		reader = new BufferedReader(new InputStreamReader(
				serverComSocket.getInputStream()
				)) ;
		this.mouvementType = StraightMouvement.KEY;
	}
	
	/**
	 * 
	 * Here the snake's movement type may be defined.
	 * 
	 * @param serverComSocket - communication socket with the server
	 * @param game - reference to the game
	 * @param mouvementType - desired movement type key.
	 * @throws IOException thrown if the reader couldn't be created from the provided socket.
	 */
	public ClientThread(Socket serverComSocket, Game game, String mouvementType) throws IOException {
		this(serverComSocket, game);
		this.mouvementType = mouvementType;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC GET/SET METHODS
	 * 
	 ************************************************************************/
	
	public Snake getSnake() {
		return snake;
	}
	
	public void setTickCallbackClass(Runnable tickCallbackClass) {
		this.tickCallbackClass = tickCallbackClass;
	}
	
	public void setTockCallbackClass(Runnable tockCallbackClass) {
		this.tockCallbackClass = tockCallbackClass;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC RUNNABLE METHOD (THREAD LOOP)
	 * 
	 ************************************************************************/
	
	@Override
	public void run() {
		try {
			while(!reader.ready()) {}
			// get this client's snake entity from server
			StringTokenizer st = new StringTokenizer(reader.readLine());
			String requestType = st.nextToken();
			if(!requestType.equals("PUT"))
				throw new RuntimeException("Expected a PUT request as first Server request.");
			System.out.println( requestType.toUpperCase() + " request received.");

			// Handle request
			new PUTRequest(game, mouvementType).handleRequest(st);
			
			// set client snake to first snake in game
			snake = game.getFirstSnake();
			
			System.out.println("You were assigned snake number " + snake.getID() + ".");
			
			// initializeRequestMap
			initializeRequestMap();
			
			while(isRunning) {
				if(reader.ready()) {
					// Identify requestType
					st = new StringTokenizer(reader.readLine());
					requestType = st.nextToken();
					System.out.println( requestType.toUpperCase() + " request received.");

					// Handle request
					getRequest(requestType).handleRequest(st);
				}
			}
		 
			closeConnection();
			
		} catch (IOException e) {
			System.err.println("IOException occured while communicating with serrver. Closing socket.");
			try {
			closeConnection();
			} catch (IOException e2) {
				System.err.println("IOException occured while closing socket.");
				System.err.println("Ignoring error and moving on to end thread");
				return;
			}
			return;
		}
		
	}
	
	/************************************************************************
	 * 
	 * PRIVATE METHODS
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Executes all tasks required before closing connection.
	 * 
	 * @throws IOException
	 */
	private void closeConnection() throws IOException{
		serverComSocket.close();
	}
	
	/**
	 * 
	 * Initialize request map with request handlers.
	 * 
	 */
	private void initializeRequestMap() {
		requestMap.put(PUTRequest.KEY, new PUTRequest(game));
		requestMap.put(EXITRequest.KEY, new EXITRequest(new exitCallbackClass()));
		requestMap.put(SETRequest.KEY, new SETRequest(game));
		requestMap.put(TICKRequest.KEY, new TICKRequest(tickCallbackClass));
		requestMap.put(DELRequest.KEY, new DELRequest(game));
		requestMap.put(TOCKRequest.KEY, new TOCKRequest(tockCallbackClass));
	}
	
	private Request getRequest(String key) {
		return requestMap.get(key);
	}
	
	private class exitCallbackClass implements Runnable {
		
		@Override
		public void run() {		
			snake.dies();
		}
		
	}
	
}
