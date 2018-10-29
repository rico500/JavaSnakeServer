package mouvement;

import java.util.Random;

import entity.Cell;

public class RandomMouvement implements Mouvement{

	private static final int MAXSTEPS = 3;
	private static final String MV_ID = "RAND";
	
	private int nSteps;
	private int step = 1;
	
	private Directions dir;
	
	private Random random = new Random();
	
	public RandomMouvement() {
		this.nSteps = random.nextInt(MAXSTEPS)+1;
		dir = Directions.getFromValue(random.nextInt(3));
	}
	
	@Override
	public Cell computeNextCell(Cell head) {
		
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
		}
		
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
		
		// reset head and increment step
		step++;
		
		return nextCell;
	}

	@Override
	public Directions getDirection() {
		return dir;
	}
	
}
