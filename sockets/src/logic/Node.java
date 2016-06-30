package logic;

import java.io.ObjectOutputStream;

public class Node {

	private Client client;
	private Server server;
	private Thread TClient;
	private Thread TServer;
	
	public Node(int portS, String ip, int portC, boolean bird, boolean msn) {
		
		
		client = new Client();
		TClient = new Thread(new Runnable() {
			
			@Override
			public void run() {
				client.connection(ip, portC, bird);
			}
		});
		
		TClient.start();
		
		server = new Server(portS, bird);
		
		TServer = new Thread(new Runnable() {
			
			@Override
			public void run() {
				server.waitConect();
			}
		});
		
		TServer.start();
	}
	
}
