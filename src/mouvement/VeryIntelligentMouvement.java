package mouvement;

import java.util.Random;

import entity.Cell;
import entity.Snake;
import game.Game;

public class VeryIntelligentMouvement implements Mouvement{
	
	public static final String KEY = "VIT";

	private Game game;
	private Directions dir;
	private Snake snake;
	private boolean dirChange = false;
	private Random random = new Random();
	
	public VeryIntelligentMouvement(Directions dir, Game game, Snake snake) {
		this.dir = dir;
		this.game = game;
		this.snake = snake;
	}
	
	@Override
	public void computeNextDirection() {
		
		// temporarily store reference to snake's head
		// distances to obstacles are evaluated from the head
		Cell head = snake.getCellList().get(0);
		
		// Compute direction of next obstacle in every direction
		int straightDistance = computeDistanceToObstacle(head, dir);
		int rightDistance = computeDistanceToObstacle(head, dir.turnRight());
		int leftDistance = computeDistanceToObstacle(head, dir.turnLeft());
		
		// Check for any probable collisions at the next step with a snake's head
		for(Snake s : game.getSnakeMap().values()) {
			if(!s.equals(snake)) {
				// check for collision straight ahead
				if(s.getMouvement().computeNextCell(s.getCellList().get(0)).equals(
						snake.getMouvement().computeNextCell(head))) {
					straightDistance = 0;
				}
			}
		}
		
		// Check for proximity with another snake's step
		for(Snake s : game.getSnakeMap().values()) {
			if(!s.equals(snake)) {
				Cell distanceVector = head.getVectorTo(s.getCellList().get(0));
				int distX = distanceVector.getX();
				int distY = distanceVector.getY();
				
				// check for head diagonally located straight ahead to the right or left
				switch(dir) {
				
				case NORTH:
					if(distY == 1) {
						if(distX == 1) {
							leftDistance = 0;
						} else if(distX == -1) {
							rightDistance = 0;
						}
					}
					break;
				case EAST:
					if(distX == 1) {
						if(distY == 1) {
							rightDistance = 0;
						} else if(distY == -1) {
							leftDistance = 0;
						}
					}
					break;
				case SOUTH:
					if(distY == -1) {
						if(distX == -1) {
							leftDistance = 0;
						} else if(distX == 1) {
							rightDistance = 0;
						}
					}
					break;
				case WEST:
					if(distX == -1) {
						if(distY == -1) {
							rightDistance = 0;
						} else if(distY == 1) {
							leftDistance = 0;
						}
					}
					break;
				
				}
			}
		}
		
		System.out.println("Computed Distances, str: " + straightDistance + ", rgt: " + rightDistance + ", lft: " + leftDistance);
		
		//find out which direction has the obstacle furthest away and go in that direction
		int tmpDistance = straightDistance;
		Directions nextDir = dir;
		if(tmpDistance < rightDistance) {
			nextDir = dir.turnRight(); 
			tmpDistance = rightDistance;
		}
		if(tmpDistance < leftDistance) {
			nextDir = dir.turnLeft();
		}
		
		// Check for directions with same obstacle distance
		int sameDistanceCounter = 1;
		Directions[] dirArray = new Directions[3];
		dirArray[0] = nextDir;
		if (tmpDistance == straightDistance && nextDir != dir) {
			dirArray[sameDistanceCounter] = dir;
			sameDistanceCounter++;
		}
		if (tmpDistance == rightDistance && nextDir != dir.turnRight()) {
			dirArray[sameDistanceCounter] = dir.turnRight();
			sameDistanceCounter++;
		}
		if (tmpDistance == leftDistance && nextDir != dir.turnLeft()) {
			dirArray[sameDistanceCounter] = dir.turnLeft();
			sameDistanceCounter++;
		}
		
		// If there are multiple directions which are equally viable , randomly select
		// one of them
		if(sameDistanceCounter > 1) {
			System.out.println("Randomly selected direction.");
			nextDir = dirArray[random.nextInt(sameDistanceCounter)];
		}
			
		// check if the direction has changed
		if(nextDir == dir) {
			dirChange = false;
		} else {
			dirChange = true;
			// apply that direction to the snake
			dir = nextDir;
		}
		
	}
	
	private int computeDistanceToObstacle(Cell origin, Directions testDir) {
		
		// Compute direction of next obstacle in every direction
		int obstacleDistance = 0;
		int tmpDistance = 0;

		Cell head = snake.getCellList().get(0);

		switch(testDir) {

		case NORTH:
			obstacleDistance = Game.GRID_SIZE - head.getY();
			for(Snake s : game.getSnakeMap().values()) {
				for(Cell c : s.getCellList()) {
					if(!c.equals(head)) {
						if (head.getVectorTo(c).getX() == 0) {
							tmpDistance = c.getY()-head.getY();
							if(tmpDistance < obstacleDistance && tmpDistance >0) {
								obstacleDistance = tmpDistance;
							}
						}
					}
				}
			}
			break;
		
		case EAST:
			obstacleDistance = Game.GRID_SIZE - head.getX();
			for(Snake s : game.getSnakeMap().values()) {
				for(Cell c : s.getCellList()) {
					if(!c.equals(head)) {
						if (head.getVectorTo(c).getY() == 0) {
							tmpDistance = c.getX()-head.getX();
							if(tmpDistance < obstacleDistance && tmpDistance >0) {
								obstacleDistance = tmpDistance;
							}
						}
					}
				}
			}
			break;
		
		case SOUTH:
			obstacleDistance = head.getY();
			for(Snake s : game.getSnakeMap().values()) {
				for(Cell c : s.getCellList()) {
					if(!c.equals(head)) {
						if (head.getVectorTo(c).getX() == 0) {
							tmpDistance = head.getY()-c.getY();
							if(tmpDistance < obstacleDistance && tmpDistance >0) {
								obstacleDistance = tmpDistance;
							}
						}
					}
				}
			}
			break;
		
		case WEST :
			obstacleDistance = head.getX();
			for(Snake s : game.getSnakeMap().values()) {
				for(Cell c : s.getCellList()) {
					if(!c.equals(head)) {
						if (head.getVectorTo(c).getY() == 0) {
							tmpDistance = head.getX()-c.getX();
							if(tmpDistance < obstacleDistance && tmpDistance >0) {
								obstacleDistance = tmpDistance;
							}
						}
					}
				}
			}
			break;
		}
		return obstacleDistance;
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
