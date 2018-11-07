package mouvement;

import java.util.Random;

import entity.Cell;
import entity.Snake;
import game.Game;

public class RandomIntelligentMouvement implements Mouvement {
	
	public static final String KEY = "RDI";

	private Game game;
	private Directions dir;
	private Snake snake;
	private boolean dirChange = false;
	private Random random = new Random();
	
	public RandomIntelligentMouvement(Directions dir, Game game, Snake snake) {
		this.dir = dir;
		this.game = game;
		this.snake = snake;
	}
	
	@Override
	public void computeNextDirection() {
		
		// Compute next location of the snake's head
		Cell nextHead = computeNextCell(snake.getCellList().get(0));
		
		// If that location is occupied by another snake or is out of bounds, try
		// another direction
		if(game.cellIsOccupied(nextHead) || game.isOutOfBounds(nextHead) 
				|| game.cellIsReserved(nextHead, snake)) {
			
			System.out.println("Trying to avoid obstacle");
			
			// try going randomly left or right
			switch(random.nextInt(2)) {

			case(0):
				dir = dir.turnLeft();
				System.out.println("Turning left, dir = " + dir.name());
			break;
			
			case(1):
				dir = dir.turnRight();
				System.out.println("Turning RIGHT, dir = " + dir.name());
			break;
			}
			
			// Update the next head's position
			nextHead = computeNextCell(snake.getCellList().get(0));
			
			// if the new randomly selected direction is also occupied, try the opposite direction
			if(game.cellIsOccupied(nextHead) || game.isOutOfBounds(nextHead) 
					|| game.cellIsReserved(nextHead, snake)) {
				
				System.out.println("Switching to second option");
				
				dir = dir.turnLeft().turnLeft();
			}
			
			dirChange = true;
		} else {
			dirChange = false;
		}

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
