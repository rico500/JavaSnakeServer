package net.request;

import java.util.StringTokenizer;

/**
 * 
 * Request is sent when client exits game. The client's entity is removed from the game.
 * 
 * The syntax is :
 * 
 * EXIT
 * 
 * @author ebrunner
 *
 */
public class EXITRequest extends Request{

	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	public static final String KEY = "EXIT";
	
	Runnable exitCallbackClass;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	public EXITRequest(Runnable exitCallbackClass) {
		super(null);
		this.exitCallbackClass = exitCallbackClass;
	}
	
	public EXITRequest() {
		super(null);
	}
	
	/************************************************************************
	 * 
	 * PUBLIC METHODS
	 * 
	 ************************************************************************/
	
	@Override
	public void handleRequest(StringTokenizer requestBody) {
		exitCallbackClass.run();
	}

	@Override
	public String createRequest() {
		return KEY;
	}
	
	
	
}
