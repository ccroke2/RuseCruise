package stockTicker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.awt.*;
import javax.swing.*;

import java.util.*;

public class LoadDriver {
	
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/stockTicker";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sesame";
    private static final String MAX_POOL = "250";
    private static Properties properties;

	static int columnNumber = 0;
	static int rowNumber = 0;
	static JPanel stockPanel = new JPanel();
	private static JTextArea jtaResults = new JTextArea(50, 25);
	
	private static Properties getProperties() 
    {
        if (properties == null) // add in credentials to enter database
        {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }
	
	 public void createFrame()
	    {
	        EventQueue.invokeLater(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                JFrame frame = new JFrame("Results"); // this window will show the results of a query
	                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                
	                JPanel panel = new JPanel();
	                jtaResults.setLineWrap(true);
	                jtaResults.setWrapStyleWord(true);
	                jtaResults.setEditable(false);
	                JScrollPane jspScroller = new JScrollPane(jtaResults);
	                jspScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	                jspScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	                panel.add(jspScroller);
	                
	                frame.add(BorderLayout.CENTER, panel);
	                frame.pack();
	                frame.setLocationRelativeTo(null);
	                frame.setVisible(true);
	                frame.setResizable(false);
	            }
	        });
	    }
	    
	public void readData() {
		 try {
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	        } 
		 catch (Exception ex) {
	           ex.printStackTrace();
	        }
		 
		 Connection conn = null;
		 
		 try {
			 conn = DriverManager.getConnection(DATABASE_URL, getProperties());

		     // Do something with the Connection
		     Statement stmt = null;
		     ResultSet rs = null;

		     try {
		         stmt = conn.createStatement();
		         //change the query to whatever you need
		         rs = stmt.executeQuery("SELECT * FROM stockNames");
		         // Now do something with the ResultSet ....
		         ResultSetMetaData rsmd = rs.getMetaData();
		         columnNumber = rsmd.getColumnCount();
		            
		            while (rs.next()) // while there are still rows available...
		            {
		                for (int i = 1; i <= columnNumber; i++) 
		                {
		                    String columnValue = rs.getString(i); // ... find the contents of column i...
		                    jtaResults.append(columnValue); // ... and add said contents to the results frame
		                    jtaResults.append(" ");
		                }
		                jtaResults.append("\n");
		            }
		            
		         //return jtaResults; //could change this so that it can be integrated into the home screen  
		            
		            
		     }
		     catch (SQLException ex) {
		         // handle any errors
		         System.out.println("SQLException: " + ex.getMessage());
		         System.out.println("SQLState: " + ex.getSQLState());
		         System.out.println("VendorError: " + ex.getErrorCode());
		     }
		     finally {
		         // release resources in reverse-order of their creation if they are no-longer needed

		         if (rs != null) {
		             try {
		                 rs.close();
		             } 
		             catch (SQLException sqlEx) {} // ignore

		             rs = null;
		         }

		         if (stmt != null) {
		             try {
		                 stmt.close();
		             } 
		             catch (SQLException sqlEx) {} // ignore

		             stmt = null;
		         }
		     } 
		 }
		     catch (SQLException ex) {
		     // handle any errors
		     System.out.println("SQLException: " + ex.getMessage());
		     System.out.println("SQLState: " + ex.getSQLState());
		     System.out.println("VendorError: " + ex.getErrorCode());
		 }
	}
}
