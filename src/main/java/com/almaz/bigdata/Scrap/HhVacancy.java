package com.almaz.bigdata.Scrap;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HhVacancy extends Parser
{
	public int check(Connection conn)
	{
		int responce = conn.response().statusCode();
		stat.increment(responce);
		return responce;
	}
	public String parse(Document doc)
	{
		String res = " ";
		
		//get Link to vacancy page
		Elements links = doc.select("meta[itemprop=url]");
        if(links.size()==0)	
        {
    		links = doc.select("link[rel=canonical]");
            if(links.size()==0)	
            	res = res.concat("null	");
            else
            {
//            	System.out.println( links.first().attr("href") );
            	res = res.concat( links.first().attr("href").replaceFirst("^https?:/*(www)?[.]?", "") ).concat("	");
            }
        }
        else
        {
        	res = res.concat( links.first().attr("content").replaceFirst("^https?:/*(www)?[.]?((?!hh).)*", "") ).concat("	");
        }
        
        
        //Publication date
		links = doc.getElementsByAttributeValue("class", "vacancy-sidebar__publication-date");
        if(links.size()!=1)	
        {
    		links = doc.select("meta[name=description]");
            if(links.size()!=1)	
            	res = res.concat("null	");
            else
            	res = res.concat( links.first().attr("content").replaceFirst(".*Дата публикации: ", "") ).concat("	");
            	
        }
        else
        {
        	res = res.concat( links.first().text() ).concat("	");
        }
       
        //Salary, City, Years of experience
        links = doc.select("td>div[class=l-paddings]");
        if(links.size()!=3)	
        	res = res.concat("null	").concat("null	").concat("null	");
        else
        {
            for(Element link : links)
            	res = res.concat( link.text() ).concat("	");
        }
        
        //Vacancy title
        links = doc.select("h1[class=title b-vacancy-title");
        if(links.size()!=1)	
        	res = res.concat("null	");
        else
        	res = res.concat( links.first().text() ).concat("	");

        //Organization name and link
        links = doc.select("a[itemprop=hiringOrganization");
        if(links.size()!=1)	
        {
        	res = res.concat("null	");
        	res = res.concat("null	");
        }
        else
        {
        	res = res.concat( links.first().text() ).concat("	");
        	res = res.concat( links.first().attr("href") ).concat("	");
        }

        //Vacancy mode
        links = doc.select("div[class=b-vacancy-employmentmode l-paddings");
        if(links.size()!=1)	
        	res = res.concat("null	");
        else
        	res = res.concat( links.first().text().replaceAll("Тип занятости", " ") ).concat("	");

        //Required skills
        links = doc.select("span[data-qa=skills-element");
        if(links.size()==0)
        	res = res.concat("null	");
        else
        {
	        for(Element link : links)
	        {
	        	res = res.concat( link.attr("data-tag-id").replaceAll("\t", " ") );
	        	res = res.concat(", ");
//	        	System.out.println( link.attr("data-tag-id") );
	        }
        	res = res.concat("	");	        
        }

        //Vacancy description
        links = doc.select("div[class=b-vacancy-desc-wrapper");
        if(links.size()!=1)	
        	res = res.concat("null	");
        else
        	res = res.concat( links.first().text().replaceAll("\t|\r|\n", " ") ).concat("	");
 //       System.out.println( links.first().text() );

//       System.out.println(res);
		return res;
	}
}
