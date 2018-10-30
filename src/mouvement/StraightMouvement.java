package mouvement;

import entity.Cell;

public class StraightMouvement implements Mouvement {
	
	private Directions dir;

	public StraightMouvement(Directions dir) {
		this.dir = dir;
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
	
	public void setDirection(Directions dir) {
		this.dir = dir;
	}

}
