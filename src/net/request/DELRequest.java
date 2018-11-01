package net.request;

import java.util.StringTokenizer;

import entity.Entity;
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
	
	private Entity entityToDel;
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public DELRequest(Game game) {
		super(game);
	}
	
	public DELRequest(Entity entityToDel) {
		super(null);
		this.entityToDel = entityToDel;
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
		
		game.removeEntity(entityID);
		
	}

	@Override
	public String createRequest() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(KEY);
		sb.append(" ");
		sb.append(entityToDel.getKey());
		sb.append(" ");
		sb.append(entityToDel.getID());
		
		return sb.toString();
		
	}
	
}
