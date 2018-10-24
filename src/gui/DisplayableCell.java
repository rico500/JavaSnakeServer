package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class DisplayableCell extends Rectangle {
	
	public DisplayableCell(double x, double y, double size,  Color color)
	{
		super(size, size, color);
		
		// update displayable position
		setTranslateX(x*size);
		setTranslateY(y*size);

		// Esthetics
		setArcWidth(size/2);
		setArcHeight(size/2);
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
	}
	
}
