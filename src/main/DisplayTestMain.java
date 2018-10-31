package main;

import entity.Snake;
import game.Game;
import gui.EntityDisplayer;
import gui.PeriodicGameFrame;
import javafx.scene.paint.Color;
import mouvement.Directions;
import mouvement.RandomMouvement;
import mouvement.StraightMouvement;

public class DisplayTestMain extends EntityDisplayer {

	
	public DisplayTestMain() {	
	}
	
	public static void main(String[] args) {
		game = new Game();
		
		StraightMouvement rm1 = new StraightMouvement(Directions.EAST);
		Snake snake1 = new Snake(rm1, PeriodicGameFrame.GRID_SIZE/2-5, PeriodicGameFrame.GRID_SIZE/2, Color.ALICEBLUE);
		StraightMouvement rm2 = new StraightMouvement(Directions.WEST);
		Snake snake2 = new Snake(rm2, PeriodicGameFrame.GRID_SIZE/2 + 6, PeriodicGameFrame.GRID_SIZE/2, Color.AQUAMARINE);
		
		game.addEntity(1, snake1);
		game.addEntity(2, snake2);
		
		game.startGame();
		launch(args);
	}
	
}
