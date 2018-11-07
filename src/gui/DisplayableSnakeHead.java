package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import mouvement.Directions;

public class DisplayableSnakeHead extends Polygon{
	
	private static final int P_ARRAY_SIZE = 6;
	
	private static final Double[] polygoneVerticesSouth = {
			0.0, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE/2, 0.0};
	
	private static final Double[] polygoneVerticesNorth = {
			GameFrame.ELEMENT_SIZE/2, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE, 0.0, 
			0.0, 0.0};
	
	private static final Double[] polygoneVerticesEast = {
			0.0, 0.0, 
			0.0, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE, GameFrame.ELEMENT_SIZE/2};
	
	private static final Double[] polygoneVerticesWest = {
			0.0, GameFrame.ELEMENT_SIZE/2, 
			GameFrame.ELEMENT_SIZE, 0.0, 
			GameFrame.ELEMENT_SIZE, GameFrame.ELEMENT_SIZE};
	
	public DisplayableSnakeHead(double x, double y, Directions dir, Color fillColor) {
		
		switch(dir) {
		
		case NORTH : 
			getPoints().setAll(add(x*GameFrame.ELEMENT_SIZE, y*GameFrame.ELEMENT_SIZE, polygoneVerticesNorth));
			break;
		case SOUTH : 
			getPoints().setAll(add(x*GameFrame.ELEMENT_SIZE, y*GameFrame.ELEMENT_SIZE, polygoneVerticesSouth));
			break;
		case EAST : 
			getPoints().setAll(add(x*GameFrame.ELEMENT_SIZE, y*GameFrame.ELEMENT_SIZE, polygoneVerticesEast));
			break;
		case WEST : 
			getPoints().setAll(add(x*GameFrame.ELEMENT_SIZE, y*GameFrame.ELEMENT_SIZE, polygoneVerticesWest));
			break;
		}
		
		// Esthetics
		setStroke(Color.BLACK);
		setStrokeWidth(3.0);
		setStrokeLineCap(StrokeLineCap.ROUND);
		setFill(fillColor);
	}

	private Double[] add(double x, double y, Double[] pointList) {
		Double[] newPointList = new Double[P_ARRAY_SIZE];
		for(int i = 0; i < P_ARRAY_SIZE; i = i + 2) {
			newPointList[i] = pointList[i] + x;
		}
		for(int i = 1; i < P_ARRAY_SIZE; i = i + 2) {
			newPointList[i] = pointList[i] + y;
		}
		return newPointList;
	}
	
}
