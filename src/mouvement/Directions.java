package mouvement;

public enum Directions {
	NORTH(0), EAST(1), SOUTH(2), WEST(3);
	
	private int value;
	
	private Directions(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public Directions turnRight() {
		return getFromValue((value + 1)%4);
	}
	
	public Directions turnLeft() {
		return getFromValue((4 + value - 1)%4);
	}
	
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
		}
		return dir;
	}
	
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
