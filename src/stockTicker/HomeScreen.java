package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import javax.swing.*;

//import stockTicker.HomeScreen2.Research;


public class HomeScreen extends JPanel implements ActionListener {
	
	private JScrollPane scrollPane = new JScrollPane();
	private JButton butt = new JButton ("Log Out!");
	private JLabel text = new JLabel("Welcome to your Ruse Cruise Stock Ticker");
	private JPanel pan = new JPanel();
	private JPanel search = new JPanel();
	private JPanel allStocks = new JPanel();
	private ArrayList<JPanel> stockPanels = new ArrayList<JPanel>(20);
	
	CardLayout cl;
	JPanel cardPanel;
	
	public HomeScreen (CardLayout clin, JPanel cardPanelin) {
		setLayout(new BorderLayout());
		
		JTextField Ctf = new JTextField(30);
		JButton jb1 = new JButton("Research");
		search.add(text);
		search.add(Ctf);
		search.add(jb1);
		cl = clin;
		cardPanel = cardPanelin;
		
		scrollPane.setPreferredSize(new Dimension (500, 300));
		scrollPane.add(butt);
		
		//Prints out stocks from test_stocks.txt
		try {
			BufferedReader in =  new BufferedReader( 
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream("test_stocks.txt")));
		

		    String inputLine;
		    allStocks.setLayout(new GridLayout(0,1));
			while ((inputLine = in.readLine()) != null) {
		        JLabel stock = new JLabel(inputLine);
		       JPanel sPanel = new JPanel();
		       sPanel.add(stock);
		       allStocks.add(sPanel);
			}		    
		    in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		scrollPane.setViewportView(allStocks);
		add(scrollPane, BorderLayout.CENTER);
		text.setHorizontalAlignment(JLabel.CENTER);
		add(search, BorderLayout.NORTH);
		//add(pan);
		add(butt, BorderLayout.SOUTH);
		butt.addActionListener(this);
		
		//creates all of the stock panels
		JPanel stock1  = new JPanel();
		JPanel stock2  = new JPanel();
		JPanel stock3  = new JPanel();
		JPanel stock4  = new JPanel();
		JPanel stock5  = new JPanel();
		JPanel stock6  = new JPanel();
		JPanel stock7  = new JPanel();
		JPanel stock8  = new JPanel();
		JPanel stock9  = new JPanel();
		JPanel stock10 = new JPanel();
		JPanel stock11 = new JPanel();
		JPanel stock12 = new JPanel();
		JPanel stock13 = new JPanel();
		JPanel stock14 = new JPanel();
		JPanel stock15 = new JPanel();
		JPanel stock16 = new JPanel();
		JPanel stock17 = new JPanel();
		JPanel stock18 = new JPanel();
		JPanel stock19 = new JPanel();
		JPanel stock20 = new JPanel();
		
		//adds panels to the ArrayList
		stockPanels.add(stock1);
		stockPanels.add(stock2);
		stockPanels.add(stock3);
		stockPanels.add(stock4);
		stockPanels.add(stock5);
		stockPanels.add(stock6);
		stockPanels.add(stock7);
		stockPanels.add(stock8);
		stockPanels.add(stock9);
		stockPanels.add(stock10);
		stockPanels.add(stock11);
		stockPanels.add(stock12);
		stockPanels.add(stock13);
		stockPanels.add(stock14);
		stockPanels.add(stock15);
		stockPanels.add(stock16);
		stockPanels.add(stock17);
		stockPanels.add(stock18);
		stockPanels.add(stock19);
		stockPanels.add(stock20);
		
		//enterStockPanel("Google", "GGL", 15);
		
	
	}
	
	//ActionListener for Enter and NewUser buttons, creates DialogBoxes
		@Override
		public void actionPerformed (ActionEvent e) {
			if(e.getSource()==butt) {
				cl.previous(cardPanel);
			}
		}
			
				
	/*void enterStockPanel(String name, String abr, int value) {
		
		stockPanels.set(i, )
		
	}*/
}

/*
public class HomeScreen2 {
	public class Research extends JPanel{
		TextField Ctf = TextField(30);
		JButton jb1 = new JButton("Research");
		//jb1.addActionListener(new ActionListener() {
			//public void actionPerformed(ActionEvent e) {
				
			//}
		//})
		public Research() {
			add(Ctf);
			add(jb1);
		}
	}
	
	public HomeScreen2() {
		add(new Research(), BorderLayout.North);
	}

	public static void main(String[] args) {
		HomeScreen2 frame = new HomeScreen2();
		frame.setSize(400,400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}*/

