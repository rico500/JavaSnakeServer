package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import mouvement.Directions;

/**
 * 
 * A displayable node in the javaFX frame. This node is a triangle pointing in the 
 * direction provided by the constructor. It is used to represent the snake's head.
 * 
 * @author ebrunner
 *
 */
public class DisplayableSnakeHead extends Polygon{
	
	/** Size of the point array defining the triangle polygon */
	private static final int P_ARRAY_SIZE = 6;
	
	/** Point array defining a triangle oriented towards South*/
	private static final Double[] polygoneVerticesSouth = {
			0.0, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE/2, 0.0};
	
	/** Point array defining a triangle oriented towards North*/
	private static final Double[] polygoneVerticesNorth = {
			GameFrame.ELEMENT_SIZE/2, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE, 0.0, 
			0.0, 0.0};
	
	/** Point array defining a triangle oriented towards East*/
	private static final Double[] polygoneVerticesEast = {
			0.0, 0.0, 
			0.0, GameFrame.ELEMENT_SIZE, 
			GameFrame.ELEMENT_SIZE, GameFrame.ELEMENT_SIZE/2};
	
	/** Point array defining a triangle oriented towards West*/
	private static final Double[] polygoneVerticesWest = {
			0.0, GameFrame.ELEMENT_SIZE/2, 
			GameFrame.ELEMENT_SIZE, 0.0, 
			GameFrame.ELEMENT_SIZE, GameFrame.ELEMENT_SIZE};
	
	
	/**
	 * 
	 * Snake head constructor. Defines the location, color and orientation of the snake's head
	 * 
	 * @param x - x coordinate in grid
	 * @param y - y coordinate in grid
	 * @param dir - direction towards which the head is oriented
	 * @param fillColor - head color
	 */
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

	/**
	 * 
	 * translates a list of points by x and y.
	 * 
	 * @param x - x translation component
	 * @param y - y translation component
	 * @param pointList - point list to be translated
	 * @return translated point list
	 * 
	 */
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
