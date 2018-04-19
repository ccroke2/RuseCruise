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
	private JButton btn = new JButton("More Info");

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
		pnl.setPreferredSize(new Dimension(175, 50));
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
		pnl.add(snPnl);
		pnl.add(btn);
		btn.addActionListener(this);
		pnl.setBorder(new LineBorder(Color.BLACK, 2));
		
		return pnl;
	}

	public void actionPerformed (ActionEvent e) {
		if(e.getSource()==btn) {
			System.out.println("clicked");
			String xName = stockAb;
			cardPanel.add(new InfoScreen(cl, cardPanel, xName),xName);
			cl.show(cardPanel, xName);
		}
	}
}