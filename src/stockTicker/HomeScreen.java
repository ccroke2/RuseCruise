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
	
	private int frameSize;
	private String[] sortTypes = {"Name","Abbreviation","Current Value","Percent Change"};
	
	private JScrollPane resultsPane = new JScrollPane();
	private JScrollPane portPane = new JScrollPane();
	
	private JButton jbLogOut = new JButton("Log Out!");
	private JButton jbInfo = new JButton("More Info");
	private JButton jbSearch = new JButton("Search");
	private JButton jbTest = new JButton("TEST BUTTON");
	
	private JLabel jlbGreet = new JLabel("Welcome to your Ruse Cruise Stock Ticker");
	private JLabel jlbPort = new JLabel("This is where the portfolio will go");
	private JLabel jlbPortStock = new JLabel("This where the other stocks will go");
	private JLabel jlbSortPrompt = new JLabel("Sort Favorite Stocks by: ");
	private JComboBox combo;
	private JComboBox jcbxFavStockSort = new JComboBox(sortTypes);
	
	private JPanel pan = new JPanel();
	private JPanel sortPanel = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JPanel allStocks = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel holderPanel = new JPanel();
	private JPanel upperPanel = new JPanel();
	private JPanel functionsPanel = new JPanel();
	private JPanel favStocksPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	
	private ArrayList<JPanel> stockPanels = new ArrayList<JPanel>(20);
	
	LoadDriver ld = new LoadDriver();
	CardLayout cl;
	JPanel cardPanel;
	
	public HomeScreen (CardLayout clin, JPanel cardPanelin) {
		cl = clin;
		cardPanel = cardPanelin;
		setLayout(new BorderLayout());		
		
		//creates choices for search, creates search drop-down
		APIcall getList = new APIcall();
		combo = new JComboBox(getList.sFullAbr);
		combo.setEditable(true);
		AutoCompleteDecorator.decorate(combo);
		searchPanel.add(combo);
		searchPanel.add(jbSearch);
		jbSearch.addActionListener(this);
		
		//Prints out stocks from test_stocks.txt
		try {
			BufferedReader in =  new BufferedReader( 
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream("test_stocks.txt")));
		
		    String inputLine;
		    allStocks.setLayout(new GridLayout(0,1));
		    ArrayList<APIcall> stocks = new ArrayList<APIcall>();
		    ArrayList<String> sAbs = new ArrayList<String>();
		    ArrayList<JPanel> pnlList = new ArrayList<JPanel>();
			while ((inputLine = in.readLine()) != null) {
				JPanel stockNamesPnl = new JPanel();
				stockNamesPnl.setLayout(new FlowLayout());
				String stock = inputLine.substring(0, inputLine.indexOf("\t"));
				stockNamesPnl.add(new JLabel(stock));
				APIcall sCall = new APIcall(stock);
				sCall.setValues();
				stockNamesPnl.add(new JLabel(sCall.stockFullName));
				stocks.add(sCall);
				sAbs.add(stock);
				pnlList.add(stockNamesPnl);
			}
			APIcall x = new APIcall();
			for (int i = 0; i < stocks.size(); i++) {
				StockPanel spnl = new StockPanel(cl, cardPanel,stocks.get(i).stockName);
			    allStocks.add(spnl.getStockPanel(pnlList.get(i)));
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
		upperPanel.add(functionsPanel);
		
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
			if(e.getSource()==jbLogOut) {
				cl.previous(cardPanel);
			}
			if(e.getSource()==jbInfo) {
				cl.next(cardPanel);
			}
			if(e.getSource()==jbSearch) {
				String xName = (String)combo.getSelectedItem();
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

