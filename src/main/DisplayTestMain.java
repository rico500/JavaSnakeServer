package main;

import entity.Snake;
import gui.EntityDisplayer;
import gui.GameFrame;
import javafx.scene.paint.Color;
import mouvement.Directions;
import mouvement.RandomMouvement;
import mouvement.StraightMouvement;

public class DisplayTestMain extends EntityDisplayer {

	
	public DisplayTestMain() {
		RandomMouvement rm1 = new RandomMouvement();
		Snake snake1 = new Snake(rm1, GameFrame.GRID_SIZE/2, GameFrame.GRID_SIZE/2, Color.ALICEBLUE);
		StraightMouvement rm2 = new StraightMouvement(Directions.SOUTH);
		Snake snake2 = new Snake(rm2, GameFrame.GRID_SIZE/2 + 6, GameFrame.GRID_SIZE/2 + 6, Color.AQUAMARINE);
		
		addEntity(snake1);
		addEntity(snake2);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
