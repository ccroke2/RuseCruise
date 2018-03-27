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
	
	static Dimension frameSize = new Dimension(450,600);
	CardLayout cl = new CardLayout();
	LoginScreen2 logScreen = new LoginScreen2(this);
	HomeScreen mainScreen = new HomeScreen();
	JPanel cardPanel = new JPanel();
	//String panelShow = "login";
	
	public StockTickerMain() { 
		setTitle("Stock Ticker");
		add(cardPanel);
		
		cardPanel.setLayout(cl);
		cardPanel.add(mainScreen, "home");
		cardPanel.add(logScreen, "login");
		cl.show(cardPanel, "home");
		cl.next(cardPanel);
		
		//add(new LoginScreen2());
		ImageIcon icon = new ImageIcon ("C:\\Users\\kaitl\\Desktop\\iconRC.png");
		setIconImage(icon.getImage());
		
		
	}
	
	public void setCard() {
		cl.next(cardPanel);
		System.out.println("change card");
	}
	
	
	public static void main(String[] args) {
		 EventQueue.invokeLater(new Runnable(){
	            public void run(){
	            StockTickerMain t = new StockTickerMain();
	            t.setLocation(622, 180);
	            t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            t.setSize(frameSize);
	            t.setResizable(true);
	            t.pack();
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
