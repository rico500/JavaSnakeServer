package net.request;

import java.io.PrintWriter;
import java.util.StringTokenizer;

import entity.Snake;
import game.Game;


/**
 * 
 * Request to delete snake in game.
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
	private Game game;
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Use this constructor to handle a DEL request
	 * 
	 * @param game - reference to the game where the snake is to be deleted.
	 */
	public DELRequest(Game game) {
		super(null);
		this.game = game;
	}
	
	/**
	 * 
	 * Use this constructor to create a DEL request message and send it.
	 * 
	 * @param writer - writer object which will send the message.
	 * @param snakeToDel - reference to the snake which must be deleted.
	 */
	public DELRequest(PrintWriter writer, Snake snakeToDel) {
		super(writer);
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
