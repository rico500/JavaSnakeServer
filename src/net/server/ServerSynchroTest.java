package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import entity.Snake;
import game.Game;
import gui.EventDrivenGameFrame;
import javafx.scene.paint.Color;
import mouvement.Directions;
import mouvement.StraightMouvement;

public class ServerSynchroTest {

	private static Game game;
	
	private static final Color[] COLOR_ARRAY = {Color.ALICEBLUE, Color.GOLD, Color.MEDIUMSEAGREEN, Color.ORCHID, Color.CHOCOLATE};
	
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
		
		Directions[] dirArray = {Directions.EAST, Directions.WEST};
		int[] xArray = {0, EventDrivenGameFrame.GRID_SIZE};
		int[] yArray = {EventDrivenGameFrame.GRID_SIZE/2, EventDrivenGameFrame.GRID_SIZE/2};
		
		// Accept clients as they come
		for(int counter = 1; counter < 3; counter++) {
			// wait for client to connect
			Socket clientComSocket = acceptSocket.accept();
			
			System.out.println("New client accepted. ID: " + counter);
			
			// assign new snake to client
			// TODO Put new snake in a position with no other snakes
			// TODO Assign logical initial direction
			Snake snake = new Snake(new StraightMouvement(dirArray[counter-1]), xArray[counter-1], yArray[counter-1], COLOR_ARRAY[(counter-1)%5]);
			
			// add snake to game simulation
			// TODO add unique ID finder
			game.addEntity(counter, snake);
			
			// initialize thread that will take care of client
			ServerSynchroThread st = new ServerSynchroThread(clientComSocket, game, snake);
			
			// launch thread
			new Thread(st).start();
			
			System.out.println("Thread started for client " + counter);
			
			// add serverThread as a game listener
			game.addListener(st);
		}
		
		game.startGame();
		
	}

	
}
