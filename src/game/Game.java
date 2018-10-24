package game;

import java.util.ArrayList;

import entity.Entity;

public class Game {
	
	ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	public Game() {};
	
	public void addEntity(Entity e) {
		entityList.add(e);
	}
	
	public boolean removeEntity(Entity e) {
		return entityList.remove(e);
	}
	
	public void checkStep() {
		
	}
	
	//private ArrayList<>

}
