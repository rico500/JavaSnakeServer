package net.request;

import java.util.StringTokenizer;

import entity.Snake;
import game.Game;
import mouvement.Directions;
import mouvement.Mouvement;
import mouvement.StraightMouvement;

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
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public SETRequest(Game game) {
		super(game);
	}
	
	public SETRequest(Game game, Snake snakeToSet, Directions dir) {
		super(game);
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
