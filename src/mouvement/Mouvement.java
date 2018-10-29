package mouvement;
import entity.Cell;
import javafx.scene.Node;

public interface Mouvement {

	public Cell computeNextCell(Cell cell);
	public Directions getDirection();
	
}
