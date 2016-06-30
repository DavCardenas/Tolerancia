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
	private boolean serverData;
	private Registry registry;

	public Server(int port, Warehouse wareS , Warehouse wareC,boolean serverD) {
		this.port = port;
		socket = new Socket();
		run = true;
		serverData = serverD;
		this.ware = wareS;
		this.wareC = wareC;
		if (serverData) {
			registry = new Registry();
		}
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
					
					Thread hilo2 = new Thread(new Runnable() {

						@Override
						public void run() {
							writeSocket();
						}
					});

					hilo2.start();
				
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
			if (!wareC.isEmpty()) {	
				System.out.println("entra ");
				msn = wareC.getMessage();
					try{
						objectOut.writeObject(msn);
					}catch(SocketException e) {
						System.out.println("No se envio");
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void writeSocket() {
		try {
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			while (run) {
					msn = new Message();
					msn.setMessage("Hola Mundo");
					try{
						
						objectOut.writeObject(msn);
						replyMessage();
					}catch(SocketException e) {
						System.out.println("NOTA NO DEBERIA ACABAR");
						run = false;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			
			objectOut.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void readSocket() {
		try {
			objectInput = new ObjectInputStream(socket.getInputStream());
			System.out.println("asd");
			while (run) {
				try {
					msn = (Message) objectInput.readObject();
					if (msn != null) {
						System.out.println(msn.getIp());
						System.out.println(msn.getHour());
						System.out.println(msn.getMessage());
						ware.addMessage(msn);
						if (serverData) {
							saveRegister(msn);
						}
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
