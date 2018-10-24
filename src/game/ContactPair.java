package game;

import entity.Cell;
import entity.Entity;

public class ContactPair {

	private Entity challenger;
	private Entity defender;
	private Cell contactLocation;
	
	public ContactPair(Entity challenger, Entity defender, Cell contactLocation) {
		this.challenger = challenger;
		this.defender = defender;
		this.contactLocation = contactLocation;
	}

	public Entity getChallenger() {
		return challenger;
	}

	public Entity getDefender() {
		return defender;
	}

	public Cell getContactLocation() {
		return contactLocation;
	}
	
}
