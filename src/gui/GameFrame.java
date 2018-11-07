package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import entity.Cell;
import entity.Snake;
import game.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mouvement.Directions;
import mouvement.Mouvement;
import mouvement.StraightMouvement;
import net.client.ClientThread;
import net.request.EXITRequest;
import net.request.SETRequest;

public abstract class GameFrame extends Application {

	// ***************************************************************************** //
	// *                                                                           * //
	// *                      EDIT CONSTANTS AS YOU LIKE                           * //
	// *                                                                           * //
	// ***************************************************************************** //

	/** Graphical window title */
	static public String TITLE = "Game";

	/** Background Color */
	static public final Color BG_COLOR = Color.WHITE;

	/** The size of all the elements building up the grid */
	static public final double ELEMENT_SIZE = 32;
	
	/** The number of lines/columns in the grid */
	static public final int GRID_SIZE = 24;

	// ***************************************************************************** //
	// *                                                                           * //
	// *                   DO NOT EDIT THE FOLLOWING ATTRIBUTES                    * //
	// *                                                                           * //
	// ***************************************************************************** //

	/** Graphical window size (square) */
	static public final double WINDOW_SIZE = GRID_SIZE*ELEMENT_SIZE;
	
	/** Root of the Java FX scene graph containing all the elements to display */
	protected Group root;
	
	/** The timer for scheduling next game step */
	private Timer timer;
	
	/** Instance of the game */
	private Game game ;

	/** Communication socket with server */
	private Socket comSocket;

	/** Character stream to send requests to server */
	private PrintWriter writer;
	
	/** Thread to handle server requests */
	private ClientThread clientThread;
	
	/** Flag indicating if the client is in bot or manual mode */
	private boolean isBot = true;
	
	/** Flag indicating  if the client is in Tick phase */
	private boolean isTick = false;
	
	/** Temporary direction variable to store any snake direction update sent during the 
	 * tick phase */
	private Directions pendingDir = null;

	// ***************************************************************************** //
	// *                                                                           * //
	// *                         DO NOT EDIT PAST HERE                             * //
	// *                                                                           * //
	// ***************************************************************************** //
	
