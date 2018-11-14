package net.request;

import java.io.PrintWriter;
import java.util.StringTokenizer;

import entity.Cell;
import entity.Snake;
import game.Game;
import mouvement.Directions;
import mouvement.Mouvement;
import mouvement.StraightMouvement;

import javafx.scene.paint.Color;


/**
 * 
 *  A PUT request allows to add a whole entity to the game, e.g. a snake 
 *  is added to the game when a client joins or a randomly placed entity such
 *  as an apple is added.
 *  
 *  The syntax is :
 *  
 *  PUT ID ColorR ColorG ColorB ColorO [Opts] [CellLocation]
 * 
 * @author ebrunner
 *
 */
public class PUTRequest extends Request{
	
	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	public static final String KEY = "PUT";
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	private Snake snakeToPut;
	private String mouvementType;
	private Game game;

	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Use this constructor to handle a PUT Request. 
	 * The new snake will have a straight movement type by default.
	 * 
	 * @param game - reference to the game where the new snake is to be put.
	 */
	public PUTRequest(Game game) {
		super(null);
		this.game = game;
		this.mouvementType = StraightMouvement.KEY;
	}
	
	/**
	 * 
	 * Use this constructor to handle a PUT Request. 
	 * The new snake will have a movement type defined by the constructor.
	 * 
	 * @param game - reference to the game where the new snake is to be put.
	 * @param mouvementType - Key corresponding to the desired movement type for the snake.
	 */
	public PUTRequest(Game game, String mouvementType) {
		super(null);
		this.game = game;
		this.mouvementType = mouvementType;
	}
	
	/**
	 * 
	 * Use this constructor to create and send a PUT request message.
	 * 
	 * @param writer - writer object which will send the message.
	 * @param snake - reference to snake to put in remote game.
	 */
	public PUTRequest(PrintWriter writer, Snake snake) {
		super(writer);
		this.snakeToPut = snake;
		this.mouvementType = StraightMouvement.KEY;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	@Override
	public void handleRequest(StringTokenizer st) {
		
		// Parse request
		int snakeID = Integer.parseInt(st.nextToken());
		Color color = new Color(Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()));
		Directions dir = Directions.getFromValue(Integer.parseInt(st.nextToken()));
		
		// Construct snake entity
		Snake tmpSnake = new Snake(color);
		tmpSnake.setMouvement(Mouvement.getMouvementFromKey(mouvementType, dir, game, tmpSnake));
		while(st.hasMoreTokens())
			tmpSnake.addCell(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));

		game.addSnake(snakeID, tmpSnake);
		
	}
	
	@Override
	public String createRequest() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(KEY);
		sb.append(" ");
		sb.append(snakeToPut.getID());
		sb.append(" ");
		sb.append(snakeToPut.getColor().getRed());
		sb.append(" ");
		sb.append(snakeToPut.getColor().getGreen());
		sb.append(" ");
		sb.append(snakeToPut.getColor().getBlue());
		sb.append(" ");
		sb.append(snakeToPut.getColor().getOpacity());
		sb.append(" ");
		sb.append(Integer.toString(snakeToPut.getMouvement().getDirection().getValue()));
		sb.append(" ");
		
		for(Cell c : snakeToPut.getCellList()) {
			sb.append(Integer.toString(c.getX()));
			sb.append(" ");
			sb.append(Integer.toString(c.getY()));
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	
}
