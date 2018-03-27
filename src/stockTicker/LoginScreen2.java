package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class LoginScreen2 extends JPanel implements ActionListener {
	
	JPanel panel = new JPanel() {
		@Override
		protected void paintComponent (Graphics g) {
			try {
				BufferedImage ruseCruiseLogo = ImageIO.read(new File("C:\\Users\\kaitl\\Desktop\\ruseCruiseLogo.png"));
				g.drawImage(ruseCruiseLogo, 25, 5, 220, 200, null);
			}
			catch(Exception e) {
				System.out.println("Bad read");
				System.out.println(e);
			}
		}
	};
	
	private JButton jbEnter   = new JButton("Enter");
	private JButton jbNewUser = new JButton("New User?");
	private JLabel jlbSlogan  = new JLabel("pack your bags!");
	private JLabel jlbDisclaimer  = new JLabel("ALPHA 0.0.1");
	private JLabel jlbPassPrompt  = new JLabel("Enter the password:");
	private JPasswordField passwordField = new JPasswordField(25);
	
	private char[] masterPassword = {'w','3','l','c','o','m','3','A','b','o','a','r','d','!'};
	private char[] inputPassword;
	private String displayPassword = "w3lcom3Aboard!";
		
	
	
	public LoginScreen2() {
		
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new LineBorder(Color.BLACK, 5));
		add(panel);
		
		//setLayout(new BorderLayout());
		
		//sets the alignment of all the components to be centered
		//jlbSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);
		jbEnter.setAlignmentX(Component.CENTER_ALIGNMENT);
		jbNewUser.setAlignmentX(Component.CENTER_ALIGNMENT);
		jlbPassPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		jlbDisclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Adds components to panel
		panel.add(Box.createRigidArea(new Dimension(0,220)));
		panel.add(jlbPassPrompt);
		panel.add(Box.createRigidArea(new Dimension(0,20)));
		panel.add(passwordField);
		panel.add(Box.createRigidArea(new Dimension(0,8)));
		panel.add(jbEnter);
		jbEnter.addActionListener(this);
		panel.add(Box.createRigidArea(new Dimension(0,50)));
		panel.add(jbNewUser);
		jbNewUser.addActionListener(this);
		panel.add(Box.createRigidArea(new Dimension(0,50)));
		panel.add(jlbDisclaimer);
		
	}
	
	//ActionListener for Enter and NewUser buttons, creates DialogBoxes
	@Override
	public void actionPerformed (ActionEvent e) {
		if(e.getSource()==jbEnter) {
			inputPassword = passwordField.getPassword();
			if(Arrays.equals(inputPassword, masterPassword)) {
				System.out.println("That's the right one!");
				StockTickerMain mainClass = new StockTickerMain();
				mainClass.setCard("home");
			} else {
				JOptionPane.showMessageDialog(null,
						"That password is incorrect. Please try again",
						"Incorrect Password", JOptionPane.WARNING_MESSAGE);
			}
			Arrays.fill(inputPassword,'0');
		}
		else if(e.getSource()== jbNewUser) {
			JOptionPane.showMessageDialog(null, "Your password is "+displayPassword,
					"New User Password", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	
	
}
