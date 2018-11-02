package net.server;

import java.io.IOException;

import net.client.Client;

public class ServerClient {
	
	public static void main (String[] args) throws IOException {
		String[] serverArgs = {"9999"};
		Server.main(serverArgs);
		String[] clientArgs = {"9999", "127.0.0.1"};
		Client.main(clientArgs);
	}

}
