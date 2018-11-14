package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 * 
 * A displayable node in the javaFX frame. Styling of the squares constituting a snake 
 * is defined here. 
 * 
 * @author ebrunner
 *
 */
public class DisplayableCell extends Rectangle {
	
	/**
	 * 
	 * Displayable cell constructor. Takes as arguments the coordinates of the 
	 * cell and the color of the corresponding snake.
	 * 
	 * @param x - x coordinate in the grid
	 * @param y - y coordinate in the grid
	 * @param color - cell's color
	 * 
	 */
	public DisplayableCell(double x, double y,  Color color)
	{
		super(GameFrame.ELEMENT_SIZE, GameFrame.ELEMENT_SIZE, color);
		
		// update displayable position
		setTranslateX(x*GameFrame.ELEMENT_SIZE);
		setTranslateY(y*GameFrame.ELEMENT_SIZE);

		// Esthetics
		setArcWidth(GameFrame.ELEMENT_SIZE/2);
		setArcHeight(GameFrame.ELEMENT_SIZE/2);
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
	}
	
}
