package stockTicker;

import java.awt.*;
import java.awt.event.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
import javax.swing.*;

public class LoginScreen extends JPanel {
	
	//private BufferedImage ruiseCruiseLogo = ImageIO.read(new ... );
	private JButton enter = new JButton("Enter");
	private JButton newUser = new JButton("New User?");
	private JLabel slogan = new JLabel("pack your bags!");
	private JLabel disclaimer = new JLabel("ALPHA");
	private JPasswordField password = new JPasswordField(25);
		
	public LoginScreen() {
		setLayout(new FlowLayout());
		FlowLayout centered = new FlowLayout();
		centered.setAlignment(FlowLayout.CENTER);
		
		add(slogan);
		add(password);
		add(enter);
		add(newUser);
		add(disclaimer);
	
	
	}
	
	
}
