package net.request;

import java.util.StringTokenizer;

import game.Game;

public abstract class Request {

	/************************************************************************
	 * 
	 * ATTRBUTES
	 * 
	 ************************************************************************/
	
	/** Requests act on a game, thus a reference to the game 
	 * must exist in every request */
	protected Game game;

	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	public Request(Game game) {
		this.game = game;
	}
	
	/************************************************************************
	 * 
	 * ABSTRACT METHODS
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Retrieve information or make modifications on game according 
	 * to received informations
	 * 
	 */
	abstract public void handleRequest(StringTokenizer requestBody);
	
	/**
	 * 
	 * Create a request in the standardized form
	 * 
	 */
	abstract public String createRequest();
	
}
