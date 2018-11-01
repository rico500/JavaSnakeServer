package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import entity.Cell;
import entity.Entity;
import entity.Snake;

public class Game implements Runnable{
	
	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	/** Game grid size*/
	public static final int GRID_SIZE = 24;
	
	/** Game speed */
	public static final long TIME_STEP = 1000L;
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	/** Set entities in game */
	private HashMap<Integer, Entity> entityMap = new HashMap<Integer, Entity>();
	
	/** List of listening instances implementing GameListener*/
	private ArrayList<GameListener> listenerList = new ArrayList<GameListener>();
	
	/** Timer to schedule gameSteps */
	private Timer timer;
	
	/** Lists of modified/added/deleted entities */
	private ArrayList<Entity> addedEntity = new ArrayList<Entity>();
	private ArrayList<Entity> removedEntity = new ArrayList<Entity>();
	private ArrayList<Entity> modifiedEntity = new ArrayList<Entity>();
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/
	
	public Game() {};
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	public void addEntity(int ID, Entity e) {
		e.setID(ID);
		entityMap.put(ID, e);
		addedEntity.add(e);
	}
	
	public void removeEntity(int ID) {
		 Entity tmpEntity = entityMap.remove(ID);
		 if(tmpEntity == null) {
			 System.err.println("Warning: Entity with ID " + ID + " is not part of the game but was requested to be removed.");
		 } else
			 removedEntity.add(tmpEntity);
	}

	public void removeEntity(Entity e) {
		removeEntity(e.getID());
	}
	
	public Entity getFirstEntity() {
		Object[] entityArray =  entityMap.values().toArray();
		return ((Entity) entityArray[0]);
	}
	
	public Entity getEntity(int ID) {
		return entityMap.get(ID);
	}
	
	public Entity getEntity(Entity e) {
		return entityMap.get(e.getID());
	}
	
	public HashMap<Integer, Entity> getEntityMap(){
		return entityMap;
	} 
	
	public void addListener(GameListener listener) {
		listenerList.add(listener);
	}
	
	public void removeListener(GameListener listener) {
		listenerList.remove(listener);
	}
	
	public void addModifiedEntity(Entity e){
		if(!modifiedEntity.contains(e))
			modifiedEntity.add(e);
	}
	
	public ArrayList<Entity> getAddedEntity(){
		return addedEntity;
	}
	
	public ArrayList<Entity> getRemovedEntity(){
		return removedEntity;
	}
	
	public ArrayList<Entity> getModifiedEntity(){
		return modifiedEntity;
	}
	
	public void clearAllModLists() {
		addedEntity.clear();
		removedEntity.clear();
		modifiedEntity.clear();
	}
	
	/************************************************************************
	 * 
	 * GAME STEP TIMING
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Starts the timer process which executes game steps at regular intervals.
	 *
	 */
	public void startGame() {
		// Create the timer
		timer = new Timer();
		
		// Schedule first game step
		timer.schedule(new GameTimerTask(this), TIME_STEP);
	}
	
	/**
	 * 
	 * Task class. Calls the runnable method at regular time intervals
	 *
	 */
	private class GameTimerTask extends TimerTask {

		Game game;
		
		public GameTimerTask(Game game) {this.game = game;}
		
		/**
		 * A game step has all its entities evolve based on their current state,
		 * it computes collisions and defines possible death of an entity. It also
		 * calls on the gui methods to display the state of the game if required.
		 */
		@Override
		public void run() {	
			Thread gameThread = new Thread(game);
			gameThread.start();
		}

	}
	
	/**
	 * 
	 * Runnable method executed at a regular interval defined by TIME_STEP.
	 * All game logic and event notification starts from here.
	 * 
	 */
	@Override
	public void run() {
		// update all entities in game
		update();

		// Check and handle contacts.
		// Some entities may disappear at this point because of death due to a
		// collision.
		handleContacts();
		
		// call listeners
		notifyListeners();
		
		// clear modification lists
		clearAllModLists();
		
		timer.schedule(new GameTimerTask(this), TIME_STEP);
	}
	
