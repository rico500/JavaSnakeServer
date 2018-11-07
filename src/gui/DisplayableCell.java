package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class DisplayableCell extends Rectangle {
	
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
