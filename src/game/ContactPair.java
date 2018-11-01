package game;

import entity.Snake;
import entity.Cell;

public class ContactPair {

	private Snake challenger;
	private Snake defender;
	private Cell contactLocation;
	
	public ContactPair(Snake challenger, Snake defender, Cell contactLocation) {
		this.challenger = challenger;
		this.defender = defender;
		this.contactLocation = contactLocation;
	}

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
