package com.almaz.bigdata.Scrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.glassfish.grizzly.http.server.HttpServer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.almaz.server.*;

public class Scraper implements Runnable
{
	private static ConcurrentLinkedQueue<String> linkList = new ConcurrentLinkedQueue<String>();
	private String sessionId; 
	private Parser parser;
	private int id;
	private final static int PROCESS_AMOUNT = 10;
	
	public Scraper(int idd)
	{
		id = idd;
	}
	public static void prepareLinks(String baseURL, int firstIdx, int lastIdx) throws Exception
	{
		String url = "";
		for(int i = firstIdx; i<lastIdx; i++)
		{
			url = baseURL.concat(Integer.toString(i));
			linkList.add(url);
		}
	}
	
	public void setSessinoId(String firstLink)
	{
		sessionId = firstLink.replaceFirst("^https?:/*(www)?[.]?", "");
		sessionId = sessionId.replaceAll("[./]", "_");
	}
	public void scrap() throws IOException
	{
		int size = linkList.size();
		String currentUrl = "";
		Connection resp;
		Document doc;
		
		int responce = 0;
		while(!linkList.isEmpty())
		{
			currentUrl = linkList.poll();
			
			resp = Jsoup.connect(currentUrl)
		    	.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
		    	.referrer("http://www.google.com")
		    	.ignoreHttpErrors(true);

			//java.net.SocketTimeoutException: Read timed out
			try {
				doc = resp.get();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

	        responce = parser.check(resp);
		    if( responce==404 )//страница не существует
		    {
		       	continue;
		    }
		    else if(responce != 200)
		    {
		       	continue;
		    }
		    parser.parse(doc);
	        MyResource.value = parser.stat.getRepresentation();
		        
		}
	}

	public void configure(String className)
	{
		String classFullName = new String("com.almaz.bigdata.Scrap.").concat(className);
		try{
			Class classTemp = Class.forName(classFullName);
		System.out.println(classFullName);
		parser = (Parser)classTemp.newInstance();
		} catch( ClassNotFoundException e){
			System.out.println("Almaz " + e);
		} catch( IllegalAccessException e){
			System.out.println("Almaz " + e);
		} catch( InstantiationException e){
			System.out.println("Almaz " + e);
		}	
	}
	
	public void run()
	{
		try {
			setSessinoId(linkList.peek());
//			parser = new HhVacancy();
			scrap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception
	{
		if(args.length != 4)
			System.err.println("Usage: <ParserClass> <url> <firstIndex> <lastIndex>");

		//"https://hh.ru/vacancy/"
		String urlBase = args[1];
		
//		try{
		Scraper.prepareLinks(urlBase, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
//		} catch(NumberFormatException e){
//			System.out.println("1: " + args[0] + " 2: " + args[1] + " 3: " + args[2] + " 4: " + args[3]);
//			System.exit(-1);
//		}
		Scraper[] scraperArr = new Scraper[PROCESS_AMOUNT];
		for(int i=0; i<PROCESS_AMOUNT; i++)
		{
			scraperArr[i] = new Scraper(i+1); 
			scraperArr[i].configure(args[0]);
			(new Thread (scraperArr[i]) ).start();
		}
		
        final HttpServer server = GrizzlyHttpServer.startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", GrizzlyHttpServer.getBaseUri()));
        MyResource.value = "Got it olala";
        try{
        System.in.read();
        } catch(IOException e)	{	e.printStackTrace();	}
        server.stop();
	}
	
}
