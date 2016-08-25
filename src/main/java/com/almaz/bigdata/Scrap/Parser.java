package com.almaz.bigdata.Scrap;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;

public abstract class Parser {

	protected Stat stat;
	Parser()
	{
		stat = new Stat();
	}
	public abstract int check(Connection conn);
	public abstract String parse(Document doc);
	public String resultedStat()	{	return stat.getRepresentation();	}
}
