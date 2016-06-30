package logic;

import java.io.ObjectOutputStream;

public class Node {

	private Client client;
	private Server server;
	private Thread TClient;
	private Thread TServer;
	private Warehouse wareClient;
	private Warehouse wareServer;
	
	public Node(int portS, String ip, int portC, boolean bird, boolean msn) {
		
		
		wareClient = new Warehouse();
		wareServer = new Warehouse();
		
		client = new Client();
		TClient = new Thread(new Runnable() {
			
			@Override
			public void run() {
				client.connection(ip, portC, wareClient , wareServer, msn);
			}
		});
		
		TClient.start();
		
		server = new Server(portS, wareServer, wareClient, msn);
		
		TServer = new Thread(new Runnable() {
			
			@Override
			public void run() {
				server.waitConect();
			}
		});
		
		TServer.start();
	}
	
}
