package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Registry {
	
	private File file;
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	
	public Registry() {
		
	}
	
	public void readFile() throws IOException {
		fileReader = new FileReader(file);
		bufferedReader = new BufferedReader(fileReader);
		String c;
		while ((c = bufferedReader.readLine()) != null) {
			System.out.println(c);
		}
	}
	
	public void writeFile(String data) throws IOException {
		fileWriter = new FileWriter(file,true);
		bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(data);
		bufferedWriter.newLine();
		bufferedWriter.close();
	}
	
	public void createFile(String path) {
		file = new File(path);
	}

}
