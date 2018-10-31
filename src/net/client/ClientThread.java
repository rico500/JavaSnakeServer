package net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

import entity.Snake;
import game.Game;
import net.request.EXITRequest;
import net.request.PUTRequest;
import net.request.Request;
import net.request.SETRequest;
import net.request.TICKRequest;

public class ClientThread implements Runnable{

	/** communication socket with server */	
	private Socket serverComSocket;
	
	private Snake snake;
	
	private Game game;
	
	private BufferedReader reader;
	
	private static HashMap<String, Request> requestMap = new HashMap<String, Request>();
	
	private boolean isRunning = true;
	
	private Runnable tickCallbackClass;
	
	public ClientThread(Socket serverComSocket, Game game) throws IOException {
		this.serverComSocket = serverComSocket;
		this.game = game;
		reader = new BufferedReader(new InputStreamReader(
				serverComSocket.getInputStream()
				)) ;
	}
	
	public ClientThread(Socket serverComSocket, Game game, Runnable tickCallbackClass) throws IOException {
		this(serverComSocket, game);
		this.tickCallbackClass = tickCallbackClass;
	}
	
	public Snake getSnake() {
		return snake;
	}
	
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
			new PUTRequest(game).handleRequest(st);
			
			// set client snake to first snake in game
			snake = (Snake) game.getFirstEntity();
			
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
	
	private void closeConnection() throws IOException{
		serverComSocket.close();
	}
	
	private void initializeRequestMap() {
		requestMap.put(PUTRequest.KEY, new PUTRequest(game));
		requestMap.put(EXITRequest.KEY, new EXITRequest(new exitCallbackClass()));
		requestMap.put(SETRequest.KEY, new SETRequest(game));
		requestMap.put(TICKRequest.KEY, new TICKRequest(tickCallbackClass));
	}

	private Request getRequest(String key) {
		return requestMap.get(key);
	}
	
	private class exitCallbackClass implements Runnable {

		@Override
		public void run() {		
			isRunning = false;
		}
		
	}
	
}