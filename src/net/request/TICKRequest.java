package net.request;

import java.util.StringTokenizer;

import game.Game;

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
