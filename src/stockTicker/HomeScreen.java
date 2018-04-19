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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class HomeScreen extends JPanel implements ActionListener, ItemListener {
	
	private String[] sortTypes = {"Name","Abbreviation","Current Value","Percent Change"};
	
	private JScrollPane resultsPane = new JScrollPane();
	private JScrollPane portPane = new JScrollPane();
	
	private JButton jbLogOut = new JButton("Log Out!");
	private JButton jbInfo = new JButton("More Info");
	private JButton jbSearch = new JButton("Search");
	private JLabel jlbText = new JLabel("Welcome to your Ruse Cruise Stock Ticker");
	private JPanel pan = new JPanel();
	private JPanel search = new JPanel();
	private JPanel allStocks = new JPanel();
	private ArrayList<JPanel> stockPanels = new ArrayList<JPanel>(20);
	private JComboBox combo;
	private JButton refresh = new JButton("refresh");
	private Vector<String> all_ab_full = new Vector<String>();
	private int firstFull_indx;
	LoadDriver ld = new LoadDriver();
	private APIcall getList = new APIcall();
	private JButton jbTest = new JButton("TEST BUTTON");
	
	private JLabel jlbGreet = new JLabel("Welcome to your Ruse Cruise Stock Ticker");
	private JLabel jlbPort = new JLabel("This is where the portfolio will go");
	private JLabel jlbPortStock = new JLabel("This where the other stocks will go");
	private JLabel jlbSortPrompt = new JLabel("Sort Favorite Stocks by: ");
	private JComboBox jcbxFavStockSort = new JComboBox(sortTypes);
	
	private JPanel sortPanel = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel holderPanel = new JPanel();
	private JPanel upperPanel = new JPanel();
	private JPanel functionsPanel = new JPanel();
	private JPanel favStocksPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	
	CardLayout cl;
	JPanel cardPanel;
	
	public HomeScreen (CardLayout clin, JPanel cardPanelin) {
		cl = clin;
		cardPanel = cardPanelin;
		
		setLayout(new BorderLayout());
		
		
		getList.getAllNames();
		all_ab_full.addAll(getList.sFullAbr);
		all_ab_full.addAll(getList.sFull);
		firstFull_indx = getList.sFullAbr.size();
		combo = new JComboBox(all_ab_full);
		//creates choices for search, creates search drop-down
		combo.setEditable(true);
		Dimension d = new Dimension(100,20);
		combo.setPreferredSize(d);
		AutoCompleteDecorator.decorate(combo);

		searchPanel.add(combo);
		searchPanel.add(jbSearch);
		jbSearch.addActionListener(this);
		
		
		
		//scrollPane.setPreferredSize(new Dimension (500, 300));
		//scrollPane.add(butt);
		
		//Prints out stocks from test_stocks.txt
		try {
			BufferedReader in =  new BufferedReader( 
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream("test_stocks.txt")));
		
		    String inputLine;
		    allStocks.setLayout(new GridLayout(0,1));
		    ArrayList<APIcall> stocks = new ArrayList<APIcall>();
		    ArrayList<String> sAbs = new ArrayList<String>();
		    ArrayList<JPanel> pnlList = new ArrayList<JPanel>();
		    APIcall tempCall = new APIcall();
			while ((inputLine = in.readLine()) != null) {
		       String stock = inputLine.substring(0, inputLine.indexOf("\t"));
		       sAbs.add(stock);
			}
			APIcall x = new APIcall();
			stocks = x.batchValues(sAbs);
			for (int i = 0; i < stocks.size(); i++) {
				JPanel stockNamesPnl = new JPanel();
				stockNamesPnl.setLayout(new FlowLayout());
				stockNamesPnl.add(new JLabel(sAbs.get(i)));
				stockNamesPnl.add(new JLabel(stocks.get(i).stockFullName));
				StockPanel spnl = new StockPanel(cl, cardPanel,stocks.get(i));
			    allStocks.add(spnl.getStockPanel(stockNamesPnl));
			}
			
		    in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		favStocksPanel.setLayout(new GridLayout(0,1));
		//~~~~~~~
		favStocksPanel.add(jbTest);
		jbTest.addActionListener(this);
		
		for (int i=0; i<20; i++) {
			JPanel newPanel = new JPanel();
			JButton testButt = new JButton("Stock Name Here");
			newPanel.add(testButt);
			favStocksPanel.add(newPanel);
		}
		//~~~~~~~
		
		sortPanel.setLayout(new FlowLayout());
		jcbxFavStockSort.setBackground(Color.WHITE);
		jcbxFavStockSort.addItemListener(this);
		sortPanel.add(jlbSortPrompt);
		sortPanel.add(jcbxFavStockSort);
		functionsPanel.setLayout(new GridLayout(0,2));
		functionsPanel.add(sortPanel);
		functionsPanel.add(searchPanel);
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
		jlbGreet.setAlignmentX(Component.CENTER_ALIGNMENT);
		upperPanel.add(jlbGreet);
		upperPanel.add(refresh);
		upperPanel.add(functionsPanel);
		refresh.addActionListener(this);
		
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(allStocks, BorderLayout.CENTER);
		
		portPane.setPreferredSize(new Dimension(400,300));
		portPane.setViewportView(favStocksPanel);
		resultsPane.setPreferredSize(new Dimension (400, 300));
		resultsPane.setViewportView(allStocks);
		
		holderPanel.setLayout(new BoxLayout(holderPanel, BoxLayout.X_AXIS));
		holderPanel.add(portPane);
		holderPanel.add((Box.createRigidArea(new Dimension(10,0))));
		holderPanel.add(resultsPane);
		
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(jbLogOut);
		jbLogOut.addActionListener(this);
		
		add(upperPanel, BorderLayout.NORTH);
		add(holderPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

	}
	
	//ActionListener for Info, Search and Exit buttons, creates DialogBoxes
		@Override
		public void actionPerformed (ActionEvent e) {

			if(e.getSource() == refresh) {
				cl.previous(cardPanel);
				cl.previous(cardPanel);
				cardPanel.remove(3);
				cardPanel.remove(2);
				SplashScreen temp = new SplashScreen(cl, cardPanel, 1);
				cardPanel.add(temp, "splash");
				cl.next(cardPanel);
			}
			if(e.getSource()==jbLogOut) {
				cl.first(cardPanel);
			}
			if(e.getSource()==jbInfo) {
				cl.next(cardPanel);
			}
			if(e.getSource()==jbSearch) {
				String xName;
				if(combo.getSelectedIndex() >= firstFull_indx) {
					xName = getList.sFullAbr.get(combo.getSelectedIndex() - firstFull_indx);
				}
				else {
					xName = (String)combo.getSelectedItem();
				}
				cardPanel.add(new InfoScreen(cl, cardPanel, xName),xName);
				cl.show(cardPanel, xName);
			}
		}
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED) {
				if(e.getSource()==jcbxFavStockSort) {
					if(jcbxFavStockSort.getSelectedItem().equals("Name")) {
						System.out.println("NAME SORT SELECTED");
					}
					if(jcbxFavStockSort.getSelectedItem().equals("Abbreviation")) {
						System.out.println("ABBREVIATION SORT SELECTED");
					}
					if(jcbxFavStockSort.getSelectedItem().equals("Current Value")) {
						System.out.println("CURRENT VALUE SORT SELECTED");
					}
					if(jcbxFavStockSort.getSelectedItem().equals("Percent Change")) {
						System.out.println("PERCENT CHANGE SORT SELECTED");
					}
				}
			}
		}
			
}

