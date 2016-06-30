package logic;

public class Queues {

	private Warehouse wareClient;
	private Warehouse wareServer;
	private static Queues queues;
	
	public static Queues getQueues(Warehouse wareC, Warehouse wareS){
		if (queues == null) {
			queues = new Queues(wareC,wareS);
		}
		return queues;
	}
	
	private Queues(Warehouse wareC, Warehouse wareS) {
		this.wareClient = wareC;
		this.wareServer = wareS;
	}

	public Warehouse getWareClient() {
		return wareClient;
	}

	public void setWareClient(Warehouse wareClient) {
		this.wareClient = wareClient;
	}

	public Warehouse getWareServer() {
		return wareServer;
	}

	public void setWareServer(Warehouse wareServer) {
		this.wareServer = wareServer;
	}
	
	
}
