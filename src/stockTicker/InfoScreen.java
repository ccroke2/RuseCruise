package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.panel.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.*;
import org.jfree.data.time.*;
import org.jfree.data.time.ohlc.*;
import org.jfree.data.xy.*;
import org.jfree.ui.*;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.DateAxis;

import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import java.net.URL;

import java.io.*;

//ChartMouseListener
public class InfoScreen extends JPanel implements ActionListener, ChartMouseListener, ItemListener{

	CardLayout cl;
	JPanel cardPanel;
	
	private int stockNum = 0;
	private int initialStockNum = 0;
	private int addIn = 0;
	
	private String stockName;
	private String stockAbr;
	private double stockCurPrice;
	private String stockValue;
	private double stockValueNum;
	private double stockReturn;
	private String stockOpen;
	private String stockHigh;
	private String stockLow;
	private String stockClose;
	private String stockPerc;
	private String timePeriod   = "Day";
	private String timePrompt	= "Historical Data for the Last: ";
	private String[] timeLabels = {"Day","Month","Year","5 Years", "Year to Date"};
	
	ArrayList<String> favStocks = new ArrayList<String>();
	
	private JLabel jlbStockName;
	private JLabel jlbStockAbr;
	private JLabel jlbValue;
	private JLabel jlbReturn;
	private JLabel jlbOpen;
	private JLabel jlbHigh;
	private JLabel jlbLow;
	private JLabel jlbClose;
	private JLabel jlbPerc;
	private JLabel jlbTimePrompt;
	private JLabel[] blank = new JLabel[10];
	
	
	private JButton jbBack   = new JButton("Back");
	private JButton jbAdd   = new JButton("Add/Update Stock");
	private JButton jbDelete = new JButton("Delete");
	private JButton jbStockDown = new JButton("-");
	private JButton jbStockUp	= new JButton("+");
	
	private JTextField jtfStockNum;
	private JComboBox<String> jcbxTimePeriod = new JComboBox(timeLabels);
	
	private JPanel stockInfoPanel   = new JPanel();
	private JPanel stockNumPanel    = new JPanel();
	private JPanel stockStatusPanel = new JPanel();
	private JPanel shellChartPanel  = new JPanel();
	private JPanel timeDropPanel    = new JPanel();
	private JPanel buttonPanel      = new JPanel();

	private ChartPanel chartPanel;
	private Crosshair xCrosshair;
	private Crosshair yCrosshair;
	private Font stockNameFont = new Font("AGENCY FB", Font.BOLD, 20);
	private Border borderBase = new LineBorder(Color.GRAY, 1);
	
	private TimeSeries seriesD = new TimeSeries("Value");
	private TimeSeries seriesYTD = new TimeSeries("Value");
	private TimeSeries seriesM = new TimeSeries("Value");
	private TimeSeries seriesY = new TimeSeries("Value");
	private TimeSeries series5Y = new TimeSeries("Value");
	
	public SimpleDateFormat s1 = new SimpleDateFormat("MM-dd-yyyy");
	
	public APIcall stockInfo;
	public BufferedReader reader = null;
	public BufferedWriter writer = null;
	File inputFile = new File("portfolio.txt");
	File tempFile = new File("tempFile.txt");
	boolean successful = false;
	
