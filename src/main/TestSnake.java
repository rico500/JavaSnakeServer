package main;

import entity.Snake;
import javafx.scene.paint.Color;
import mouvement.Mouvement;
import mouvement.RandomMouvement;

public class TestSnake {

	public static void main(String[] args) throws InterruptedException {
		
		Mouvement mvmt = new RandomMouvement();
		
		Snake snake = new Snake(mvmt, 10, 10, Color.ALICEBLUE);
		
		while(true) {
			System.out.println(snake);
			snake.evolve();
			Thread.sleep(1000);
		}
		
	}
	
}
