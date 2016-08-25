package com.almaz.bigdata.Scrap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileWrite {
	private PrintWriter pw;
	public void openTxtFile(String name) throws IOException
	{
		String nameWithExtension = name.concat(".txt");
		FileWriter fw = new FileWriter(nameWithExtension, true);
		pw = new PrintWriter(fw, true);
	}
	public void writeToFile(String data)
	{
		pw.println(data);
//		pw.flush();
	}
	public void closeFile()
	{
		if(pw!=null)
			pw.close();
	}
	public static void main(String[] args)
	{
		FileWrite fileWrite = new FileWrite();
		try {
			fileWrite.openTxtFile("testFile");
			fileWrite.writeToFile("money	foney	honey");
			fileWrite.writeToFile("fist	mist	fo");
			fileWrite.writeToFile("herz	ferz	fo");
			fileWrite.closeFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
