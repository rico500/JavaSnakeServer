package mouvement;

import entity.Cell;
import gui.GameFrame;

public class SwissCrossMouvement implements Mouvement{
	
	public static final String KEY = "SCM";
	
	private static final Directions[] dirArray = {
			Directions.EAST, Directions.NORTH, Directions.EAST, 
			Directions.SOUTH, Directions.EAST, Directions.SOUTH, Directions.WEST, 
			Directions.SOUTH, Directions.WEST, Directions.NORTH, Directions.WEST, 
			Directions.NORTH};
	
	private int step = 0;
	
	private Directions currentDir;
	
	private int branchSize;
	
	private boolean dirChange = false;
	
	public SwissCrossMouvement(Directions dir) {
		this.branchSize = (GameFrame.GRID_SIZE-6)/3;
		if(dir.compareTo(Directions.EAST) != 0) {
			throw new IllegalArgumentException("Snake must start evolving "
					+ "towards east to draw the cross in the right direction.");
		}
		this.currentDir = dir;
	}

	@Override
	public void computeNextDirection() {
		
		if(step%branchSize == 0) {
			currentDir = dirArray[step/branchSize];
			dirChange = true;
		} else {
			dirChange = false;
		}	
		
		step ++;
		
	}

	@Override
	public Cell computeNextCell(Cell head) {
		// Compute nextCell
		Cell nextCell = null;
		switch(currentDir) {

		case NORTH:
			nextCell = new Cell(head.getX(), head.getY()+1);
			break;
		case EAST:
			nextCell = new Cell(head.getX()+1, head.getY());
			break;
		case SOUTH:
			nextCell = new Cell(head.getX(), head.getY()-1);
			break;
		case WEST :
			nextCell = new Cell(head.getX()-1, head.getY());
			break;
		}

		return nextCell;
	}

	@Override
	public Directions getDirection() {
		return currentDir;
	}

	@Override
	public void setDirection(Directions dir) {
		
	}

	@Override
	public boolean directionHasChanged() {
		return dirChange;
	}

	@Override
	public void cancelDirChange() {
		dirChange = false;
	}
	
}
