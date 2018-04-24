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
	
	private String loadInfo = "Loading ...";
	private JLabel jlbLoad = new JLabel(loadInfo);
	private JLabel jlbWait = new JLabel("Please Wait");
	private JButton jbNext = new JButton("Next");
	
	
	
	public SplashScreen(CardLayout clin, JPanel cardPanelin, int backInfo) {
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		//jlbLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
		//add(jlbLoad, BorderLayout.CENTER);
		JPanel temp = new JPanel();
		temp.setLayout(new BorderLayout());
		jlbWait.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(jlbWait, BorderLayout.SOUTH);
		URL url = getClass().getResource("/loader.gif");
        Icon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon);
        temp.add(label, BorderLayout.CENTER);
		temp.setBackground(Color.WHITE);
		add(temp,BorderLayout.CENTER);
		
		final CardLayout cl = clin;
		final JPanel cardPanel = cardPanelin;
		System.out.println(backInfo);
	    
		if(backInfo == 1) {
			System.out.println("HERE");
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
				System.out.println("STUCK");
				TimeUnit.SECONDS.sleep(1);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			//SplashScreen splScreen = new SplashScreen(cl, cardPanel, 1);
			//cardPanel.add(splScreen, "splash");
			//cl.show(cardPanel,"splash");
		}
	}
	
	/*
	public void goToHome(CardLayout clin, JPanel cardPanelin) {
		cl = clin;
		cardPanel = cardPanelin;
		
		System.out.println("third");
		try {
    			TimeUnit.SECONDS.sleep(3);
	    }
	    catch(Exception e) {
	    		e.printStackTrace();
	    }
		
		HomeScreen mainScreen = new HomeScreen(cl, cardPanel);
        cardPanel.add(mainScreen, "main");
        cl.next(cardPanel);
	}*/
}
