package mouvement;
import entity.Cell;

public interface Mouvement {

	public Cell computeNextCell(Cell cell);
	public Directions getDirection();
	
}
