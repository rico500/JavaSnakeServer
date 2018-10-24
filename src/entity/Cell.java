package entity;

public class Cell {

	private int x, y;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void set(Cell c) {
		this.x = c.getX();
		this.y = c.getY();
	}
	
	@Override
	public boolean equals(Object o) {
		
		// If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        // Check if o is an instance of Cell or not 
        // "null instanceof [type]" also returns false
        if (!(o instanceof Cell)) { 
            return false; 
        } 
        
        // typecast o to Cell so that we can compare data members  
        Cell c = (Cell) o; 
        
        // Execute comparison
		if (x == c.getX() && y == c.getY()) 
			return true;
		else
			return false;
	}
	
	public Cell clone() {
		return new Cell(x, y);
	}
	
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

}
