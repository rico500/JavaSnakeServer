package net.request;

import java.util.StringTokenizer;

import entity.Snake;
import game.Game;
import mouvement.Directions;


/**
 * 
 * Request to delete entity in game.
 * 
 * The syntax is :
 * 
 * DEL ID
 * 
 * @author ebrunner
 *
 */
public class DELRequest extends Request{

	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	public static final String KEY = "DEL";
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	private Snake snakeToDel;
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public DELRequest(Game game) {
		super(game);
	}
	
	public DELRequest(Snake snakeToDel) {
		super(null);
		this.snakeToDel = snakeToDel;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	@Override
	public void handleRequest(StringTokenizer st) {
		
		// Parse request
		int entityID = Integer.parseInt(st.nextToken());
		
		game.getSnake(entityID).dies();
		
	}

	@Override
	public String createRequest() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(KEY);
		sb.append(" ");
		sb.append(snakeToDel.getID());
		
		return sb.toString();
		
	}
	
}
