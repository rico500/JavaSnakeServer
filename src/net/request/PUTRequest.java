package net.request;

import java.util.StringTokenizer;

import entity.Cell;
import entity.Entity;
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
 *  PUT EntityType ID ColorR ColorG ColorB ColorO [Opts] [CellLocation]
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
	
	Entity entityToPut;

	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public PUTRequest(Entity entity) {
		super(null);
		this.entityToPut = entity;
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
		String entityType = st.nextToken();	
		int entityID = Integer.parseInt(st.nextToken());
		Color color = new Color(Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()),
				Double.parseDouble(st.nextToken()));
		
		Entity tmpEntity = null;
		
		if(entityType.equals(Snake.KEY)) {
			Directions dir = Directions.getFromValue(Integer.parseInt(st.nextToken()));
			tmpEntity = new Snake(new StraightMouvement(dir), color);
			while(st.hasMoreTokens())
				tmpEntity.addCell(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		} else {
			throw new RuntimeException("In PUT request, unknown entityType " + entityType);
		}
		
		game.addEntity(entityID, tmpEntity);
	}

	@Override
	public String createRequest() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(KEY);
		sb.append(" ");
		sb.append(entityToPut.getKey());
		sb.append(" ");
		sb.append(entityToPut.getID());
		sb.append(" ");
		sb.append(entityToPut.getColor().getRed());
		sb.append(" ");
		sb.append(entityToPut.getColor().getGreen());
		sb.append(" ");
		sb.append(entityToPut.getColor().getBlue());
		sb.append(" ");
		sb.append(entityToPut.getColor().getOpacity());
		sb.append(" ");
		if(entityToPut.getClass() == Snake.class) {
			Snake snakeToPut = (Snake) entityToPut;
			sb.append(Integer.toString(snakeToPut.getMouvement().getDirection().getValue()));
			sb.append(" ");
		}
		
		for(Cell c : entityToPut.getCellList()) {
			sb.append(Integer.toString(c.getX()));
			sb.append(" ");
			sb.append(Integer.toString(c.getY()));
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	
}
