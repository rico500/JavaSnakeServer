package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import entity.Cell;
import entity.Entity;
import entity.Snake;
import game.Game;
import gui.DisplayableCell;
import gui.PeriodicGameFrame;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import mouvement.Directions;
import net.client.ClientThread;
import net.request.EXITRequest;
import net.request.SETRequest;

public class ClientSETTest extends PeriodicGameFrame {
	
	private static Game game ;

	private static Socket comSocket;

	private static PrintWriter writer;
	
	private static ClientThread clientThread;

	@Override
	public Collection<Node> gameStep() {
		// take care of rearranging data on current state of game to display it
		ArrayList<Node> nodeList = new ArrayList<Node>();
		for(Entity e : game.getEntityMap().values()) {
			for(Cell c : e.getCellList())
				nodeList.add(new DisplayableCell(c.getX(), c.getY(), ELEMENT_SIZE ,e.getColor()));
		}
		return nodeList;
	}

	@Override
	public void exitEventHandler() {
		try {
			writer.println(new EXITRequest().createRequest());
			writer.flush();
			comSocket.close();
		} catch (IOException e) {
			System.err.println("IOException occured while closing socket.");
		}
	}

	@Override
	public void keyEventHandler(KeyEvent event) {
		
		// Check if client has already been given a snake from the server
		Snake snakeToUpdate = clientThread.getSnake();
		if(snakeToUpdate != null) {
			
			Directions dir = snakeToUpdate.getMouvement().getDirection();
			
			boolean isKnown = false;
			
			switch(event.getCode()) {
			
			case LEFT:  dir = Directions.WEST; isKnown = true; break;
			case UP:    dir = Directions.SOUTH; isKnown = true; break;
			case RIGHT: dir = Directions.EAST; isKnown = true; break;
			case DOWN:	dir = Directions.NORTH; isKnown = true; break;
			default : // ignore any other key
				
			}
			
			if(isKnown == true) {
				writer.println(new SETRequest(game, snakeToUpdate, dir).createRequest());
				writer.flush();
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException {

		int port = Integer.parseInt(args[0]);
		String machine = args[1];

		comSocket = new Socket(machine, port);

		writer = new PrintWriter(comSocket.getOutputStream());

		game = new Game();
		game.startGame();

		clientThread = new ClientThread(comSocket, game);
		new Thread(clientThread).start();

		Application.launch(args);
	}
}
