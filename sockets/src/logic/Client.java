package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

public class Client {
	
	private Socket socket;
	private ObjectOutputStream objectOut;
	private ObjectInputStream objectInput;
	private Message msn;
	private Calendar calendar;
	private String dir_ip;
	private BufferedReader br;
	private boolean run;
	private int port;
	private int attempts = 1;
	private Warehouse ware;
	private Warehouse wareS;
	private Queues queues;
	private boolean message;
	
	public void connection(String ip, int port, Queues queues, boolean msn) {
		dir_ip = ip;
		this.port = port;
		this.queues = queues;
		run = true;
		message = msn;
		
		try {
			socket = new Socket(dir_ip,this.port);
			
				Thread hilo2 = new Thread(new Runnable() {
	
					@Override
					public void run() {
						readSocket();					
					}
				});
				hilo2.start();
				
				if (message) {
					Thread hilo = new Thread(new Runnable() {
						
						@Override
						public void run() {
							writeSocket();					
						}
					});
					hilo.start();
					
				}
			
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("No se puede conectar");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (attempts < 5) {
				attempts += 1;
				connection(dir_ip , port, this.queues, msn);
			}
		}
	}
	
	public void readSocket() {
		try {
			objectInput = new ObjectInputStream(socket.getInputStream());
			while (run) {
				try {
					msn = (Message) objectInput.readObject();
					if (msn != null) {
						System.out.println(msn.getMessage() + " Leido desde el cliente");
						queues.getWareClient().addMessage(msn);
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
			socket.close();
		} catch (IOException e) {
		}
	}
	
	public void writeSocket() {
		try {
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			//while (run) {
					msn = new Message();
					msn.setIp(getIP());
					msn.setHour(generateHour());
					msn.setMessage("Hola Mundo");
					try{
						
						objectOut.writeObject(msn);
						System.out.println("Mensaje enviado");
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
	
	public String generateHour(){
		int H,M,S;
		calendar = new GregorianCalendar();
		H = calendar.get(Calendar.HOUR_OF_DAY);
		M = calendar.get(Calendar.MINUTE);
		S = calendar.get(Calendar.SECOND);
		
		return H + ":" + M+ ":" + S;
	}
	
	public String getIP() throws SocketException {
		String ip = "";
		for (Enumeration<NetworkInterface> ifaces = 
	               NetworkInterface.getNetworkInterfaces();
	             ifaces.hasMoreElements(); )
	        {
	            NetworkInterface iface = ifaces.nextElement();
	            
	            for (Enumeration<InetAddress> addresses =
	                   iface.getInetAddresses();
	                 addresses.hasMoreElements(); )
	            	
	            {
	                InetAddress address = addresses.nextElement();
	                if (address.getHostAddress().contains("192.168.1.")) {
						ip = address.getHostAddress();
					}
	            }
	        }
		return ip;
	}
	
	public void replyMessage() {
		try {
			//objectOut = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("entra ");
			if (!queues.getWareServer().isEmpty()) {	
				msn = queues.getWareServer().getMessage();
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
