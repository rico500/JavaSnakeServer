package net.request;

import java.io.PrintWriter;
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
	
	/************************************************************************
	 * 
	 * Attributes
	 * 
	 ************************************************************************/
	
	private Runnable exitCallbackClass;
	
	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	/**
	 * 
	 * Use this constructor to handle an EXIT request
	 * 
	 * @param exitCallbackClass - reference to the callback class which will handle the exit request.
	 */
	public EXITRequest(Runnable exitCallbackClass) {
		super(null);
		this.exitCallbackClass = exitCallbackClass;
	}
	
	/**
	 * 
	 * Use this constructor to create an EXIT request message and send it
	 * 
	 * @param writer - writer object which will send the message.
	 */
	public EXITRequest(PrintWriter writer) {
		super(writer);
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
