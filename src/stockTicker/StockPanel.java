package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.net.URL;
import javax.swing.ImageIcon;

public class StockPanel implements ActionListener{
	
	private int numO;
	private double perc;
	private String full;
	public String stockAb;
	
	private JButton btn = new JButton("More");
	private JButton btn2 = new JButton("More");
	
	private JLabel pLbl;
 	private JLabel cLbl;
 	private JLabel picLabel;
	private JPanel pnl = new JPanel();
	private JPanel pnl2 = new JPanel();
	private JPanel percPnl = new JPanel();
	
	private APIcall sCall;
	
	CardLayout cl;
	JPanel cardPanel;
	
	public StockPanel(CardLayout clin, JPanel cardPanelin, APIcall sCallin) {
		cl = clin;
		cardPanel = cardPanelin;

		sCall = sCallin;
		stockAb = sCall.stockName;
		numO = sCall.numOwned;
	}
	
	public StockPanel(CardLayout clin, JPanel cardPanelin, String abin, String fullin) {
		cl = clin;
		cardPanel = cardPanelin;
		
		stockAb = abin;
		full = fullin;
		if(full.length() > 40) {
			full = full.substring(0,37);
			full = full + "...";
		}
	}
	
	public JPanel getStockPanel(int w) {
		pnl2.setPreferredSize(new Dimension(w, 50));
		pnl2.setLayout(new BoxLayout(pnl2, BoxLayout.X_AXIS));
		JPanel temp = new JPanel();
		temp.setLayout(new GridLayout(2,1));
		JLabel nmA = new JLabel(stockAb);
		JLabel nmF = new JLabel(full);
		temp.add(nmA);
		temp.add(nmF);
		temp.setBackground(Color.decode("#F4F4F4"));
		pnl2.add(temp);
		pnl2.add(btn2);
		btn2.addActionListener(this);
		pnl2.setBorder(new LineBorder(Color.BLACK, 2));
		pnl2.setBackground(Color.decode("#F4F4F4"));
		
		return pnl2;
	}
	
	public JPanel getStockPanel_info(JPanel infoPnl) {
		perc = sCall.percent;
		cLbl = new JLabel(Double.toString(sCall.cPrice));
		String perc1 = String.format("%.2f",perc);
		perc1 = perc1 + "%";
 		pLbl = new JLabel(perc1);
		pnl.setPreferredSize(new Dimension(175, 50));
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
		pnl.add(infoPnl);
		btn.addActionListener(this);
		pnl.setBorder(new LineBorder(Color.BLACK, 2));
		
		try {
	 		ImageIcon icon;
	 
	 		if(perc >= 0) {
	 			pLbl.setForeground(Color.GREEN);
	 			icon = createImageIcon("/Green_Arrow_Up_Darker.svg.png");
	 			picLabel = new JLabel(icon);
	 		}
	 		else {
	 			pLbl.setForeground(Color.RED);
	 			icon = createImageIcon("/2000px-Red_Arrow_Down.svg.png");
	 			picLabel = new JLabel(icon);
	 		}
	 		pnl.add(cLbl);
	 		percPnl.add(pLbl);
	 		percPnl.add(picLabel);
	 		percPnl.setBackground(Color.decode("#F4F4F4"));
	 		pnl.add(percPnl);}
	 		catch(Exception e){
	 			e.printStackTrace();
	 		}
	 		pnl.add(btn);
		
	 		pnl.setBackground(Color.decode("#F4F4F4"));
		return pnl;
	}
	
	private ImageIcon createImageIcon(String path) {
 		java.net.URL imgURL = getClass().getResource(path);
 		if (imgURL != null) {
 			return new ImageIcon(imgURL);
 		}
 		else {
 			System.err.println("Couldn't find file: " + path);
 			return null;
 		}
 	}

	public void actionPerformed (ActionEvent e) {
		if(e.getSource()==btn || e.getSource()==btn2) {
			System.out.println("clicked");
			String xName = stockAb;
			try {
			cardPanel.add(new InfoScreen(cl, cardPanel, xName, numO),xName);
			cl.show(cardPanel, xName);
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Could not retrieve information for " + xName + ".",
						"No Stock Info", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}