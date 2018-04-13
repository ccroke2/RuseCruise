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
	
	//key 0=new stock, 1=new high/low/open/close, 2=new current price, 3 = new stocks owned
	//insertInfo = number of stocks owned now (can be used for other info if needed
	//stockName is the abbreviation
	public void API_insert(String stockName, int key, String insertInfo) {
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } 
	 catch (Exception ex) {
           ex.printStackTrace();
        }
	 
	 Connection conn = null;
	 if (insertInfo == null) {
		 insertInfo = "1";
	 }
	 try {
		 conn = DriverManager.getConnection(DATABASE_URL, getProperties());

	     // Do something with the Connection
	     Statement stmt = null;
	     ResultSet rs = null;

	     try {/*
	         stmt = conn.createStatement();
	         APIcall newCall = new APIcall();
	         ArrayList<String> APIres = new ArrayList<String>();
	         APIres = newCall.returnAPI_single(stockName, new Date());
	         double curPrice = newCall.singleCurrentPrice(stockName);
	         //insert all info (new stock added)
	         if(key == 0) {
	        	 	rs = stmt.executeQuery("INSERT INTO portfolio (stockabrev, stockprice, stockhigh, stocklow, stockopen, stockclose, stockupdate, stocksowned)" + 
	        	 			"VALUES ("+ stockName + "," + curPrice + "," + Double.parseDouble(APIres.get(1)) + "," + Double.parseDouble(APIres.get(2)) + "," + Double.parseDouble(APIres.get(0)) + "," + Double.parseDouble(APIres.get(3)) + "," + new Date(System.currentTimeMillis()) + "," + insertInfo + ");");
	        	 	
	         }
	         //insert new open/close/high/low for current stock
	         else if(key == 1) {
	        	 	rs = stmt.executeQuery("UPDATE portfolio" + 
	        	 			"SET stockhigh =" + Double.parseDouble(APIres.get(1)) + ", stocklow=" + Double.parseDouble(APIres.get(2))+", stockopen=" + Double.parseDouble(APIres.get(0)) + ", stockclose="+ Double.parseDouble(APIres.get(3)) + 
	        	 			"WHERE stockabrev =" + stockName +";");
	         }
	         //insert new current values for current stock
	         else if(key == 2) {
	        	 	rs = stmt.executeQuery("UPDATE portfolio" + 
	        	 			"SET stockprice = " + curPrice +
	        	 			"WHERE stockabrev =" + stockName +";");
	         }
	         //insert new amount owned of current stock
	         else if (key == 3) {
	        	 	rs = stmt.executeQuery("UPDATE portfolio" + 
	        	 			"SET stocksowned = " + Integer.parseInt(insertInfo) +
	        	 			"WHERE stockabrev =" + stockName +";");
	         } 
	         */   
	     }
	     catch (Exception ex) {
	         // handle any errors
	         //System.out.println("SQLException: " + ex.getMessage());
	         //System.out.println("SQLState: " + ex.getSQLState());
	         //System.out.println("VendorError: " + ex.getErrorCode());
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
	
	//get
	public double getInfo(String stockName, int key) {
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

	     try {/*
	         stmt = conn.createStatement();
	         APIcall newCall = new APIcall();
	         ArrayList<String> APIres = new ArrayList<String>();
	         APIres = newCall.returnAPI_single(stockName, new Date());
	         double curPrice = newCall.singleCurrentPrice(stockName);
	         //return high
	         if(key == 0) {
	        	 	rs = stmt.executeQuery("SELECT stockhigh FROM portfolio WHERE stockabrev = " + stockName + ";");
	         }
	         //return low
	         else if(key == 1) {
	        	 	rs = stmt.executeQuery("SELECT stocklow FROM portfolio WHERE stockabrev = " + stockName + ";");
	         }
	         //return close
	         else if(key == 2) {
	        	 	rs = stmt.executeQuery("SELECT stockclose FROM portfolio WHERE stockabrev = " + stockName + ";");
	         }
	         //return open
	         else if (key == 3) {
	        	 	rs = stmt.executeQuery("SELECT stockopen FROM portfolio WHERE stockabrev = " + stockName + ";");
	         } 
	         //return current
	         else if (key == 4) {
	        	 	rs = stmt.executeQuery("SELECT stockprice FROM portfolio WHERE stockabrev = " + stockName + ";");
	         }
	         //return number owned
	         else if (key == 5) {
	        	 	rs = stmt.executeQuery("SELECT stocksowned FROM portfolio WHERE stockabrev = " + stockName + ";");
	         }
	        return Double.parseDouble(rs.toString());*/
	     }
	     catch (Exception ex) {
	         // handle any errors
	         //System.out.println("SQLException: " + ex.getMessage());
	         //System.out.println("SQLState: " + ex.getSQLState());
	         //System.out.println("VendorError: " + ex.getErrorCode());
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
	 return -100000000;
	}

}
