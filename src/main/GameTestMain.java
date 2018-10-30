package main;

import entity.Snake;
import game.Game;
import gui.EntityDisplayer;
import gui.GameFrame;
import javafx.application.Application;
import javafx.scene.paint.Color;
import mouvement.Directions;
import mouvement.StraightMouvement;

public class GameTestMain {

	public static void main(String[] args) {
		
		Game game = new Game();
		
		StraightMouvement rm1 = new StraightMouvement(Directions.WEST);
		Snake snake1 = new Snake(rm1, GameFrame.GRID_SIZE/2-5, GameFrame.GRID_SIZE/2, Color.ALICEBLUE);
		//StraightMouvement rm2 = new StraightMouvement(Directions.WEST);
		//Snake snake2 = new Snake(rm2, GameFrame.GRID_SIZE/2 + 6, GameFrame.GRID_SIZE/2, Color.AQUAMARINE);
		
		game.addEntity(1, snake1);
		//game.addEntity(2, snake2);

		Application.launch(EntityDisplayer.class, args);
		
	}

}
