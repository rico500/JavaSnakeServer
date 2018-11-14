package net.request;

import java.io.PrintWriter;
import java.util.StringTokenizer;

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
	
	/**
	 * 
	 * Use this constructor to handle a TICK request
	 * 
	 * @param tickCallbackClass - reference to the callback class which will handle the tick request.
	 */
	public TICKRequest(Runnable tickCallBackClass) {
		super(null);
		this.tickCallBackClass = tickCallBackClass;
	}
	
	/**
	 * 
	 * Use this constructor to create a TICK request message and send it
	 * 
	 * @param writer - writer object which will send the message.
	 */
	public TICKRequest(PrintWriter writer ) {
		super(writer);
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
