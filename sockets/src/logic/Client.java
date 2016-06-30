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
	private boolean serverData;
	
	public void connection(String ip, int port, boolean serverD) {
		dir_ip = ip;
		this.port = port;
		ware = new Warehouse();
		run = true;
		serverData = serverD;
		
		try {
			socket = new Socket(dir_ip,this.port);
			
			if (!serverData) {
				Thread hilo = new Thread(new Runnable() {
					
					@Override
					public void run() {
						writeSocket();					
					}
				});
				hilo.start();
				
				Thread hilo2 = new Thread(new Runnable() {
	
					@Override
					public void run() {
						readSocket();					
					}
				});
				hilo2.start();
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
				connection(dir_ip , port, serverD);
			}
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
			while (run) {
					msn = new Message();
					msn.setIp(getIP());
					msn.setHour(generateHour());
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
			if (!ware.isEmpty()) {	
				System.out.println("entra ");
				msn = ware.getMessage();
					try{
						System.out.println(ware.size());
						objectOut.writeObject(msn);
						System.out.println(ware.size());
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
}
