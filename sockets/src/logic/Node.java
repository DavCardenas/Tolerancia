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
		
		Queues queues = Queues.getQueues(wareClient, wareServer);
		
		client = new Client();
		TClient = new Thread(new Runnable() {
			
			@Override
			public void run() {
				client.connection(ip, portC, queues, msn);
			}
		});
		
		TClient.start();
		
		server = new Server(portS, queues , msn);
		
		TServer = new Thread(new Runnable() {
			
			@Override
			public void run() {
				server.waitConect();
			}
		});
		
		TServer.start();
	}
	
}
