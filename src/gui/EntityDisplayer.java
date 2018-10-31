package gui;

import java.util.ArrayList;
import java.util.Collection;

import entity.Cell;
import entity.Entity;
import game.Game;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

public class EntityDisplayer extends PeriodicGameFrame{

	protected static Game game ;
	
	@Override
	public Collection<Node> gameStep() {
		// take care of rearranging data on current state of game to display it
		ArrayList<Node> nodeList = new ArrayList<Node>();
		for(Entity e : game.getEntityMap().values()) {
			for(Cell c : e.getCellList())
			nodeList.add(new DisplayableCell(c.getX(), c.getY(), ELEMENT_SIZE ,e.getColor()));
		}
		return nodeList;
	}

	@Override
	public void exitEventHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyEventHandler(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

}
