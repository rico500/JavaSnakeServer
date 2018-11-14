package net.request;

import java.io.PrintWriter;
import java.util.StringTokenizer;

import entity.Snake;
import game.Game;
import mouvement.Directions;

/**
 * 
 * Modify a snake's direction of evolution in the game.
 * 
 * The syntax is :
 * 
 * SET ID DIRECTION
 * 
 * @author ebrunner
 *
 */
public class SETRequest extends Request {

	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	public static final String KEY = "SET";
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	private Snake snakeToSet;
	private Directions dir;
	private Game game;
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Use this constructor to handle a SET request
	 * 
	 * @param game - reference to the game where the snake is to be modified.
	 */
	public SETRequest(Game game) {
		super(null);
		this.game = game;
	}
	
	/**
	 * 
	 * Use this constructor to create a SET request message and send it.
	 * 
	 * @param writer - writer object which will send the message.
	 * @param game - reference to the game where the snake is located.
	 * @param snakeToSet - reference to the snake which must be set.
	 * @param dir - direction which the snake wants to take.
	 */
	public SETRequest(PrintWriter writer, Game game, Snake snakeToSet, Directions dir) {
		super(writer);
		this.game = game;
		this.snakeToSet = snakeToSet;
		this.dir = dir;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	@Override
	public void handleRequest(StringTokenizer st) {

		// parse request
		int snakeID = Integer.parseInt(st.nextToken());
		Directions dir = Directions.getFromValue(Integer.parseInt(st.nextToken()));
		
		// Set new state
		game.getSnake(snakeID).getMouvement().setDirection(dir);
		game.addModifiedSnake(game.getSnake(snakeID));
		System.out.println("Snake " + snakeID + " goes towards "+ dir.name());
	}

	@Override
	public String createRequest() {
		
		StringBuilder sb = new StringBuilder();

		sb.append(KEY);
		sb.append(" ");
		sb.append(snakeToSet.getID());
		sb.append(" ");
		sb.append(dir.getValue());
		
		return sb.toString();
	}
	
}
