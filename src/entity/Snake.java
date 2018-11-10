package entity;

import java.util.ArrayList;

import javafx.scene.paint.Color;

import mouvement.Mouvement;

/**
 * 
 * The Snake class stores the representation of a snake made of a list of Cells.
 * Methods are included to compute the next state of the snake based on its heading.
 * Various movement types can be attached to the instance when it is constructed.
 * 
 * @author ebrunner
 *
 */
public class Snake {
	
	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	/** indicates number of steps before the snake grows by one cell */
	private static final int GROW_RATE = 2;
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	/** step counter, to know when to grow */
	private int step = -1;
	
	/** unique ID to identify identity between server and client */
	private int ID;
	
	/** list of cells belonging to this snake */
	private ArrayList<Cell> snake = new ArrayList<Cell>();
	
	/** Reference to mouvement object containing logic to compute
	 * the snake's next step
	 */
	private Mouvement mvmt;
	
	/** Snake's color */
	private final Color color;
	
	/** Flag indicating if the snake is alive or not. If it is dead it will stay static*/
	private boolean isAlive = true;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * the Snake constructor creates a snake with one cell at the position (x0, y0) 
	 * in the grid.
	 * 
	 * @param mvmtType - refernce to a mouvement object
	 * @param x0 - initial position along x axis
	 * @param y0 - initial position along y axis
	 * @param color - the snake's color 
	 */
	public Snake(Mouvement mvmtType, int x0, int y0, Color color) {
		this.mvmt = mvmtType;
		this.color = color;
		snake.add(new Cell(x0, y0));
	}
	
	/**
	 * 
	 * Used for incremental instance creation (e.g. in PUTRequest)
	 * 
	 * @param color - the snake's color
	 */
	public Snake(Color color) {
		this.color = color;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC GET/SET METHODS
	 * 
	 ************************************************************************/
	
	public Mouvement getMouvement() {
		return mvmt;
	}
	
	public void setMouvement(Mouvement mvmt) {
		this.mvmt =  mvmt;
	}
	
	public ArrayList<Cell> getCellList(){
		return snake;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setID(int ID) {
		this.ID = ID; 
	}
	
	public int getID() {
		if(ID == -1)
			throw new NullPointerException("Snake ID equals -1. It hasn't been set yet.");
		return ID;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	
	/**
	 * 
	 * Set snake's isAlive flag to false. 
	 * Other parts of the program will know that this snake is dead in the game.
	 *
	 */
	public void dies() {
		isAlive = false;
	}
	
	/**
	 * 
	 * Check if the current snake is alive.
	 * 
	 * @return true if the snake is alive, or else false is returned.
	 */
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * 
	 * Adds a cell to the current snake.
	 * 
	 * @param x - cell's x coordinate on the grid
	 * @param y - cell's y coordinate on the grid
	 */
	public void addCell(int x, int y) {
		snake.add(new Cell(x, y));
	}
	
	/**
	 * 
	 * Update the snake's cell list based on the growth-rate and its movement type. 
	 * 
	 */
	public void evolve() {
		if(isAlive) {
			int startIndex;
			if(step%GROW_RATE == 0) {
				step = 1;
				snake.add(snake.get(snake.size()-1).clone());
				startIndex = (snake.size()-2);
			} else {
				startIndex = (snake.size()-1);
			}

			//mvmt.computeNextDirection();
			Cell nextCell = mvmt.computeNextCell(snake.get(0));

			for (int i = startIndex; i>0; i--) {
				snake.get(i).set(snake.get(i-1));
			}
			snake.get(0).set(nextCell);

			step++;
		}
	}
	
	/**
	 * 
	 * returns a string representation of the snake.
	 * 
	 */
	public String toString() {
		return "Snake " + ID + " ";
	}
	
	/**
	 * 
	 * returns a more detailed string representation of the snake, with its list of cells.
	 * 
	 * @return - string representation of the snake's cell list and current heading.
	 */
	public String toStringLong() {
		StringBuilder sb = new StringBuilder();
		sb.append("Snake: ");
		for(Cell c : snake)
			sb.append(c + ", ");
		sb.append("going towards: " + mvmt.getDirection().name());
		return sb.toString();
	}
	
}
