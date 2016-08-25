package com.almaz.bigdata.Scrap;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Connection;

public privileged aspect Logger {
	
	FileWrite fileWrite;
	HashMap<Parser, FileWrite> activeParsersMap = new HashMap<Parser, FileWrite>();
	FileWrite fileStat;
	FileWrite fileInvalidResponces;
	int statFlag = 0;
	int statCnt = 0;
	
    pointcut scraperExecution(Scraper scraper): execution(public void scrap*(..)) && target(scraper);

    before(Scraper scraper) : scraperExecution(scraper) {    
    	try{
	   		activeParsersMap.put(scraper.parser, new FileWrite());
	   		String filename = scraper.sessionId + "_" + scraper.id;
	       	activeParsersMap.get(scraper.parser).openTxtFile(filename);
	       	System.out.println("before scrap " + filename);
    		if(statCnt==0)
    		{
//    			fileWrite = new FileWrite();
//    			fileWrite.openTxtFile(scraper.sessionId);
    			fileInvalidResponces = new FileWrite();
    			fileInvalidResponces.openTxtFile("responces");    			
    		}
	       	statCnt++;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    after(Scraper scraper) : scraperExecution(scraper) {    
    	try{
	       	fileWrite = activeParsersMap.get(scraper.parser);
	    	fileWrite.closeFile();
//	    	fileWrite.closeFile();
	    	
	    	if(statFlag == 0)
	    	{
	    		fileStat = new FileWrite();
	    		fileStat.openTxtFile("stat");
	    		statFlag = 1;
	    	}
	    	statCnt--;
			System.out.println("The end. statCnt = " + statCnt);		
	    	if(statCnt == 0)
	    	{
		    	fileStat.writeToFile(scraper.parser.resultedStat() );
	        	fileStat.closeFile();    
	        	fileInvalidResponces.closeFile();
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    pointcut parserExecution(Parser parser): execution(public String Parser.parse*(..)) && target(parser);

    after(Parser parser) returning(String result) : parserExecution(parser) {    
       	fileWrite = activeParsersMap.get(parser);
    	fileWrite.writeToFile(result);
//    	fileWrite.writeToFile(result);
    }
    
    pointcut checkExecution(Connection arg): execution(public int Parser.check(..)) && args(arg);

    after(Connection arg) returning(int result) : checkExecution(arg) {    
        if((result!=200) && ( result!=404))
        {
//          System.out.println( "AOP: " + result + " " + arg.request().url().toString());
        	fileInvalidResponces.writeToFile(Integer.toString(result) + " " + arg.request().url().toString());
        }
    }
    
}