	/**
	 * 
	 * Have all entities in game evolve. The method is runnable from instances of game
	 * which are not periodically updated.
	 * 
	 */
	public void update() {
		for(Entity e : entityMap.values()) {
			e.evolve();
		}
	}
	
	/************************************************************************
	 * 
	 * PRIVATE METHODS (GAME LOGIC)
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Find and return collision points between entities and entities with walls. 
	 * 
	 * @return ArrayList\<ContactPair\> list of collision points along with a reference
	 * 			to the challenging and defending entity. In case of a collision with a 
	 * 			wall, the defending entity has a null value.
	 * 
	 */
	
	private ArrayList<ContactPair> findContacts() {
		
		// Create array of Contact locations
		ArrayList<ContactPair> contactList = new ArrayList<ContactPair>();
		
		// Check each entity for contacts with other entity or frame bounds 
		for(Entity challenger : entityMap.values()) {
			
			// Check if the challenger is a playable entity (which can die on contact)
			if(challenger.isPlayable()) {
				
				// temporarily save head of challenging entity
				Cell challengerHead = challenger.getCellList().get(0);
				
				// Check if the head is found at the same location as other entities or
				// is out of bounds 
				for(Entity defender : entityMap.values()) {
					
					// if it is found at the location of another entity...
					if(defender.getCellList().contains(challengerHead)) {

						// Check that defender is a different entity from the attacker
						if(!defender.equals(challenger)) {
							
							// save the colliding entities and their location
							contactList.add(new ContactPair(challenger, defender, challengerHead));
							
						} 
						
						// If it is infact the same entity... 
						else {
							
							// check if the collision location is not the head of the same entity
							if(Collections.frequency(defender.getCellList(), challengerHead)>1) {
								
								// save the collision location
								contactList.add(new ContactPair(challenger, defender, challengerHead));
							
							}
						}
					}
				}
				
				// if the head is out of bounds save a reference to it and the 
				// location of trespassing
				if(isOutOfBounds(challengerHead)) {
					contactList.add(new ContactPair(challenger, null, challengerHead));
				}
				
			}
		}
		
		return contactList;
	}

	
	/**
	 * 
	 * Handle collision cases. The currently handled cases are those of a Snake hitting
	 * another Snake and a Snake hitting a wall.
	 * 
	 */
	public void handleContacts() {
		for (ContactPair contact : findContacts()) {
			
			// handle snake out of bounds 
			// !!! Must be implemented first to avoid null pointer exceptions due to the wall !!!
			if (contact.getChallenger().getClass() == Snake.class && 
					contact.getDefender() == null) {
				// The challenging snake dies and is removed from the list
				removeEntity(contact.getChallenger());
				System.out.println("Contact between challenging " + contact.getChallenger()
				+ "and wall.");
				System.out.println(contact.getChallenger() + " dies.");

			} else 
				
				// handle a snake contacting another snake
				if(contact.getChallenger().getClass() == Snake.class && 
				contact.getDefender().getClass() == Snake.class) {
					// The out-of-bounds snake dies and is removed from the list
					removeEntity(contact.getChallenger());
					System.out.println("Contact between challenging " + contact.getChallenger()
					+ "and defending " + contact.getDefender());
					System.out.println(contact.getChallenger() + " dies.");

				}
			else
				// in case the collision between entities is not handled, an exception
				// is thrown to remind the developer to implement the collision.
				throw new RuntimeException("Unhandled contact event type.");
		}
	}
	
	
	/**
	 * 
	 * Defines and checks for out-of-bounds state of an entity
	 * 
	 * @param Cell The first cell of the entity, e.g. the snake's head
	 * @return boolean Returns true if the cell is out of bounds
	 * 
	 */
	private boolean isOutOfBounds(Cell head) {
		return (head.getX()>=GRID_SIZE || head.getY()>=GRID_SIZE || head.getX()<0 || head.getY()<0);
	}

	/************************************************************************
	 * 
	 * PRIVATE METHODS (EVENT LOGIC)
	 * 
	 ************************************************************************/
	
	private void notifyListeners() {
		for(GameListener gl : listenerList) {
			gl.gameStepJob(this);
		}
	}
}
