package gui;

import java.util.ArrayList;
import java.util.Collection;

import entity.Cell;
import entity.Entity;
import javafx.application.Application;
import javafx.scene.Node;

public class EntityDisplayer extends GameFrame{

	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	public void addEntity(Entity e) {
		entityList.add(e);
	}
	
	public boolean removeEntity(Entity e) {
		return entityList.remove(e);
	}
	
	@Override
	public Collection<Node> gameStep() {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		for(Entity e : entityList) {
			e.evolve();
			for(Cell c : e.getCellList())
			nodeList.add(new DisplayableCell(c.getX(), c.getY(), ELEMENT_SIZE ,e.getColor()));
		}
		return nodeList;
	}

}
