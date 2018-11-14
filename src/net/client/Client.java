package net.client;

import java.io.IOException;
import java.net.UnknownHostException;

import gui.GameFrame;
import javafx.application.Application;

/**
 * 
 * Main client class.
 * 
 * launch with following arguments : 
 * 
 * server_port server_address movement_type
 * 
 * You can choose the movement type in the following list:
 * 
 * STR : Straight snake, is controllable by a client
 * RND : Random snake, moves in a random fashion 
 * RDI : Random intelligent snake, randomly chooses a direction to avoid an obstacle
 * VIT : Very intelligent snake, will always go in the direction with the farthest obstacle
 * 
 * @author ebrunner
 *
 */
public class Client extends GameFrame{

	public static void main(String[] args) throws UnknownHostException, IOException {
		Application.launch(args);
	}

}
