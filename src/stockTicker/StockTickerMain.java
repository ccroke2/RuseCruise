package stockTicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//UUX1LQNOWRPP64V9 Alpha Vantage API key

// help with JSON reading/parsing-- http://www.studytrails.com/java/json/java-json-simple/

public class StockTickerMain {

	public static void main(String[] args) {
		APIcall daily = new APIcall();
		
		// This gets today's date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String d = (String)dateFormat.format(date);
		
		// Single Stock info example (With printing final vector)
		// s becomes a vector 
		ArrayList<String> s = new ArrayList<String>();
		s = daily.returnAPI_single("GOOGL", d);
		System.out.println(s);
		
		System.out.println();
		
		// Batch stock info example (With printing final vector)
		// kList holds stock symbols that are wanted
		// k hold the current value of each stock, in the same order as kList
		ArrayList<String> kList = new ArrayList<String>();
		kList.add("GOOGL");
		kList.add("BAC");
		kList.add("F");
		kList.add("GE");
		ArrayList<String> k = new ArrayList<String>();
		k = daily.returnAPI_batch(kList);
		System.out.println(k);
		
	}
}
