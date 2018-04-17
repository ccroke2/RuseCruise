package stockTicker;

import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;

import java.util.ArrayList;
import java.lang.Object;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;

//UUX1LQNOWRPP64V9 Alpha Vantage API key

// help with JSON reading/parsing-- http://www.studytrails.com/java/json/java-json-simple/

public class StockTickerMain extends JFrame {

	static Dimension frameSize = new Dimension(950,600);
	
	SplashScreen splScreen;
	LoginScreen2 logScreen;
	HomeScreen mainScreen;
	InfoScreen infoScreen;
	CardLayout cl = new CardLayout();
	JPanel cardPanel = new JPanel();
	
	public StockTickerMain() { 
		setTitle("Stock Ticker");
		add(cardPanel);
		
		splScreen = new SplashScreen(cl, cardPanel);
		logScreen = new LoginScreen2(cl, cardPanel);
		mainScreen = new HomeScreen(cl, cardPanel);
		//infoScreen = new InfoScreen(cl, cardPanel, "GOOGL");
		
		//add(new LoginScreen2());
		//ImageIcon icon = new ImageIcon (getClass().getResource("/iconRC.png"));
		//setIconImage(icon.getImage());\\

		cardPanel.setLayout(cl);
		cardPanel.add(splScreen, "splash");
		cardPanel.add(logScreen, "login");
		cardPanel.add(mainScreen, "home");
		//cardPanel.add(infoScreen, "info");
		cl.show(cardPanel, "login");
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
	            public void run(){
	            StockTickerMain t = new StockTickerMain();
	            t.setLocation(400, 180);
	            t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            t.pack();
	            t.setSize(frameSize);
	            t.setResizable(true);
	            t.setVisible(true);
	             }
	        });
		
	    }

}
