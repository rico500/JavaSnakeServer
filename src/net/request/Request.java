package net.request;

import java.io.PrintWriter;
import java.util.StringTokenizer;


public abstract class Request {

	/************************************************************************
	 * 
	 * ATTRBUTES
	 * 
	 ************************************************************************/
	
	private PrintWriter writer;

	/************************************************************************
	 * 
	 * CONSTRUCTOR
	 * 
	 ************************************************************************/
	
	public Request(PrintWriter writer) {
		this.writer = writer;
	}
	
	/************************************************************************
	 * 
	 * PUBLIC METHOD
	 * 
	 ************************************************************************/
	
	public void sendMessage() {
		writer.println(createRequest());
		writer.flush();
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
