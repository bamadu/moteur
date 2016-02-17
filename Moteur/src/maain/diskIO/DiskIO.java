package maain.diskIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;


public class DiskIO {

	public final static String RESOURCE_DIR = "Resource/dico/";
	protected int ioNumber=0;
	private boolean debug = true;
	
	public void writeToFile(List<String> words, String path) {
		path = getFilename (path);
		File file = new File (path);
		file.delete();
		try ( Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(file), "utf-8"))) {
			String prev = "";
			for (String el: words)
				if(StringUtils.isNotBlank(el) && !StringUtils.equalsIgnoreCase(el, prev))
				{
					writer.write(el+"\n");
					prev = el;
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (debug)
			System.out.println(path+" saved ...");
		incrIO();
	}
	
	public List<String> getDataFromFile(String filename) 
	{
		List<String> buf = new ArrayList<String>();	
		
		try (Scanner scan = new Scanner(new File(getFilename(filename)))) 
		{
			while ( scan.hasNextLine()) 
			{
				String s = StringUtils.stripAccents(scan.nextLine());
				if(StringUtils.isNotBlank(s))
					buf.add(s);
			}
				
		
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Fail to open the file, check his path !");
			e.printStackTrace();
		}
		return buf;
	}
	
	public String getFilename(String path) {
		return RESOURCE_DIR+path+".txt" ;
	}
	
	public int getIoNumber() {
		return ioNumber;
	}

	protected void incrIO(int nb) {
		ioNumber += nb;
	}
	
	protected void incrIO() {
		incrIO(1);
	}
}
