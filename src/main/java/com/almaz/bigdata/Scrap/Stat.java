package com.almaz.bigdata.Scrap;

public class Stat 
{
	static int cntOk = 0;
	static int cntError = 0;
	static int cntWarning = 0;
	static int cntTotal = 0;
	static double percent = 0.0;

	protected void increment(int value)
	{
    	switch(value)
    	{
    	case 200:	cntOk++; cntTotal++; break;
    	case 403: cntError++; cntTotal++; break;
    	case 404: cntWarning++; cntTotal++; break;
    	default: break;
    	}
    	percent = (double)cntOk / cntTotal * 100;
/*    	System.out.println(" AOP afer check: OK = " + Integer.toString(cntOk) 
    								+ "  Warning = " + Integer.toString(cntWarning) 
    								+  "  Error = " + Integer.toString(cntError)  
    								+  "  Total = " + Integer.toString(cntTotal)  
    								+  "  Percent = " + Double.toString(percent)
    						);
*/    }
	protected String getRepresentation()
	{
    	String statRepresentation = "OK = " + Integer.toString(cntOk) 
				+ "  Warning = " + Integer.toString(cntWarning) 
				+  "  Error = " + Integer.toString(cntError)  
				+  "  Total = " + Integer.toString(cntTotal)  
				+  "  Percent = " + Double.toString(percent);

//    	System.out.println(statRepresentation);
    	return statRepresentation;
	}
}
