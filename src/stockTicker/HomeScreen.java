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

//import stockTicker.HomeScreen2.Research;


public class HomeScreen extends JPanel implements ActionListener {
	
	private JScrollPane scrollPane = new JScrollPane();
	private JButton jbButt = new JButton("Log Out!");
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
	
	CardLayout cl;
	JPanel cardPanel;
	
	private JPanel tempPanel = new JPanel();
	
	public HomeScreen (CardLayout clin, JPanel cardPanelin) {
		cl = clin;
		cardPanel = cardPanelin;
		
		setLayout(new BorderLayout());
		add(tempPanel, BorderLayout.SOUTH);
		tempPanel.setLayout(new GridLayout(1,2));
		
		//
		tempPanel.add(jbButt);
		jbButt.addActionListener(this);
		
		
		getList.getAllNames();
		all_ab_full.addAll(getList.sFullAbr);
		all_ab_full.addAll(getList.sFull);
		System.out.println(getList.sFull);
		firstFull_indx = getList.sFullAbr.size();
		combo = new JComboBox(all_ab_full);
		combo.setEditable(true);
		Dimension d = new Dimension(100,20);
		combo.setPreferredSize(d);
		AutoCompleteDecorator.decorate(combo);
		
		search.add(combo);
		search.add(jbSearch);
		search.add(refresh);
		refresh.addActionListener(this);
		jbSearch.addActionListener(this);
		
		
		
		scrollPane.setPreferredSize(new Dimension (500, 300));
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
			System.out.println("HERE is BATCH");
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
		
		
		scrollPane.setViewportView(allStocks);
		add(scrollPane, BorderLayout.CENTER);
		jlbText.setHorizontalAlignment(JLabel.CENTER);
		add(search, BorderLayout.NORTH);

		//add(pan);
		
		/*
		 add(butt, BorderLayout.SOUTH);
		butt.addActionListener(this);
		add(jbInfo, BorderLayout.SOUTH);
		jbInfo.addActionListener(this);
		*/
		
	
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
			if(e.getSource()==jbButt) {
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
				//cl.next(cardPanel);
				//ld.readData();
				//ld.createFrame();
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

