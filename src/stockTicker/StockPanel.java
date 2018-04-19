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
import java.text.DecimalFormat; 

public class StockPanel implements ActionListener{
	
	public String stockAb;
	
	private JPanel pnl = new JPanel();
	private JButton btn = new JButton("More Info");

	private APIcall sCall;
	
	CardLayout cl;
	JPanel cardPanel;
	
	//new comment
	
	public StockPanel(CardLayout clin, JPanel cardPanelin, String sA) {
		cl = clin;
		cardPanel = cardPanelin;
		stockAb = sA;
		sCall = new APIcall(sA);
		sCall.setValues();
	}
	
	public JPanel getStockPanel(JPanel snPnl) {
		pnl.setPreferredSize(new Dimension(175, 50));
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));

		perc = sCall.percent;
		String perc2 = String.format("%.2f",sCall.percent);
		pLbl = new JLabel(perc2+"%");
		cLbl = new JLabel(Double.toString(sCall.cPrice));
		pnl.setPreferredSize(new Dimension(640, 100));
		pnl.setLayout(new GridLayout(0,2));
		
		pnl.add(snPnl);

		percPnl.setLayout(new FlowLayout());
		
		try {
		ImageIcon icon;

		if(perc >= 0) {
			pLbl.setForeground(Color.GREEN);
			icon = createImageIcon("/Green_Arrow_Up_Darker.svg.png");
			picLabel = new JLabel(icon);
			percPnl.add(pLbl);
		}
		else {
			pLbl.setForeground(Color.RED);
			icon = createImageIcon("/2000px-Red_Arrow_Down.svg.png");
			picLabel = new JLabel(icon);
			percPnl.add(pLbl);
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

	public void actionPerformed (ActionEvent e) {
		if(e.getSource()==btn) {
			String xName = stockAb;
			cardPanel.add(new InfoScreen(cl, cardPanel, xName),xName);
			cl.show(cardPanel, xName);
		}
	}
}