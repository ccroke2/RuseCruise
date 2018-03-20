package stockTicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//UUX1LQNOWRPP64V9 Alpha Vantage API key
public class StockTickerMain {

	public static void main(String[] args) {
		APIcall daily = new APIcall();
		
		//this gets today's date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String d = (String)dateFormat.format(date);
		
		ArrayList<String> s = new ArrayList<String>();
		s = daily.returnAPI_single("GOOGL", d);
		System.out.println(s);
		
		System.out.println();
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
