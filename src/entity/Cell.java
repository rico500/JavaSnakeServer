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
	
	public boolean isEqual(Cell c) {
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
