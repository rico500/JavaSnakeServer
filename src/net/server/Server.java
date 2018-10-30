package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import entity.Snake;
import game.Game;
import javafx.scene.paint.Color;
import mouvement.Directions;
import mouvement.StraightMouvement;
import net.request.PUTRequest;
import net.request.Request;

public class Server {

	private static Game game;
	
	public static void main(String[] args) throws IOException {
		
		// Read server listener port from launch arguments
		int port;
		
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.err.println("Error while reading port parameter. It must be an integer.");
			throw e;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Please enter port parameter");
			throw e;
		}
		
		// Construct server socket. Make sure its in bounds 
		ServerSocket acceptSocket;
		
		try {
			acceptSocket = new ServerSocket(port);
		} catch (IllegalArgumentException e) {
			System.err.println("Error while reading port parameter. It must be an integer between 0 and 65535.");
			throw e;
		} 
		
		// Start game
		game = new Game();
		game.startGame();
		
		// Initialize Client counter
		int counter = 1;
		
		// Accept clients as they come
		while(true) {
			// wait for client to connect
			Socket clientComSocket = acceptSocket.accept();
			
			System.out.println("New client accpeted. ID: " + counter);
			
			// assign new snake to client
			// TODO Put new snake in a position with no other snakes
			// TODO Assign logical initial direction
			// TODO Assign random color
			Snake snake = new Snake(new StraightMouvement(Directions.NORTH), 0, 0, Color.AQUAMARINE);
			
			// add snake to game simulation
			// TODO add unique ID finder
			game.addEntity(counter, snake);
			
			// initialize thread that will take care of client
			ServerThread st = new ServerThread(clientComSocket, game, snake);
			
			// add serverThread as a game listener
			game.addListener(st);
			
			// launch thread
			new Thread(st).start();
			
			System.out.println("Thread started for client " + counter);
			
			// increment entity counter
			counter ++;
		}
	}

}
