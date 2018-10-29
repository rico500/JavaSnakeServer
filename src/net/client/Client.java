package net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		int port = Integer.parseInt(args[0]);
		String machine = args[1];

		Socket comSocket = new Socket(machine, port);

		PrintWriter writer = new PrintWriter(comSocket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				comSocket.getInputStream()
				)) ;
		
		while(true) {
			if(reader.ready())
				System.out.println(reader.readLine());
		}
	}

}