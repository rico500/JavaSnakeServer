package mouvement;
import entity.Cell;
import entity.Snake;
import game.Game;

/**
 * 
 * A mouvement computes the snake's heading based on logic defined in each movement's 
 * implementation.
 * 
 * @author ebrunner
 *
 */
public interface Mouvement {
	
	/**
	 * 
	 * Constructs the appropriate mouvement object based on the given key and passes 
	 * an initial direction argument if needed
	 * 
	 * @param key - mouvement key, unique for each type of mouvement
	 * @param dir - initial direction to take
	 * @return an object implementiing Mouvement
	 */
	public static Mouvement getMouvementFromKey(String key, Directions dir, Game game, Snake snake) {
		
		switch(key) {
		
		case(StraightMouvement.KEY):
			return new StraightMouvement(dir);
		case(RandomMouvement.KEY):
			return new RandomMouvement(dir);
		case(RandomIntelligentMouvement.KEY):
			return new RandomIntelligentMouvement(dir, game, snake);
		case(VeryIntelligentMouvement.KEY):
			return new VeryIntelligentMouvement(dir, game, snake);
		default:
			throw new IllegalArgumentException("Unknown mouvement key " + key + " .");
		
		}

	}
	
	/**
	 * 
	 * Computes the next direction taken by the snake.
	 * 
	 */
	public void computeNextDirection();
	
	/**
	 * 
	 * Computes the next Cell that the snake will occupy based on the snake's 
	 * current direction.
	 * 
	 * @param cell - cell from which the next occupied cell is computed (generally snake's head)
	 * @return the next cell occupied by the snake
	 * 
	 */
	public Cell computeNextCell(Cell cell);
	
	/**
	 * 
	 * Get the snake's current direction.
	 * 
	 * @return current snake's direction
	 * 
	 */
	public Directions getDirection();
	
	/**
	 * 
	 * Set the snake's direction.
	 * 
	 */
	public void setDirection(Directions dir);
	
	/**
	 * 
	 * Check if the snake's direction has changed during the current step.
	 * 
	 * @return true if the direction has changed, false otherwise.
	 * 
	 */
	public boolean directionHasChanged();
	
}
