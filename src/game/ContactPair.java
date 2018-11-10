package game;

import entity.Snake;
import entity.Cell;

/**
 * 
 * Stores a pair of snakes that have collided. If the snake collides with a wall,
 * the defending entity will have a null value.
 * 
 * @author ebrunner
 *
 */
public class ContactPair {
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	/** Challenging snake */
	private Snake challenger;
	
	/** Defending snake or wall */
	private Snake defender;
	
	/** Location of collision in grid */
	private Cell contactLocation;
	
	/************************************************************************
	 * 
	 * CONSTRUCTORS
	 * 
	 ************************************************************************/	
	
	/**
	 * 
	 * Create a new contact pair. Store the challenging and defending snake as a pair
	 * along with the location of their collision. If the snake hits a wall, the defending
	 * snake is conventionally set to one.
	 * 
	 * @param challenger - Snake that has initiated the collision
	 * @param defender - Snake which is victim of the collision
	 * @param contactLocation - Cell where the contact happened
	 * 
	 */
	public ContactPair(Snake challenger, Snake defender, Cell contactLocation) {
		this.challenger = challenger;
		this.defender = defender;
		this.contactLocation = contactLocation;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC GET/SET METHODS
	 * 
	 ************************************************************************/	

	public Snake getChallenger() {
		return challenger;
	}

	public Snake getDefender() {
		return defender;
	}

	public Cell getContactLocation() {
		return contactLocation;
	}
	
}
