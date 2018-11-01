package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import entity.Cell;
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
	private HashMap<Integer, Snake> snakeMap = new HashMap<Integer, Snake>();

	/** List of listening instances implementing GameListener*/
	private ArrayList<GameListener> listenerList = new ArrayList<GameListener>();

	/** Timer to schedule gameSteps */
	private Timer timer;

	/** Lists of modified/added/deleted entities */
	private ArrayList<Snake> addedSnake = new ArrayList<Snake>();
	private ArrayList<Snake> removedSnake = new ArrayList<Snake>();
	private ArrayList<Snake> modifiedSnake = new ArrayList<Snake>();

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

	public void addSnake(int ID, Snake e) {
		e.setID(ID);
		snakeMap.put(ID, e);
		addedSnake.add(e);
	}

	public void removeSnake(int ID) {
		Snake tmpSnake = snakeMap.remove(ID);
		if(tmpSnake == null) {
			System.err.println("Warning: Snake with ID " + ID + " is not part of the game but was requested to be removed.");
		} else
			removedSnake.add(tmpSnake);
	}

	public void removeSnake(Snake e) {
		removeSnake(e.getID());
	}

	public Snake getFirstSnake() {
		Object[] snakeArray =  snakeMap.values().toArray();
		return ((Snake) snakeArray[0]);
	}

	public Snake getSnake(int ID) {
		return snakeMap.get(ID);
	}

	public Snake getSnake(Snake e) {
		return snakeMap.get(e.getID());
	}

	public HashMap<Integer, Snake> getSnakeMap(){
		return snakeMap;
	} 

	public void addListener(GameListener listener) {
		listenerList.add(listener);
	}

	public void removeListener(GameListener listener) {
		listenerList.remove(listener);
	}

	public void addModifiedSnake(Snake e){
		if(!modifiedSnake.contains(e))
			modifiedSnake.add(e);
	}

	public ArrayList<Snake> getAddedSnake(){
		return addedSnake;
	}

	public ArrayList<Snake> getRemovedSnake(){
		return removedSnake;
	}

	public ArrayList<Snake> getModifiedSnake(){
		return modifiedSnake;
	}

	public void clearAllModLists() {
		addedSnake.clear();
		removedSnake.clear();
		modifiedSnake.clear();
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
		 * it computes collisions and defines possible death of a snake. It also
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
		for(Snake e : snakeMap.values()) {
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
	 * 			to the challenging and defending snake. In case of a collision with a 
	 * 			wall, the defending snake has a null value.
	 * 
	 */

	private ArrayList<ContactPair> findContacts() {

		// Create array of Contact locations
		ArrayList<ContactPair> contactList = new ArrayList<ContactPair>();

		// Check each snake for contacts with other snake or frame bounds 
		for(Snake challenger : snakeMap.values()) {

			// temporarily save head of challenging snake
			Cell challengerHead = challenger.getCellList().get(0);

			// Check if the head is found at the same location as other entities or
			// is out of bounds 
			for(Snake defender : snakeMap.values()) {

				// if it is found at the location of another snake...
				if(defender.getCellList().contains(challengerHead)) {

					// Check that defender is a different snake from the attacker
					if(!defender.equals(challenger)) {

						// save the colliding entities and their location
						contactList.add(new ContactPair(challenger, defender, challengerHead));

					} 

					// If it is infact the same snake... 
					else {

						// check if the collision location is not the head of the same snake
						if(Collections.frequency(defender.getCellList(), challengerHead)>1) {

							// save the collision location
							contactList.add(new ContactPair(challenger, defender, challengerHead));

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
				removeSnake(contact.getChallenger());
				System.out.println("Contact between challenging " + contact.getChallenger()
				+ "and wall.");
				System.out.println(contact.getChallenger() + " dies.");

			} else 

				// handle a snake contacting another snake
				if(contact.getChallenger().getClass() == Snake.class && 
				contact.getDefender().getClass() == Snake.class) {
					// The out-of-bounds snake dies and is removed from the list
					removeSnake(contact.getChallenger());
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
	 * Defines and checks for out-of-bounds state of a snake
	 * 
	 * @param Cell The first cell of the snake, e.g. the snake's head
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
