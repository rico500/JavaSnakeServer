package entity;


import java.util.ArrayList;

import javafx.scene.paint.Color;

public interface Entity {

	public ArrayList<Cell> getCellList();
	public Color getColor();
	public void evolve();
	public boolean isPlayable();
	
}
