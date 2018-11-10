package entity;

/**
 * 
 * A Cell is the basic constitutive element of a snake. It mainly stores a pair 
 * of x and y coordinates and provides basic operations on them. 
 * 
 * @author ebrunner
 *
 */
public class Cell {
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/

	/** Grid positions, x increases in the northern direction while y increases in the
	 * eastern direction. */
	private int x, y;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Creates a cell with position (x, y) in the grid.
	 * 
	 * @param x - cell's x coordinate on the grid
	 * @param y - cell's y coordinate on the grid
	 */
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC GET/SET METHODS
	 * 
	 ************************************************************************/
	
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
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Compares two cells together. It follows the logic of 
	 * o instanceof Cell ? return (x == c.getX() && y == c.getY()) || (o == this) : return false 
	 * 
	 * @param o - object to be compared to this cell
	 * @return 	true if the object is a cell with the same x and y coordinates 
	 * 			or is the same instance of Cell
	 * 
	 */
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
	
	/**
	 * 
	 * Computes the vector between this cell and cell c.
	 * 
	 * @param c - cell to which the vector from this cell must be computed.
	 * @return vector between this cell and cell c.
	 * 
	 */
	public Cell getVectorTo(Cell c) {
		return new Cell(c.getX()-x, c.getY()-y);
	}
	
	/**
	 * 
	 * Make a new Cell instance with the same coordinates as this cell.
	 * 
	 * @return cloned cell.
	 */
	public Cell clone() {
		return new Cell(x, y);
	}
	
	/**
	 * 
	 * Return string representation of the cell's coordinate pair.
	 * 
	 * @return string representation of this cell.
	 * 
	 */
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

}
