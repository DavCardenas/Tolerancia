package logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

	private Socket socket;
	private ServerSocket serverSocket;
	private ObjectInputStream objectInput;
	private ObjectOutputStream objectOut;
	private Message msn;
	private int port;
	private boolean run;
	private Warehouse ware;
	private Warehouse wareC;
	private boolean message;
	private Queues queues;
	private Registry registry;

	public Server(int port, Queues queues ,boolean msn) {
		this.port = port;
		socket = new Socket();
		run = true;
		message = msn;
		this.queues = queues;
		try {
			serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void waitConect() {
		while (run) {

			try {
				System.out.println("Esperando Conexion");
				socket = serverSocket.accept();
				System.out.println("Conexion aceptada");
				
				Thread hilo = new Thread(new Runnable() {

					@Override
					public void run() {
						readSocket();
					}
				});

				hilo.start();
				
				if (message) {
					Thread hilo2 = new Thread(new Runnable() {

						@Override
						public void run() {
							writeSocket();
						}
					});

					hilo2.start();
				}
				
			} catch (IOException e) {
			}
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
		}
	}
	
	public void replyMessage() {
		try {
			//objectOut = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("entra a replicar");
			if (!queues.getWareClient().isEmpty()) {	
				msn = queues.getWareClient().getMessage();
					try{
						objectOut.writeObject(msn);
					}catch(SocketException e) {
						System.out.println("No se envio");
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}else {
				System.out.println("No replico");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void writeSocket() {
		try {
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			//while (run) {
					msn = new Message();
					msn.setMessage("Hola Mundo");
					try{
						
						objectOut.writeObject(msn);
						//replyMessage();
					}catch(SocketException e) {
						System.out.println("NOTA NO DEBERIA ACABAR");
						run = false;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			//}
			
			//objectOut.close();
			//socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void readSocket() {
		try {
			objectInput = new ObjectInputStream(socket.getInputStream());
			while (run) {
				try {
					msn = (Message) objectInput.readObject();
					if (msn != null) {
						System.out.println(msn.getMessage() + " Leido desde el servidor");
						queues.getWareServer().addMessage(msn);
						replyMessage();
					} else {
						System.out.println("SALE");
						objectInput.close();
						socket.close();
					}
				} catch (ClassNotFoundException e) {
					socket.close();
				}
			}
			
			objectInput.close();
			serverSocket.close();
		} catch (IOException e) {
		}
	}
	
	public void saveRegister(Message msn) {
		registry.createFile("src/files/Registro.dat");
		try {
			registry.writeFile(msn.getIp() + " " + msn.getHour() + " " + msn.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
