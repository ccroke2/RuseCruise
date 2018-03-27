package stockTicker;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JPanel;

// ActionListener still needs HomeScreen to be completed.
public class LoginScreen extends JPanel {
	
	//private BufferedImage ruiseCruiseLogo = ImageIO.read(new ... );
	private JButton jbEnter = new JButton("Enter");
	private JButton jbNewUser = new JButton("New User?");
	private JLabel jlblSlogan = new JLabel("Pack your bags!", JLabel.CENTER);
	private JLabel jlblNewPass = new JLabel("Your Password: w3lcom3Aboard", JLabel.CENTER);
	private JLabel jlblVersion = new JLabel("Alpha v. 0.0.1");
	private JPasswordField jpfPassword = new JPasswordField(32);
		
	public void createFrame() {
	        
	    }
	 
	public LoginScreen() {
		this.setLayout(new BorderLayout(5, 10));
		this.add(jlblSlogan, BorderLayout.NORTH);
		this.add(jpfPassword, BorderLayout.WEST);
		this.add(jlblVersion, BorderLayout.SOUTH);
		
		JPanel jpLogin = new JPanel();
		jpLogin.setLayout(new GridLayout(2,1));
		jpLogin.add(jbEnter);
		jpLogin.add(jbNewUser);
		this.add(jpLogin, BorderLayout.EAST);	
		
		jbNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFrame();
			}
		});
		
		/*jbEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});*/
	}
	
}