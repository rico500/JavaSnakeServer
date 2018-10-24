package main;

import entity.Snake;
import game.Game;
import gui.EntityDisplayer;
import gui.GameFrame;
import javafx.scene.paint.Color;
import mouvement.Directions;
import mouvement.RandomMouvement;
import mouvement.StraightMouvement;

public class DisplayTestMain extends EntityDisplayer {

	
	public DisplayTestMain() {
		super(new Game());
		
		StraightMouvement rm1 = new StraightMouvement(Directions.WEST);
		Snake snake1 = new Snake(rm1, GameFrame.GRID_SIZE/2-5, GameFrame.GRID_SIZE/2, Color.ALICEBLUE);
		//StraightMouvement rm2 = new StraightMouvement(Directions.WEST);
		//Snake snake2 = new Snake(rm2, GameFrame.GRID_SIZE/2 + 6, GameFrame.GRID_SIZE/2, Color.AQUAMARINE);
		
		game.addEntity(snake1);
		//game.addEntity(snake2);
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
