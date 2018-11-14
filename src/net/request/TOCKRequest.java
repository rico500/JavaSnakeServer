package net.request;

import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * 
 * Indicates to the client that all clients have finished updating their game and 
 * that the client may send updates concerning the following game step.
 * 
 * The syntax is:
 * 
 * TOCK
 * 
 * @author ebrunner
 *
 */

public class TOCKRequest extends Request{

	/************************************************************************
	 * 
	 * CONSTANTS
	 * 
	 ************************************************************************/
	
	public static final String KEY = "TOCK";
	
	/************************************************************************
	 * 
	 * ATTRIBUTES
	 * 
	 ************************************************************************/
	
	Runnable tockCallBackClass;
	
	/**
	 * 
	 * Use this constructor to handle a TOCK request
	 * 
	 * @param tockCallbackClass - reference to the callback class which will handle the tock request.
	 */
	public TOCKRequest(Runnable tockCallBackClass) {
		super(null);
		this.tockCallBackClass = tockCallBackClass;
	}
	
	/**
	 * 
	 * Use this constructor to create a TOCK request message and send it
	 * 
	 * @param writer - writer object which will send the message.
	 */
	public TOCKRequest(PrintWriter writer) {
		super(writer);
	}

	@Override
	public void handleRequest(StringTokenizer requestBody) {
		// call tick request callback method
		tockCallBackClass.run();
	}

	@Override
	public String createRequest() {
		return KEY;
	}

}