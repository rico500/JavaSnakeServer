package net.client;

import java.io.IOException;
import java.net.UnknownHostException;

import gui.GameFrame;
import javafx.application.Application;

public class Client extends GameFrame{

	public static void main(String[] args) throws UnknownHostException, IOException {
		Application.launch(args);
	}

}