	public InfoScreen(CardLayout clin, JPanel cardPanelin, String stockAb, int numO) {
		if(stockAb != null) {
			
			try {
				BufferedReader in =  new BufferedReader(new FileReader("portfolio.txt"));
	    			String inputLine;
	    			while ((inputLine = in.readLine()) != null) {
					String stock = inputLine.substring(0, inputLine.indexOf("\t"));
					favStocks.add(stock);
					if(stock.equals(stockAb)) {
						addIn = 1;
						jbDelete.setEnabled(true);
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
   
			stockInfo= new APIcall(stockAb, numO);
			stockInfo.setValues();
			stockName = stockInfo.stockFullName;
			stockAbr = stockAb;
			stockCurPrice = stockInfo.cPrice;
			stockValue   = " Current Value: "+ Double.toString(stockCurPrice);
			stockValueNum = stockCurPrice;
			stockOpen    = "Open: " + Double.toString(stockInfo.openPrice);
			stockHigh    = "High: " + Double.toString(stockInfo.highPrice);
			stockLow     = "Low: " + Double.toString(stockInfo.lowPrice);
			stockClose   = "Close: " + Double.toString(stockInfo.closePrice);
			String perc = String.format("%.2f",stockInfo.percent);
			stockPerc    = perc + "%";
			stockNum = stockInfo.numOwned;
			initialStockNum = stockNum;
			jtfStockNum = new JTextField(Integer.toString(stockNum));
			stockReturn = stockValueNum * stockNum;
			
			jlbStockName = new JLabel(stockName);
			jlbStockAbr  = new JLabel(" - "+stockAbr);
			jlbValue  = new JLabel(stockValue);
			jlbReturn = new JLabel("Stock Return: $"+String.format("%.2f", stockReturn));
			jlbOpen   = new JLabel(stockOpen);
			jlbHigh   = new JLabel(stockHigh);
			jlbLow    = new JLabel(stockLow);
			jlbClose  = new JLabel(stockClose);
			jlbPerc   = new JLabel(stockPerc);
			jlbTimePrompt = new JLabel(timePrompt);
			
			cl = clin;
			cardPanel = cardPanelin;
			
			//Panel Formatting
			setLayout(new BorderLayout());
			stockInfoPanel.setLayout(new GridLayout(3,5));
			shellChartPanel.setLayout(new BorderLayout());
			buttonPanel.setLayout(new GridLayout(1,2));
			shellChartPanel.setBackground(Color.WHITE);
			
			//Component formatting
			jlbStockName.setFont(stockNameFont);
			jlbStockAbr.setFont(stockNameFont);
			stockInfoPanel.setBorder(new TitledBorder(borderBase, stockName+" - "+stockAbr, TitledBorder.LEFT, TitledBorder.TOP, stockNameFont));
			jtfStockNum.setEditable(false);
			for(int i=0; i<9; i++) {
				blank[i] = new JLabel("                 ");
			}
			
			//Adding components to panels and panels to InfoScreen
			stockInfoPanel.add(jlbValue);
			stockInfoPanel.add(blank[1]);
			stockInfoPanel.add(blank[2]);
			stockInfoPanel.add(blank[7]);
			stockInfoPanel.add(jlbReturn);
			stockInfoPanel.add(blank[3]);
			stockInfoPanel.add(jlbOpen);
			stockInfoPanel.add(jlbHigh);
			stockInfoPanel.add(jlbLow);
			stockInfoPanel.add(jlbClose);
			stockInfoPanel.add(stockNumPanel);
			stockNumPanel.add(jbStockDown);
			jbStockDown.addActionListener(this);
			stockNumPanel.add(jtfStockNum);
			stockNumPanel.add(jbStockUp);
			jbStockUp.addActionListener(this);
			stockInfoPanel.add(blank[4]);
			stockInfoPanel.add(blank[5]);
			stockInfoPanel.add(blank[6]);
			stockInfoPanel.add(stockStatusPanel);
			if (stockInfo.percent > 0) {
				
				jlbPerc.setForeground(Color.GREEN);
				stockStatusPanel.add(jlbPerc);
				ImageIcon icon = createImageIcon("/Green_Arrow_Up_Darker.svg.png");
				JLabel picLabel = new JLabel(icon);
				stockStatusPanel.add(picLabel);
				
			}
			if (stockInfo.percent <= 0) {
				
				jlbPerc.setForeground(Color.RED);
				stockStatusPanel.add(jlbPerc);
				ImageIcon icon = createImageIcon("/2000px-Red_Arrow_Down.svg.png");
				JLabel picLabel = new JLabel(icon);
				stockStatusPanel.add(picLabel);
				
			}
			
			
			shellChartPanel.add(timeDropPanel, BorderLayout.NORTH);
			jcbxTimePeriod.setAlignmentX(Component.CENTER_ALIGNMENT);
			timeDropPanel.add(jlbTimePrompt);
			timeDropPanel.add(jcbxTimePeriod);
			jcbxTimePeriod.addItemListener(this);
			shellChartPanel.add(createContent(), BorderLayout.CENTER);
			
			buttonPanel.add(jbBack);
			jbBack.addActionListener(this);
			buttonPanel.add(jbAdd);
			jbAdd.addActionListener(this);
			buttonPanel.add(jbDelete);
			if(addIn == 1) {
				jbDelete.setEnabled(true);
			}
			else {
				jbDelete.setEnabled(false);
			}
			jbDelete.addActionListener(this);
			
			add(stockInfoPanel, BorderLayout.NORTH);
			add(shellChartPanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
			stockInfoPanel.setBackground(Color.WHITE);
			stockStatusPanel.setBackground(Color.WHITE);
			stockNumPanel.setBackground(Color.WHITE);
			timeDropPanel.setBackground(Color.WHITE);
		}
	}
	
	private ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		}
		else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private void writeStock(String lineToWrite) {
		try {
        		writer = new BufferedWriter(new FileWriter(inputFile, true));
        		writer.write(lineToWrite);
        		writer.newLine();
        		System.out.println("Stock added...");
        		//jbAdd.setEnabled(false);
        } 
    	catch (IOException err) {
            System.err.println(err);
        } 
    	finally {
                try {
                    writer.close();
                } 
                catch (IOException err) {
                    System.err.println(err);
                }
        }
	}
	private void deleteStock(String lineToDelete) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		
			String currentLine;
		
			while((currentLine = reader.readLine()) != null) {
	            if(currentLine != null && !currentLine.equals(lineToDelete)){
	                writer.write(currentLine + System.getProperty("line.separator"));
	            }
	        }
			if (writer != null) {
				writer.close();
				}
				if (reader != null) {
				reader.close();
				}
				boolean successful = tempFile.renameTo(inputFile);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	//CHART CREATION METHODS\\
	
	private JFreeChart createChart(XYDataset dataset) {
	        JFreeChart chart = ChartFactory.createTimeSeriesChart("Value of "+stockName+
	        		" Over the Last "+timePeriod+" ", 
	            timePeriod, "Value", dataset);
	        XYPlot plot = chart.getXYPlot();
	        DateAxis axis = (DateAxis) plot.getDomainAxis();
	        String dateFormat;
	        if (timePeriod == "Day") {
	    			dateFormat= "HH:mm";
	        }
		    else if(timePeriod == "Year to Date") {
		    		dateFormat = "MM-dd-YY";
		    }
		    else if(timePeriod == "Month") {
		    		dateFormat = "MMM dd";
		    }
		    else if(timePeriod == "Year") {
		    		dateFormat = "MMM YYY";
		    }
		    else if(timePeriod == "5 Years") {
		    		dateFormat ="MMM YYY";
		    }
		    else {
		    		dateFormat = "MM-dd-YY";
		    }
	        axis.setDateFormatOverride(new SimpleDateFormat(dateFormat));
	    return chart;
	}
	//Collects data and creates data set: Will have to be revised with API/Db
	private XYDataset createDataset(String stockName, String timeP) {
		TimeSeries series1 = new TimeSeries("Value");
	    int key;
	   	SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    if (timeP == "Day" && seriesD.isEmpty()) {
	    		key = 0;
	    		s1 = new SimpleDateFormat("HH:mm");
	    }
	    else if(timeP == "Month" && seriesM.isEmpty()) {
	    		key = 1;
	    		s1 = new SimpleDateFormat("MM-dd-YY");
	    }
	    else if(timeP == "Year" && seriesY.isEmpty()) {
	    		key = 2;
	    		s1 = new SimpleDateFormat("MMM dd");
	    		s2 = new SimpleDateFormat("yyyy-MM-dd");
	    }
	    else if(timeP == "5 Years" && series5Y.isEmpty()) {
	    		key = 3;
	    		s1 = new SimpleDateFormat("MMM YYY");
	    }
	    else if(timeP == "Year to Date" && seriesYTD.isEmpty()) {
	    		key = 4;
	    		s1 = new SimpleDateFormat("MMM YYY");
	    }
	    else {
	    		key = -1;
	    }
	    if(key != -1) {
		    SortedMap<Date, Double> hist = stockInfo.history(key);
		    ArrayList<Date> dates = new ArrayList<Date>();
		    ArrayList<Double> vals = new ArrayList<Double>();
		    for(Iterator iterator = hist.keySet().iterator(); iterator.hasNext();) {
		    		Date iat = (Date)iterator.next();
		    		dates.add(iat);
		    		vals.add(hist.get(iat));
			}
		    
		    for (int x = 0; x < dates.size(); x++) {
		        series1.add(new Minute(dates.get(x)), vals.get(x));
		    }
		    if (timeP == "Day") {
	    			seriesD = series1;
		    }
		    else if(timeP == "Year to Date") {
		    		seriesYTD = series1;
		    }
		    else if(timeP == "Month") {
		    		seriesM = series1;
		    }
		    else if(timeP == "Year") {
		    		seriesY = series1;
		    }
		    else if(timeP == "5 Years") {
		    		series5Y = series1;
		    }
	    }
	    else {
		    	if (timeP == "Day") {
	    			series1 = seriesD;
		    }
		    else if(timeP == "Year to Date") {
		    		series1 = seriesYTD;
		    }
		    else if(timeP == "Month") {
		    		series1 = seriesM;
		    }
		    else if(timeP == "Year") {
		    		series1 = seriesY;
		    }
		    else if(timeP == "5 Years") {
		    		series1 = series5Y;
		    }
	    }
	    TimeSeriesCollection dataset = new TimeSeriesCollection();
	    dataset.addSeries(series1);
	    return dataset;
	}
	private JPanel createContent() {
        JFreeChart chart = createChart(createDataset(stockAbr, timePeriod));
        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(false);
        this.yCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        return chartPanel;
    }

	private void refreshChart() {
		shellChartPanel.removeAll();
		shellChartPanel.revalidate();
		JFreeChart aChart = createChart(createDataset(stockAbr, timePeriod));
		ChartPanel chartPanel = new ChartPanel(aChart);
		chartPanel.addChartMouseListener(this);
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(false);
        this.yCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        shellChartPanel.add(timeDropPanel, BorderLayout.NORTH);
		shellChartPanel.add(chartPanel, BorderLayout.CENTER);
	}
	
	//EVENT LISTENERS\\
	
	//ActionListener for Enter and NewUser buttons, creates DialogBoxes
    @Override
    public void actionPerformed (ActionEvent e) {
            if (e.getSource()==jbBack) {
            			cl.previous(cardPanel);
            			cardPanel.remove(cardPanel.getComponentCount() -1);
            			System.out.println("Returning home...");
            }
            if (e.getSource()==jbDelete) {
                    int deleteStock = JOptionPane.showConfirmDialog(
                                    null, "Are you sure you want to delete this "
                                    +"stock from your portfolio?", "Confirm Stock Delete", 
                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (deleteStock==JOptionPane.YES_OPTION) {
                    		cl.previous(cardPanel);
                    		cardPanel.remove(4);
                    		deleteStock(stockAbr + "\t" + initialStockNum);
                            System.out.println("This stock has been deleted.");
                    }

        
            }
            
            if (e.getSource()==jbAdd) {
            	if(addIn == 0) {
            		writeStock(stockAbr + "\t" + stockNum);
            	}
            	else if(addIn == 1) {
            		deleteStock(stockAbr + "\t" + initialStockNum);
            		writeStock(stockAbr + "\t" + stockNum);
            	}
            }
            
            if (e.getSource()==jbStockUp) {
            	stockNum++;
            	stockReturn = stockValueNum * stockNum;
            	jlbReturn.setText("Stock Return: $"+String.format("%.2f", stockReturn));
            	jtfStockNum.setText(Integer.toString(stockNum));
            }
            if (e.getSource()==jbStockDown && stockNum>0) {
        		stockNum--;
        		stockReturn = stockValueNum * stockNum;
        		jlbReturn.setText("Stock Return: $"+String.format("%.2f", stockReturn));
        		jtfStockNum.setText(Integer.toString(stockNum));
            }
    }
    
    
  //ChartMouseListener for crosshairs
    @Override
    public void chartMouseMoved(ChartMouseEvent e) {
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = e.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(e.getTrigger().getX(), dataArea, 
                RectangleEdge.BOTTOM);
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        this.yCrosshair.setValue(y);
    }
    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        // ignore
    }
    
    //ItemListener for setting the time period for the chart
    public void itemStateChanged(ItemEvent e) {
    	if(e.getStateChange()==ItemEvent.SELECTED) {
	    	if(e.getSource()==jcbxTimePeriod) {
	    		if(jcbxTimePeriod.getSelectedItem().equals("Day"))
	    			timePeriod = "Day";
	    		else if (jcbxTimePeriod.getSelectedItem().equals("Year to Date"))
	    			timePeriod = "Year to Date";
	    		else if (jcbxTimePeriod.getSelectedItem().equals("Month"))
	    			timePeriod = "Month";
	    		else if (jcbxTimePeriod.getSelectedItem().equals("Year"))
	    			timePeriod = "Year";
	    		else if (jcbxTimePeriod.getSelectedItem().equals("5 Years")) 
	    			timePeriod = "5 Years";
	    		else
	    			timePeriod = "Day";
	    		//
	    		refreshChart();
	    	}
	    }
    }
    
}
