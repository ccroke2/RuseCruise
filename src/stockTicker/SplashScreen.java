package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class SplashScreen extends JPanel implements ActionListener {
	
	private String loadInfo = "Loading ...";
	private JLabel jlbLoad = new JLabel(loadInfo);
	private JLabel jlbWait = new JLabel("Please Wait");
	private JButton jbNext = new JButton("Next");
	
	JPanel cardPanel;
	CardLayout cl;
	
	public SplashScreen(CardLayout clin, JPanel cardPanelin) {
		cl = clin;
		cardPanel = cardPanelin;
		
		setLayout(new BorderLayout());
		
		jlbLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(jlbLoad, BorderLayout.CENTER);
		jlbWait.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(jlbWait, BorderLayout.EAST);
		jbNext.setAlignmentX(Component.CENTER_ALIGNMENT);
		jbNext.addActionListener(this);
		add(jbNext, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed (ActionEvent e) {
		if(e.getSource() == jbNext)
					cl.next(cardPanel);
			} 
}
