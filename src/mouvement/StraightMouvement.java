package mouvement;

import entity.Cell;

/**
 * 
 * This movement will go in the direction which it is set to until its direction is 
 * modified by some exterior process.
 * 
 * @author ebrunner
 *
 */
public class StraightMouvement implements Mouvement {
	
	public static final String KEY = "STR";
	
	private Directions dir;

	public StraightMouvement(Directions dir) {
		this.dir = dir;
	}
	
	@Override
	public void computeNextDirection() {
		
	}
	
	@Override
	public boolean directionHasChanged() {
		return false;
	}
	
	@Override
	public Cell computeNextCell(Cell head) {
		
		Cell nextCell = null;
		switch(dir) {

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
		return dir;
	}
	
	@Override
	public void setDirection(Directions dir) {
		this.dir = dir;
	}
	
	@Override
	public void cancelDirChange() {
	}

}
