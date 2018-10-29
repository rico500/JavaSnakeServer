package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.StringTokenizer;

import game.Game;
import net.request.PUTRequest;
import net.request.Request;
import net.server.Server;

public class ClientPUTTest {
	
	private static HashMap<String, Request> requestMap = new HashMap<String, Request>();

	private static void initializeRequestMap(Game game) {
		requestMap.put(PUTRequest.KEY, new PUTRequest(game));
	}

	private static Request getRequest(String key) {
		return requestMap.get(key);
	}

	public static void main(String[] args) throws UnknownHostException, IOException {

		int port = Integer.parseInt(args[0]);
		String machine = args[1];

		Socket comSocket = new Socket(machine, port);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				comSocket.getInputStream()
				)) ;
		
		Game game = new Game();
		game.startGame();
		
		initializeRequestMap(game);

		while(true) {
			if(reader.ready()) {
				// Identify requestType
				StringTokenizer st = new StringTokenizer(reader.readLine());
				
				// Handle request
				ClientPUTTest.getRequest(st.nextToken()).handleRequest(st);
			}
		}
	}
}
