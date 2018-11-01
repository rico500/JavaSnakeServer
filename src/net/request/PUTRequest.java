package net.request;

import java.util.StringTokenizer;

import entity.Cell;
import entity.Snake;
import game.Game;
import mouvement.Directions;
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
	
	Snake snakeToPut;

	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public PUTRequest(Snake snake) {
		super(null);
		this.snakeToPut = snake;
	}
	
	public PUTRequest(Game game) {
		super(game);
	}

	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	@Override
	public void handleRequest(StringTokenizer st) {
		int snakeID = Integer.parseInt(st.nextToken());
		Color color = new Color(Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()));

		Snake tmpSnake = null;

		Directions dir = Directions.getFromValue(Integer.parseInt(st.nextToken()));
		tmpSnake = new Snake(new StraightMouvement(dir), color);
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
