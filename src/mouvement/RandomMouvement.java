package mouvement;

import java.util.Random;

import entity.Cell;

public class RandomMouvement implements Mouvement{
	
	public static final String KEY = "RND";

	private static final int MAXSTEPS = 3;
	
	private int nSteps;
	private int step = 1;
	
	private Directions dir;
	private boolean dirChange = false;
	
	private Random random = new Random();
	
	public RandomMouvement(Directions dir) {
		this.nSteps = random.nextInt(MAXSTEPS)+1;
		this.dir = dir;
	}

	@Override
	public void computeNextDirection() {
		// Change direction every nSteps
		if(step%nSteps == 0) {
			step = 0;
			nSteps = random.nextInt(MAXSTEPS)+1;
			switch(random.nextInt(2)) {

			case(1):
				dir = dir.turnLeft();
			break;
			case(2):
				dir = dir.turnRight();
			}
			// there has been a change in direction
			dirChange = true;
		} else {
			dirChange = false;
		}
		
		// increment step
		step++;
	}
	
	@Override
	public Cell computeNextCell(Cell head) {
		
		// Compute nextCell
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
	public boolean directionHasChanged() {
		return dirChange;
	}
	
	@Override
	public void setDirection(Directions dir) {
		this.dir = dir;
	}
}
