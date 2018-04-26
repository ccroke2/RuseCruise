package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.net.URL;
import javax.swing.SwingWorker;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.TimeUnit;

public class SplashScreen extends JPanel {
	
	private JLabel jlbLoad = new JLabel("Loading ...");
	private JLabel jlbBlank = new JLabel ("  ");
	private JLabel jlbBlank2 = new JLabel ("  ");
	private JPanel textPanel = new JPanel();
	private Font labelFont = new Font("IMPACT", Font.ITALIC, 40);
	
	public SplashScreen(CardLayout clin, JPanel cardPanelin, int backInfo) {
		final CardLayout cl = clin;
		final JPanel cardPanel = cardPanelin;
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		JPanel temp = new JPanel();
		temp.setLayout(new BorderLayout());
		
		jlbLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
		jlbLoad.setAlignmentY(Component.TOP_ALIGNMENT);
		jlbLoad.setFont(labelFont);
		jlbBlank.setFont(labelFont);
		jlbBlank2.setFont(labelFont);
		textPanel.setBackground(Color.WHITE);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(jlbLoad);
		textPanel.add(jlbBlank);
		textPanel.add(jlbBlank2);
		add(textPanel, BorderLayout.SOUTH);

		URL url = getClass().getResource("/loader.gif");
        Icon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon);
        temp.add(label, BorderLayout.CENTER);
		temp.setBackground(Color.WHITE);
		add(temp,BorderLayout.CENTER);
	    
		if(backInfo == 1) {
			SwingWorker worker = new SwingWorker<Integer, Void>() {
			    @Override
			    public Integer doInBackground() {
	          		HomeScreen mainScreen = new HomeScreen(cl, cardPanel);
	                  cardPanel.add(mainScreen, "main");
	                  cl.next(cardPanel);
	                  return 1;
			    }
	
			    @Override
			    public void done() {
			   
			    }
			};
			worker.execute();
		}
		else {
			try {
				TimeUnit.SECONDS.sleep(1);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}