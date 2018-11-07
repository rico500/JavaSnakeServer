package mouvement;
import entity.Cell;
import entity.Snake;
import game.Game;

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
	
	public void computeNextDirection();
	public Cell computeNextCell(Cell cell);
	public Directions getDirection();
	public void setDirection(Directions dir);
	public boolean directionHasChanged();
	
}
