package entity;


import java.util.ArrayList;

import javafx.scene.paint.Color;

public interface Entity {

	public ArrayList<Cell> getCellList();
	public void addCell(int x, int y);
	public Color getColor();
	public String getKey();
	public void evolve();
	public boolean isPlayable();
	public void setID(int ID);
	public int getID();
	
}
