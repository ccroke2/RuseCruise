package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class HomeScreen extends JPanel implements ActionListener, ItemListener {
	
	private String[] sortTypes = {"Abbreviation","Name","Current Value","Percent Change"};
	
	private JScrollPane resultsPane = new JScrollPane();
	private JScrollPane portPane = new JScrollPane();
	
	private JButton jbLogOut = new JButton("Log Out!");
	private JButton jbInfo = new JButton("More Info");
	private JButton jbSearch = new JButton("Search");
	private JLabel jlbText = new JLabel("Welcome to your Ruse Cruise Stock Ticker");
	private JLabel jlbEmptyFile = new JLabel("No Data Available.");
	private JPanel pan = new JPanel();
	private JPanel search = new JPanel();
	private JPanel allStocks = new JPanel();
	private ArrayList<JPanel> stockPanels = new ArrayList<JPanel>(20);
	private JTextField txtSearch = new JTextField(20);
	private JButton refresh = new JButton("Refresh");
	private Vector<String> all_ab_full = new Vector<String>();
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
	
	private ArrayList<APIcall> stocks = new ArrayList<APIcall>();
	private ArrayList<APIcall> tempSort = new ArrayList<APIcall>();
	
	private ArrayList<JPanel> staticSort = new ArrayList<JPanel>(); //BASE SORT
 	private ArrayList<JPanel> pnlList = new ArrayList<JPanel>(); //Abr sort
	private ArrayList<JPanel> stocks_name = new ArrayList<JPanel>(); //Name sort
	private ArrayList<JPanel> stocks_cVal = new ArrayList<JPanel>(); //current value sort
	private ArrayList<JPanel> stocks_perc = new ArrayList<JPanel>(); //percent sort
	
	private int sortIndx = 0;
	
	CardLayout cl;
	JPanel cardPanel;
	
	public HomeScreen (CardLayout clin, JPanel cardPanelin) {
		cl = clin;
		cardPanel = cardPanelin;
		
		setLayout(new BorderLayout());
		
		
		getList.getAllNames();
		all_ab_full.addAll(getList.sFullAbr);
		all_ab_full.addAll(getList.sFull);

		searchPanel.add(txtSearch);
		txtSearch.addActionListener(this);
		searchPanel.add(jbSearch);
		jbSearch.addActionListener(this);
		
		//scrollPane.setPreferredSize(new Dimension (500, 300));
		//scrollPane.add(butt);
		
		//Prints out stocks from test_stocks.txt
		try {
			BufferedReader in =  new BufferedReader(new FileReader("portfolio.txt"));
		
		    String inputLine;
		    allStocks.setLayout(new GridLayout(0,1));
		    favStocksPanel.setLayout(new GridLayout(0,1));
		    ArrayList<String> sAbs = new ArrayList<String>();
		    ArrayList<Integer> numOwned_ary = new ArrayList<Integer>();
		    APIcall tempCall = new APIcall();
		    String tempStr;
			if(sortIndx == 0) {
				while ((inputLine = in.readLine()) != null) {
					String stock = inputLine.substring(0, inputLine.indexOf("\t")); 
					int numOwn = Integer.parseInt(inputLine.substring(inputLine.indexOf("\t")+1));
					sAbs.add(stock);
					numOwned_ary.add(numOwn);
				}
			}
			APIcall x = new APIcall();
			if(sortIndx == 0) {
				stocks = x.batchValues(sAbs,numOwned_ary);
				tempSort = stocks;
			}
			String fullNameTemp;
			for (int i = 0; i < stocks.size(); i++) {
				JPanel stockNamesPnl = new JPanel();
				stockNamesPnl.setLayout(new GridLayout(2,1));
				stockNamesPnl.add(new JLabel(sAbs.get(i)));
				fullNameTemp = stocks.get(i).stockFullName;
				if(fullNameTemp.length() > 40) {
					fullNameTemp = fullNameTemp.substring(0,37);
					fullNameTemp = fullNameTemp + "...";
				}
				stockNamesPnl.add(new JLabel(fullNameTemp));
				StockPanel spnl = new StockPanel(cl, cardPanel,stocks.get(i));
			    //allStocks.add(spnl.getStockPanel());
			    JPanel favStockPnl = spnl.getStockPanel_info(stockNamesPnl);
			    favStocksPanel.add(favStockPnl);
			    pnlList.add(favStockPnl);
			    stocks_name.add(favStockPnl);
			    stocks_cVal.add(favStockPnl);
			    stocks_perc.add(favStockPnl);
			    staticSort.add(favStockPnl);
			}
			
		    in.close();
		} catch (IOException e) {
			favStocksPanel.add(jlbEmptyFile);
			e.printStackTrace();
		}
		
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
		buttonPanel.add(refresh);
		buttonPanel.add(jbLogOut);
		refresh.addActionListener(this);
		jbLogOut.addActionListener(this);
		
		add(upperPanel, BorderLayout.NORTH);
		add(holderPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

	}
	
	//////////////////// Sorting Methods \\\\\\\\\\\\\\\\\\\\
	/////// Based on the stocks array, sort the panels \\\\\\\
	private void abSort() {
		JPanel temp;
		APIcall tempAPI;
		int j;
		pnlList = staticSort;
		for (int i = 1; i < stocks.size(); i++) {
			j = i;
			while(j > 0 && (tempSort.get(j).stockName).compareTo(tempSort.get(j-1).stockName) < 0) {
				temp = pnlList.get(j);
				pnlList.set(j, pnlList.get(j-1));
				pnlList.set(j-1, temp);
				
				tempAPI = tempSort.get(j);
				tempSort.set(j, tempSort.get(j-1));
				tempSort.set(j-1, tempAPI);
				
				j = j-1;
			}
		}
		sortIndx = 0;
		tempSort = stocks;
	}
	
	private void nameSort() {
		JPanel temp;
		APIcall tempAPI;
		int j;
		stocks_name = staticSort;
		for (int i = 1; i < stocks.size(); i++) {
			j = i;
			while(j > 0 && (tempSort.get(j).stockFullName.toUpperCase()).compareTo(tempSort.get(j-1).stockFullName.toUpperCase()) < 0) {
				temp = stocks_name.get(j);
				stocks_name.set(j, stocks_name.get(j-1));
				stocks_name.set(j -1 , temp);
				
				tempAPI = tempSort.get(j);
				tempSort.set(j, tempSort.get(j-1));
				tempSort.set(j-1, tempAPI);
				
				System.out.println(tempSort.get(j).stockFullName);
				System.out.println(tempSort.get(j-1).stockFullName);
				System.out.println((tempSort.get(j).stockFullName).compareTo(tempSort.get(j-1).stockFullName));
				
				j = j-1;
			}
		}
		sortIndx = 1;
		tempSort = stocks;
	}
	
	private void currentSort() {
		JPanel temp;
		APIcall tempAPI;
		int j;
		stocks_cVal = staticSort;
		for (int i = 1; i < stocks.size(); i++) {
			j = i;
			while(j > 0 && tempSort.get(j).cPrice < tempSort.get(j-1).cPrice) {
				temp = stocks_cVal.get(j);
				stocks_cVal.set(j, stocks_cVal.get(j-1));
				stocks_cVal.set(j-1, temp);
				
				tempAPI = tempSort.get(j);
				tempSort.set(j, tempSort.get(j-1));
				tempSort.set(j-1, tempAPI);
				
				j = j-1;
			}
		}
		sortIndx = 2;
		tempSort = stocks;
	}
	
	private void percSort() {
		JPanel temp;
		APIcall tempAPI;
		int j;
		stocks_perc = staticSort;
		for (int i = 1; i < stocks.size(); i++) {
			j = i;
			while(j > 0 && tempSort.get(j).percent < tempSort.get(j-1).percent) {
				temp = stocks_perc.get(j);
				stocks_perc.set(j, stocks_perc.get(j-1));
				stocks_perc.set(j-1, temp);
				
				tempAPI = tempSort.get(j);
				tempSort.set(j, tempSort.get(j-1));
				tempSort.set(j-1, tempAPI);
				
				j = j-1;
			}
		}
		sortIndx = 3;
		tempSort = stocks;
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
				cl.show(cardPanel, "splash");
			}
			if(e.getSource()==jbLogOut) {
				cl.first(cardPanel);
			}
			if(e.getSource()==jbInfo) {
				cl.next(cardPanel);
			}
			if(e.getSource()==jbSearch || e.getSource()==txtSearch) {
				String xName;
				JPanel searchStocks = new JPanel();
				searchStocks.setLayout(new GridLayout(0,1));
				xName = txtSearch.getText().toUpperCase();
				int indx;
				
				for (int i = 0; i < all_ab_full.size(); i++) {
					indx = i%getList.sFullAbr.size();
					
					if(all_ab_full.get(i).toUpperCase().contains(xName)) {
						StockPanel spnl = new StockPanel(cl, cardPanel,getList.sFullAbr.get(indx), getList.sFull.get(indx));
						JPanel temp = spnl.getStockPanel();
						searchStocks.add(temp);
					}
				}
				resultsPane.setViewportView(searchStocks);
			}
		}
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange()==ItemEvent.SELECTED) {
				if(e.getSource()==jcbxFavStockSort) {
					JPanel n = new JPanel();
					JPanel a = new JPanel();
					JPanel c = new JPanel();
					JPanel p = new JPanel();
					n.setLayout(new GridLayout(0,1));
					a.setLayout(new GridLayout(0,1));
					c.setLayout(new GridLayout(0,1));
					p.setLayout(new GridLayout(0,1));
					if(jcbxFavStockSort.getSelectedItem().equals("Name")) {
						System.out.println("NAME SORT SELECTED");
						nameSort();
						for (int i = 0; i < stocks_name.size(); i++) {
							n.add(stocks_name.get(i));
						}
						portPane.setViewportView(n);
					}
					if(jcbxFavStockSort.getSelectedItem().equals("Abbreviation")) {
						System.out.println("ABBREVIATION SORT SELECTED");
						abSort();
						for (int i = 0; i < pnlList.size(); i++) {
							a.add(pnlList.get(i));
						}
						portPane.setViewportView(a);
					}
					if(jcbxFavStockSort.getSelectedItem().equals("Current Value")) {
						System.out.println("CURRENT VALUE SORT SELECTED");
						currentSort();
						for (int i = 0; i < stocks_cVal.size(); i++) {
							c.add(stocks_cVal.get(i));
						}
						portPane.setViewportView(c);
					}
					if(jcbxFavStockSort.getSelectedItem().equals("Percent Change")) {
						System.out.println("PERCENT CHANGE SORT SELECTED");
						percSort();
						for (int i = 0; i < stocks_perc.size(); i++) {
							p.add(stocks_perc.get(i));
						}
						portPane.setViewportView(p);
					}	
				}
			}
		}
			
}

