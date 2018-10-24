package gui;

import java.util.ArrayList;
import java.util.Collection;

import entity.Cell;
import entity.Entity;
import game.Game;
import javafx.scene.Node;

public class EntityDisplayer extends GameFrame{

	protected Game game ;
	
	public EntityDisplayer(Game game) {
		this.game = game;
	}
	
	@Override
	public Collection<Node> gameStep() {
		
		// update all entities in game
		for(Entity e : game.getEntityList()) {
			e.evolve();
		}
		
		// check and handle contacts
		game.handleContacts();
		
		// take care of rearranging data on current state of game to display it
		ArrayList<Node> nodeList = new ArrayList<Node>();
		for(Entity e : game.getEntityList()) {
			for(Cell c : e.getCellList())
			nodeList.add(new DisplayableCell(c.getX(), c.getY(), ELEMENT_SIZE ,e.getColor()));
		}
		return nodeList;
	}

}
