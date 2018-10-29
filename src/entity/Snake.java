package entity;

import java.util.ArrayList;

import javafx.scene.paint.Color;

import mouvement.Mouvement;

public class Snake implements Entity{
	
	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	/** indicates number of steps before the snake grows by one cell */
	private static final int GROW_RATE = 4;
	
	/** key string */
	public static final String KEY = "SNAKE";
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	/** step counter, to know when to grow */
	private int step = 1;

	
	private ArrayList<Cell> snake = new ArrayList<Cell>();
	private final Mouvement mvmt;
	private final Color color;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	public Snake(Mouvement mvmtType, Color color) {
		this.mvmt = mvmtType;
		this.color = color;
	}
	
	public Snake(Mouvement mvmtType, int x0, int y0, Color color) {
		this.mvmt = mvmtType;
		this.color = color;
		snake.add(new Cell(x0, y0));
	}
	
	/************************************************************************
	 * 
	 * CLASS METHODS
	 * 
	 ************************************************************************/
	
	public Mouvement getMouvement() {
		return mvmt;
	}
	
	@Override
	public ArrayList<Cell> getCellList(){
		return snake;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public String getKey() {
		return KEY;
	}
	
	@Override
	public void addCell(int x, int y) {
		snake.add(new Cell(x, y));
	}
	
	@Override
	public void evolve() {
		int startIndex;
		if(step%GROW_RATE == 0) {
			step = 1;
			snake.add(snake.get(snake.size()-1).clone());
			startIndex = (snake.size()-2);
		} else {
			startIndex = (snake.size()-1);
		}
			
		Cell nextCell = mvmt.computeNextCell(snake.get(0));
		for (int i = startIndex; i>0; i--) {
			snake.get(i).set(snake.get(i-1));
		}
		snake.get(0).set(nextCell);
		
		step++;
	}
	
	@Override
	public boolean isPlayable() {
		return true;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Snake: ");
		for(Cell c : snake)
			sb.append(c + ", ");
		sb.append("going towards: " + mvmt.getDirection().name());
		return sb.toString();
	}
	
}
