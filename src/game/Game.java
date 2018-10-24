package game;

import java.util.ArrayList;

import entity.Cell;
import entity.Entity;
import entity.Snake;

public class Game {
	
	/** define grid size*/
	public static final int GRID_SIZE = 24;
	
	/** Set entities in game */
	ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	public Game() {};
	
	public void addEntity(Entity e) {
		entityList.add(e);
	}
	
	public boolean removeEntity(Entity e) {
		return entityList.remove(e);
	}
	
	public ArrayList<Entity> getEntityList(){
		return entityList;
	} 
	
	private ArrayList<ContactPair> findContacts() {
		
		// Create array of Contact locations
		ArrayList<ContactPair> contactList = new ArrayList<ContactPair>();
		
		// Check each entity for contacts with other entity or frame bounds 
		for(Entity challenger : entityList) {
			
			// Check if the challenger is a playable entity (which can die on contact)
			if(challenger.isPlayable()) {
				
				// temporarily save head of entity
				Cell head = challenger.getCellList().get(0);
				
				// Check if the head is found at the same location as other entities or
				// is out of bounds 
				for(Entity defender : entityList) {

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

	public void handleContacts() {
		for (ContactPair contact : findContacts()) {
			
			// handle snake out of bounds 	
			if (contact.getChallenger().getClass() == Snake.class && 
					contact.getDefender() == null) {
				entityList.remove(contact.getChallenger());
				System.out.println("Contact between challenging snake " + contact.getChallenger()
				+ " and wall.");
				System.out.println("Snake " + contact.getChallenger() + " dies.");

			} else 
				
				// handle a snake contacting another snake
				if(contact.getChallenger().getClass() == Snake.class && 
				contact.getDefender().getClass() == Snake.class) {
					entityList.remove(contact.getChallenger());
					System.out.println("Contact between challenging snake " + contact.getChallenger()
					+ " and defending snake " + contact.getDefender());
					System.out.println("Snake " + contact.getChallenger() + " dies.");

				}
			else
				throw new RuntimeException("Unhandled contact event type.");
		}
	}
	
	private boolean isOutOfBounds(Cell head) {
		return (head.getX()>=GRID_SIZE || head.getY()>=GRID_SIZE || head.getX()<0 || head.getY()<0);
	}

}
