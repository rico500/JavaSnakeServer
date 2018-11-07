package net.request;

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
	
	public TOCKRequest(Runnable tockCallBackClass) {
		super(null);
		this.tockCallBackClass = tockCallBackClass;
	}
	
	public TOCKRequest() {
		super(null);
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