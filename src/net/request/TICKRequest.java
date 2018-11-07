package net.request;

import java.util.StringTokenizer;

import game.Game;

/**
 * 
 * Indicates to the client that it has received all necessary information
 * to ensure its version of the game is up to date with the server's
 * version of the game.
 * 
 * The syntax is:
 * 
 * TICK
 * 
 * @author ebrunner
 *
 */
public class TICKRequest extends Request{

	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	public static final String KEY = "TICK";
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	Runnable tickCallBackClass;
	
	public TICKRequest(Runnable tickCallBackClass) {
		super(null);
		this.tickCallBackClass = tickCallBackClass;
	}
	
	public TICKRequest() {
		super(null);
	}

	@Override
	public void handleRequest(StringTokenizer requestBody) {
		// call tick request callback method
		tickCallBackClass.run();
	}

	@Override
	public String createRequest() {
		return KEY;
	}

}
