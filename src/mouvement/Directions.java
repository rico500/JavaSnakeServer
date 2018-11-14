package mouvement;

/**
 * 
 * Enumeration of directions. The four cardinal directions are defined and have 
 * the following properties :
 * 
 * NAME		ORIENTATION		VALUE
 * NORTH	dx=0; dy=1 		0
 * EAST 	dx=1; dy=0 		1
 * SOUTH 	dx=0; dy=-1 	2
 * WEST 	dx=-1; dy=0		3
 * 
 * @author ebrunner
 *
 */
public enum Directions {
	
	/** Defined directions */
	NORTH(0), EAST(1), SOUTH(2), WEST(3);
	
	/** current value of the enum, corresponding to the current direction */
	private int value;
	
	/** constructor which assigns a value to the enum */
	private Directions(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return value of the enum
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 
	 * Change enum to the right of its current direction.
	 * 
	 * @return direction after the turn.
	 */
	public Directions turnRight() {
		return getFromValue((value + 1)%4);
	}
	
	/**
	 * 
	 * Change enum to the left of its current direction.
	 * 
	 * @return direction after the turn.
	 */
	public Directions turnLeft() {
		return getFromValue((4 + value - 1)%4);
	}
	
	/**
	 * 
	 * Get direction corresponding to the given value
	 * 
	 * @param val - integer corresponding to a direction
	 * @return Direction corresponding to the given value
	 * @throws IllegalArgumentException if val isn't contained between 0 and 3 inclusive.
	 */
	public static Directions getFromValue(int val) {
		Directions dir = null;
		switch(val) {
		case(0): 
			dir =  NORTH;
		break;
		case(1): 
			dir =  EAST;
		break;
		case(2): 
			dir =  SOUTH;
		break;
		case(3): 
			dir =  WEST;
		break;
		default:
			throw new IllegalArgumentException("value is out of bounds. It must be contained between 0 and 3.");
		}
		return dir;
	}
	
	/**
	 * 
	 * Convert direction to its value
	 *  
	 * @return value of the direction.
	 */
	public int toValue() {
		switch(this) {
		case NORTH :
			return 0;
		case EAST :
			return 1;
		case SOUTH :
			return 2;
		case WEST :
			return 3;
		default :
			throw new RuntimeException("Unknown direction.");
		}
	}
}
