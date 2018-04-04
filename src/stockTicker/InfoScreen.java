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

public class InfoScreen extends JPanel implements ActionListener, ChartMouseListener, ItemListener {

	CardLayout cl;
	JPanel cardPanel;
	
	/*
	try {
		BufferedImage statusArrowRed = ImageIO.read(getClass().getResource("/2000px-Red_Arrow_Down.svg.png"));
		private JLabel jlbArrowRed = new JLabel(statusArrowRed);
		BufferedImage statusArrowGreen = ImageIO.read(getClass().getResource("/Green_Arrow_Up_Darker.svg.png"));
		private JLabel jlbArrowGreen = new JLabel(statusArrowGreen);
	}
	catch(Exception e) {
		System.out.println("Bad read");
		System.out.println(e);
	}
	*/
	private int stockNum = 0;
	private int stockValueNum = 200;
	private int stockReturn = stockValueNum * stockNum;
	
	private String stockName = "Stock Name";
	private String stockAbr  = "STN";
	private String stockValue   = " Current Value: "+"200";
	private String stockOpen    = "Open: "+"00";
	private String stockHigh    = "High: "+"11";
	private String stockLow     = "Low: "+"22";
	private String stockClose   = "Close: "+"33";
	private String stockPerc    = "20%";
	private String timePeriod   = "Day";
	private String timePrompt	= "Historical Data for the Last: ";
	private String[] timeLabels = {"Day","Week","Month","Year","5 Years"};
	
	private JLabel jlbStockName = new JLabel(stockName);
	private JLabel jlbStockAbr  = new JLabel(" - "+stockAbr);
	private JLabel jlbValue  = new JLabel(stockValue);
	private JLabel jlbReturn = new JLabel("Stock Return: $"+Integer.toString(stockReturn));
	private JLabel jlbOpen   = new JLabel(stockOpen);
	private JLabel jlbHigh   = new JLabel(stockHigh);
	private JLabel jlbLow    = new JLabel(stockLow);
	private JLabel jlbClose  = new JLabel(stockClose);
	private JLabel jlbPerc   = new JLabel(stockPerc);
	private JLabel jlbTimePrompt = new JLabel(timePrompt);
	private JLabel[] blank   = new JLabel[10];
	
	private JButton jbBack   = new JButton("Back");
	private JButton jbDelete = new JButton("Delete");
	private JButton jbStockDown = new JButton("-");
	private JButton jbStockUp	= new JButton("+");
	
	private JTextField jtfStockNum = new JTextField("0");
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
	
	public InfoScreen(CardLayout clin, JPanel cardPanelin) {
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
		stockInfoPanel.setBorder(new TitledBorder(stockName+" - "+stockAbr));
		jtfStockNum.setEditable(false);
		for(int i=0; i<9; i++) {
			blank[i] = new JLabel("                 ");
		}
		
		//Adding components to panels and panels to InfoScreen
		stockInfoPanel.add(jlbValue);
		stockInfoPanel.add(blank[0]);stockInfoPanel.add(blank[1]);stockInfoPanel.add(blank[2]);
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
		stockInfoPanel.add(blank[4]);stockInfoPanel.add(blank[5]);stockInfoPanel.add(blank[6]);
		stockInfoPanel.add(stockStatusPanel);
		stockStatusPanel.add(jlbPerc);
		/*
			if(stockValue > prevStockValue)
				stockStatusPanel.add(jlbGreenArrow);
			else
				stockStatusPanel.add(jlbRedArrrow);
		*/
		
		shellChartPanel.add(timeDropPanel, BorderLayout.NORTH);
		jcbxTimePeriod.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeDropPanel.add(jlbTimePrompt);
		timeDropPanel.add(jcbxTimePeriod);
		jcbxTimePeriod.addItemListener(this);
		shellChartPanel.add(createContent(), BorderLayout.CENTER);
		
		buttonPanel.add(jbBack);
		jbBack.addActionListener(this);
		buttonPanel.add(jbDelete);
		jbDelete.addActionListener(this);
		
		add(stockInfoPanel, BorderLayout.NORTH);
		add(shellChartPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	//CHART CREATION METHODS\\
	
	private JFreeChart createChart(XYDataset dataset) {
	        JFreeChart chart = ChartFactory.createXYLineChart("Value of "+stockName+
	        		" Over the Last "+timePeriod+" ", 
	            timePeriod, "Value", dataset);
	    return chart;
	}
	//Collects data and creates data set: Will have to be revised with API/Db
	private XYDataset createDataset(String stockName) {
	    XYSeries series = new XYSeries(stockName);
	    for (int x = 0; x < 10; x++) {
	        series.add(x, x + Math.random() * 4.0);
	    }
	    XYSeriesCollection dataset = new XYSeriesCollection(series);
	    return dataset;
	}
	private JPanel createContent() {
        JFreeChart chart = createChart(createDataset(stockName));
        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);
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
		JFreeChart aChart = createChart(createDataset(stockName));
		ChartPanel chartPanel = new ChartPanel(aChart);
		chartPanel.addChartMouseListener(this);
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);
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
            if(e.getSource()==jbBack) {
                    cl.previous(cardPanel);
                    System.out.println("back button working");
            }
            if(e.getSource()==jbDelete) {
                    int deleteStock = JOptionPane.showConfirmDialog(
                                    null, "Are you sure you want to delete this "
                                    +"stock from your portfolio?", "Confirm Stock Delete", 
                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(deleteStock==JOptionPane.YES_OPTION) {
                    		cl.previous(cardPanel);
                            System.out.println("This will do soemthing later");
                    }
            }
            if(e.getSource()==jbStockUp) {
            	stockNum++;
            	stockReturn = stockValueNum * stockNum;
            	jlbReturn.setText("Stock Return: $"+Integer.toString(stockReturn));
            	jtfStockNum.setText(Integer.toString(stockNum));
            }
            if(e.getSource()==jbStockDown && stockNum>0) {
        		stockNum--;
        		stockReturn = stockValueNum * stockNum;
        		jlbReturn.setText("Stock Return: $"+Integer.toString(stockReturn));
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
	    		else if (jcbxTimePeriod.getSelectedItem().equals("Week"))
	    			timePeriod = "Week";
	    		else if (jcbxTimePeriod.getSelectedItem().equals("Month"))
	    			timePeriod = "Month";
	    		else if (jcbxTimePeriod.getSelectedItem().equals("Year"))
	    			timePeriod = "Years";
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











