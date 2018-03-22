package stockTicker;

import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;

/*
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
*/

//UUX1LQNOWRPP64V9 Alpha Vantage API key

// help with JSON reading/parsing-- http://www.studytrails.com/java/json/java-json-simple/

public class StockTickerMain extends JFrame{

	JPanel screen = new JPanel();
	
	public StockTickerMain() { 
		setTitle("Stock Ticker");
		add(screen);
	}
	
	
	public static void main(String[] args) {
		 EventQueue.invokeLater(new Runnable(){
	            public void run(){
	            StockTickerMain t = new StockTickerMain();
	            t.setLocation(470, 220);
	            t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            t.setSize(900,600);
	            t.setResizable(false);
	            t.setVisible(true);
	             }
	        });
	    }
		
		
		/*APIcall daily = new APIcall();
		
		// This gets today's date
		TimeZone timeZone = TimeZone.getTimeZone("US/Eastern");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(timeZone);
		Date date = new Date();
		String d = (String)dateFormat.format(date);
		
		
		// Single Stock info example (With printing final vector)
		// s becomes a vector 
		ArrayList<String> s = new ArrayList<String>();
		s = daily.returnAPI_single("GOOGL", d);
		System.out.println(s);
		 
		System.out.println();
		
		//Percentage
		double perc = daily.stockPercent("AAPL");
		System.out.println(perc);
		
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
		*/
}
