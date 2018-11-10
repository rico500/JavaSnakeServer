package net.request;

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
	
	Snake snakeToPut;
	String mouvementType;

	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public PUTRequest(Snake snake) {
		super(null);
		this.snakeToPut = snake;
		this.mouvementType = StraightMouvement.KEY;
	}
	
	public PUTRequest(Game game) {
		super(game);
		this.mouvementType = StraightMouvement.KEY;
	}
	
	public PUTRequest(Game game, String mouvementType) {
		super(game);
		this.mouvementType = mouvementType;
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
