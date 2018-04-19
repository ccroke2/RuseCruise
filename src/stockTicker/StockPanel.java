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
	
	public String stockAb;
	
	private JPanel pnl = new JPanel();
	private JPanel percPnl = new JPanel();
	private JLabel pLbl;
	private JLabel cLbl;
	private JLabel picLabel;
	private JButton btn = new JButton("More Info");
	
	private double currentPrice;
	private double perc;

	private APIcall sCall;
	
	CardLayout cl;
	JPanel cardPanel;
	
	public StockPanel(CardLayout clin, JPanel cardPanelin, APIcall sCallin) {
		cl = clin;
		cardPanel = cardPanelin;
		
		sCall = sCallin;
		stockAb = sCall.stockName;
	}
	
	public JPanel getStockPanel(JPanel snPnl) {
		
		perc = sCall.percent;
		pLbl = new JLabel(Double.toString(sCall.percent));
		cLbl = new JLabel(Double.toString(sCall.cPrice));
		pnl.setPreferredSize(new Dimension(640, 100));
		pnl.setLayout(new GridLayout(0,2));
		pnl.add(snPnl);
		percPnl.setLayout(new FlowLayout());
		percPnl.add(pLbl);
		try {
		ImageIcon icon;

		if(perc >= 0) {
			icon = createImageIcon("/Green_Arrow_Up_Darker.svg.png");
			picLabel = new JLabel(icon);
		}
		else {
			icon = createImageIcon("/2000px-Red_Arrow_Down.svg.png");
			picLabel = new JLabel(icon);
		}
		percPnl.add(picLabel);
		pnl.add(percPnl);}
		catch(Exception e){
			e.printStackTrace();
		}
		pnl.add(cLbl);
		pnl.add(btn);
		btn.addActionListener(this);
		pnl.setBorder(new LineBorder(Color.BLACK, 2));
		
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
		if(e.getSource()==btn) {
			System.out.println("clicked");
			String xName = stockAb;
			cardPanel.add(new InfoScreen(cl, cardPanel, xName),xName);
			cl.show(cardPanel, xName);
			//cl.next(cardPanel);
			//ld.readData();
			//ld.createFrame();
		}
	}
}