package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import entity.Snake;
import game.Game;
import gui.GameFrame;
import javafx.scene.paint.Color;
import mouvement.Directions;
import mouvement.StraightMouvement;
import net.request.PUTRequest;

public class Server {
	
	public static void main(String[] args) throws IOException {
		
		// Create game constants
		final Color[] COLOR_ARRAY = {Color.ALICEBLUE, Color.GOLD, Color.LAVENDER, Color.MEDIUMVIOLETRED};
		final Directions[] dirArray = {Directions.EAST, Directions.WEST, Directions.NORTH, Directions.SOUTH};
		final int[] xArray = {3, GameFrame.GRID_SIZE-3, GameFrame.GRID_SIZE/2, GameFrame.GRID_SIZE/2};
		final int[] yArray = {GameFrame.GRID_SIZE/2, GameFrame.GRID_SIZE/2, 0, GameFrame.GRID_SIZE};
		
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
		
		// Create game
		Game game = new Game();
		
		// read number of desired players
		final int playerN = Integer.parseInt(args[2]);
		if(playerN > 4 || playerN < 2) {
			throw new IllegalArgumentException("Allowed number of players between 2 and 4, you required "+ playerN);
		}
		
		for(int counter = 1; counter < playerN+1; counter++) {
			// assign new snake to client
			// TODO Put new snake in a position with no other snakes
			// TODO Assign logical initial direction
			Snake snake = new Snake(new StraightMouvement(dirArray[counter-1]), xArray[counter-1], yArray[counter-1], COLOR_ARRAY[(counter-1)%5]);
		
			// add snake to game simulation
			// TODO add unique ID finder
			game.addSnake(counter, snake);
		}
		
		// Create Client Array
		ArrayList<ServerThread> clientList = new ArrayList<ServerThread>();
		
		// Accept clients as they come
		for(int counter = 1; counter < playerN+1; counter++) {
			// wait for client to connect
			Socket clientComSocket = acceptSocket.accept();
			
			System.out.println("New client accepted. ID: " + counter);
			
			// initialize thread that will take care of client
			ServerThread st = new ServerThread(clientComSocket, game, (Snake) game.getSnake(counter));
			clientList.add(st);
			
			// launch thread
			new Thread(st).start();
			
			// push client's entity to client first
			st.pushSnake(game.getSnake(counter));
			
			System.out.println("Thread started for client " + counter);
			
			// add serverThread as a game listener
			game.addListener(st);
		}
		
		// push all other participating entities to game
		for(ServerThread cl : clientList) {
			for(Snake e : game.getSnakeMap().values()) {
				if(cl.getID() != e.getID()) {
					cl.pushSnake(e);
				}
			}
		}
		
		game.startGame();
		
	}

	
}
