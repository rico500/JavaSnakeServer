package net.request;

import java.util.StringTokenizer;

import entity.Entity;
import entity.Snake;
import game.Game;
import mouvement.Directions;
import mouvement.Mouvement;
import mouvement.StraightMouvement;

/**
 * 
 * Modify an entitie's direction of evolution in the game.
 * 
 * The syntax is :
 * 
 * SET EntityType ID STATE
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
	
	private Entity entityToSet;
	private Directions dir;
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public SETRequest(Game game) {
		super(game);
	}
	
	public SETRequest(Game game, Entity entityToSet, Directions dir) {
		super(game);
		this.entityToSet = entityToSet;
		this.dir = dir;
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
		
		// if request is for a snake...
		if(entityType.equals(Snake.KEY)) {
			Directions dir = Directions.getFromValue(Integer.parseInt(st.nextToken()));
			// Set new state
			Snake snakeToSet  = (Snake) game.getEntity(entityID);
			Mouvement mouvToSet = snakeToSet.getMouvement();
			if(mouvToSet.getClass() == StraightMouvement.class) {
				StraightMouvement strMouvToSet = (StraightMouvement) mouvToSet;
				strMouvToSet.setDirection(dir);
			} else {
				throw new RuntimeException("In SET request for entity of type Snake, "
						+ "only straight mouvement types may be modified.");
			}
			
		// if request is for an unknown or untreated type...	
		} else {
			throw new RuntimeException("In SET request, unknown entityType " + entityType);
		}
		
	}

	@Override
	public String createRequest() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(KEY);
		sb.append(" ");
		sb.append(entityToSet.getKey());
		sb.append(" ");
		sb.append(entityToSet.getID());
		sb.append(" ");
		sb.append(dir.getValue());
		
		return sb.toString();
	}
	
}
