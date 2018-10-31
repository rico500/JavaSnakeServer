package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import entity.Cell;
import entity.Entity;
import game.Game;
import gui.DisplayableCell;
import gui.PeriodicGameFrame;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import net.client.ClientThread;
import net.request.EXITRequest;

public class ClientPUTTest extends PeriodicGameFrame{
	
	private static Game game ;
	
	private static Socket comSocket;
	
	private static PrintWriter writer;
	
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
		// Nothing here for this test
	}

	public static void main(String[] args) throws UnknownHostException, IOException {

		int port = Integer.parseInt(args[0]);
		String machine = args[1];

		comSocket = new Socket(machine, port);
		
		writer = new PrintWriter(comSocket.getOutputStream());
		
		game = new Game();
		game.startGame();
		
		ClientThread clientThread = new ClientThread(comSocket, game);
		new Thread(clientThread).start();
		
		Application.launch(args);
	}
}
