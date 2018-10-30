package game;

import java.util.ArrayList;
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
	HashMap<Integer, Entity> entityMap = new HashMap<Integer, Entity>();
	
	/** List of listening instances implementing GameListener*/
	ArrayList<GameListener> listenerList = new ArrayList<GameListener>();
	
	/** Timer to schedule gameSteps */
	Timer timer;
	
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
	}
	
	public boolean removeEntity(int ID, Entity e) {
		return entityMap.remove(ID, e);
	}
	
	public boolean removeEntity(Entity e) {
		return entityMap.remove(e.getID(), e);
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
		for(Entity e : entityMap.values()) {
			e.evolve();
		}

		// Check and handle contacts.
		// Some entities may disappear at this point because of death due to a
		// collision.
		handleContacts();
		
		// call listeners
		notifyListeners();
		
		timer.schedule(new GameTimerTask(this), TIME_STEP);
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
				
				// temporarily save head of entity
				Cell head = challenger.getCellList().get(0);
				
				// Check if the head is found at the same location as other entities or
				// is out of bounds 
				for(Entity defender : entityMap.values()) {

					// if it is found at the location of another entity...
					if(!defender.equals(challenger) && defender.getCellList().contains(head)) {

						// ... save a reference to the entities and their contact 
						// location
						contactList.add(new ContactPair(challenger, defender, head));
					} else

						// if the head is out of bounds save a refernce to it and the 
						// location of trespassing
						if(isOutOfBounds(head)) {
							contactList.add(new ContactPair(challenger, null, head));
						}
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
				System.out.println("Contact between challenging snake " + contact.getChallenger()
				+ " and wall.");
				System.out.println("Snake " + contact.getChallenger() + " dies.");

			} else 
				
				// handle a snake contacting another snake
				if(contact.getChallenger().getClass() == Snake.class && 
				contact.getDefender().getClass() == Snake.class) {
					// The out-of-bounds snake dies and is removed from the list
					removeEntity(contact.getChallenger());
					System.out.println("Contact between challenging snake " + contact.getChallenger()
					+ " and defending snake " + contact.getDefender());
					System.out.println("Snake " + contact.getChallenger() + " dies.");

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