	/** 
	 * Initialize the graphical display.
	 *
	 * In a Java FX application, this is a mandatory replacement of the constructor.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	@Override
	public void start(Stage primaryStage) throws UnknownHostException, IOException {
		
		// Parse arguments
		List<String> args = getParameters().getRaw();
		int port = Integer.parseInt(args.get(0));
		String machine = args.get(1);
		String mouvementType = args.get(2);
		if(mouvementType.equals(StraightMouvement.KEY)) {isBot = false;}
		
		// connect to server
		comSocket = new Socket(machine, port);
		
		// initialize character writer
		writer = new PrintWriter(comSocket.getOutputStream());
		
		// start game
		game = new Game();
		
		// start thread to handle server requests
		clientThread = new ClientThread(comSocket, game, mouvementType);
		clientThread.setTickCallbackClass(new TickEventHandler(new TickEventTask()));
		clientThread.setTockCallbackClass(new TockEventHandler(new TockEventTask()));
		new Thread(clientThread).start();
		
		// Create the root of the graph scene, and all its children 
		root = new Group();

		// create the scene graph
		Scene scene = new Scene(root, WINDOW_SIZE, WINDOW_SIZE, BG_COLOR);
		
		// add a KeyEvent listener to the scene
		if(!isBot) {
			scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					keyEventHandler(event);
				} 
			});
		}
		// terminate the Application when the window is closed
		primaryStage.setOnCloseRequest(event -> { exitEventHandler(); Platform.exit(); System.exit(0); } );

		// Create the timer
		timer = new Timer();
		
		// Show a graphical window with all the graph scene content
		primaryStage.setScene(scene);
		primaryStage.setTitle(TITLE);
		primaryStage.setResizable(false);
		primaryStage.show();	
	}	
	
	
	public void exitEventHandler() {
		try {
			writer.println(new EXITRequest().createRequest());
			writer.flush();
			comSocket.close();
		} catch (IOException e) {
			System.err.println("IOException occured while closing socket.");
		}
		Platform.exit();
	}

	public void keyEventHandler(KeyEvent event) {
		
		// Check if client has already been given a snake from the server
		Snake snakeToUpdate = clientThread.getSnake();
		if(snakeToUpdate != null) {
			
			// Get current snake's direction
			Directions CurrentDir = snakeToUpdate.getMouvement().getDirection();
			
			// The direction implied from the Keyboard is stored temporarily for evaluation
			Directions DesiredDir = CurrentDir;
			
			// A flag is set to mark if the entered key is known or not
			boolean isKnown = false;
			
			// For any arrow key, set the desired direction of travel
			switch(event.getCode()) {
			
			case LEFT:  DesiredDir = Directions.WEST; isKnown = true; break;
			case UP:    DesiredDir = Directions.SOUTH; isKnown = true; break;
			case RIGHT: DesiredDir = Directions.EAST; isKnown = true; break;
			case DOWN:	DesiredDir = Directions.NORTH; isKnown = true; break;
			default : // ignore any other key
				
			}
			
			// If the desired direction of travel is aligned with the current one, set flag
			// to true. (aligned directions are NORTH & SOUTH and EAST & WEST)
			boolean isAligned = ((CurrentDir.getValue() - DesiredDir.getValue())%2 == 0);
			
			// If all conditions are fulfilled, communicate new direction to server
			if(isKnown == true  && !isAligned) {
				if(!isTick) {
					writer.println(new SETRequest(game, snakeToUpdate, DesiredDir).createRequest());
					writer.flush();
				} else {
					pendingDir = DesiredDir;
				}
			} 
		}
	}
	
	private class TickEventHandler implements Runnable {
		
		Runnable eventTask;
		
		public TickEventHandler(Runnable eventTask) {
			this.eventTask = eventTask;
		}
		
		@Override
		public void run() {
			Platform.runLater(eventTask);
		}
	}
	
	private class TickEventTask implements Runnable {
		/**
		 * 
		 * The runnable method is called whenever the server sends a TICK request
		 * indicating that the client has all the information to compute the next step of
		 * the game. All clients must complete the game step before starting to gather
		 * information for the next one.
		 * 
		 */
		@Override
		public void run() {
			
			// client enters tick phase
			isTick = true;
			
			// update entity locations based on indications from server
			game.update();

			// display entities
			root.getChildren().setAll(computeNodeList());

			// Clear all modification lists
			game.clearAllModLists();

		}
	}
	
	private class TockEventHandler implements Runnable {
		
		Runnable eventTask;
		
		public TockEventHandler(Runnable eventTask) {
			this.eventTask = eventTask;
		}
		
		@Override
		public void run() {
			Platform.runLater(eventTask);
		}
	}
	
	private class TockEventTask implements Runnable {
		/**
		 * 
		 * The runnable method is called whenever the server sends a TOCK request
		 * indicating that all clients have finished updating their game with information
		 * from the server. The client may now send back information concerning the next game
		 * step.
		 * 
		 */
		@Override
		public void run() {
			
			// If the client is a bot, then now is the time to send information on 
			// the bot's next action.
			if(isBot) {
				Mouvement nextMouv = clientThread.getSnake().getMouvement();
				nextMouv.computeNextDirection();
				if(nextMouv.directionHasChanged()) {
					writer.println(new SETRequest(game, clientThread.getSnake(), nextMouv.getDirection()).createRequest());
					writer.flush();
				}
				
				// if the client isn't a bot, send any pending direction updates that may
				// have been entered by the user
			} else {
				if(pendingDir != null) {
					writer.println(new SETRequest(game, clientThread.getSnake(), pendingDir).createRequest());
					writer.flush();
					pendingDir = null;
				}
			}
			
			// client exits tick phase by completing tock logic
			isTick = false;
		}
	}
	
	public Collection<Node> computeNodeList() {
		// take care of rearranging data on current state of game to display it
		ArrayList<Node> nodeList = new ArrayList<Node>();
		for(Snake e : game.getSnakeMap().values()) {
			
			Iterator<Cell> cellIterator = e.getCellList().iterator();
			Cell nextCell = cellIterator.next();
			if(e.isAlive()) {
				nodeList.add(new DisplayableSnakeHead(nextCell.getX(), nextCell.getY(), e.getMouvement().getDirection(),e.getColor()));
			}else {
				nodeList.add(new DisplayableSnakeHead(nextCell.getX(), nextCell.getY(), e.getMouvement().getDirection(),Color.DARKGRAY));
			}
			while(cellIterator.hasNext()) {
				nextCell = cellIterator.next();	
				if(e.isAlive()) {
					nodeList.add(new DisplayableCell(nextCell.getX(), nextCell.getY(),e.getColor()));
				}else {
					nodeList.add(new DisplayableCell(nextCell.getX(), nextCell.getY(), Color.DARKGRAY));
				}

			}
		}
		return nodeList;
	}
	
}
