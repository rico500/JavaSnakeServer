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
 * DEL EntityType ID
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
		String entityType = st.nextToken();	
		int entityID = Integer.parseInt(st.nextToken());
		
		game.removeSnake(entityID);
		
	}

	@Override
	public String createRequest() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(KEY);
		sb.append(" ");
		sb.append(snakeToDel.getKey());
		sb.append(" ");
		sb.append(snakeToDel.getID());
		
		return sb.toString();
		
	}
	
}
