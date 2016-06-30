package logic;

import java.util.ArrayDeque;

import com.sun.org.apache.regexp.internal.recompile;


public class Warehouse {
	
	private ArrayDeque<Message> queue;
	
	public Warehouse() {
		queue = new ArrayDeque<Message>();
	}
	
	public void addMessage(Message msn) {
		queue.add(msn);
	}
	
	public Message getMessage() {
		return queue.remove();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public int size() {
		return queue.size();
	}

}
