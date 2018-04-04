package stockTicker;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

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
	
	private String stockName = "Stock Name";
	private String stockAbr = "STN";
	private String stockValue = "200";
	private String stockOpen = "00";
	private String stockHigh = "11";
	private String stockLow = "22";
	private String stockClose = "33";
	private String stockPerc = "20%";
	private String timePeriod = "Day";
	private String[] timeLabels = {"Day","Week","Month","Year","5 Years"};
	
	private JLabel jlbStockName = new JLabel(stockName);
	private JLabel jlbStockAbr  = new JLabel(stockAbr);
	private JLabel jlbValue = new JLabel(stockValue);
	private JLabel jlbOpen  = new JLabel(stockOpen);
	private JLabel jlbHigh  = new JLabel(stockHigh);
	private JLabel jlbLow   = new JLabel(stockLow);
	private JLabel jlbClose = new JLabel(stockClose);
	private JLabel jlbPerc  = new JLabel(stockPerc);
	private JLabel[] blank  = new JLabel[10];
	
	private JButton jbBack = new JButton("Back");
	private JButton jbDelete = new JButton("Delete");
	private JButton jbStockDown = new JButton("-");
	private JButton jbStockUp	= new JButton("+");
	
	private JTextField jtfStockNum = new JTextField("0");
	private JComboBox jcbxTimePeriod = new JComboBox(timeLabels);
	
	private JPanel stockInfoPanel   = new JPanel();
	private JPanel stockNumPanel    = new JPanel();
	private JPanel stockStatusPanel = new JPanel();
	private JPanel shellChartPanel  = new JPanel();
	private JPanel buttonPanel      = new JPanel();

	
	private ChartPanel chartPanel;
	private Crosshair xCrosshair;
	private Crosshair yCrosshair;
	
	
	public InfoScreen(CardLayout clin, JPanel cardPanelin) {
		cl = clin;
		cardPanel = cardPanelin;

		setLayout(new BorderLayout());
		buttonPanel.setLayout(new GridLayout(1,2));
		stockInfoPanel.setLayout(new GridLayout(3,5));
		stockInfoPanel.setBackground(Color.WHITE);
		add(buttonPanel, BorderLayout.SOUTH);
		add(stockInfoPanel, BorderLayout.NORTH);
		stockNumPanel.setBackground(Color.WHITE);
		stockStatusPanel.setBackground(Color.WHITE);
		jtfStockNum.setEditable(false);
		
		for(int i=0; i<9; i++) {
			blank[i] = new JLabel("                 ");
		}
		
		stockInfoPanel.add(jlbStockName);
		stockInfoPanel.add(jlbStockAbr);
		stockInfoPanel.add(blank[0]);
		stockInfoPanel.add(blank[1]);
		stockInfoPanel.add(blank[2]);
		stockInfoPanel.add(jlbValue);
		stockInfoPanel.add(jlbOpen);
		stockInfoPanel.add(jlbHigh);
		stockInfoPanel.add(jlbLow);
		stockInfoPanel.add(jlbClose);
		stockInfoPanel.add(stockNumPanel);
		stockNumPanel.add(jbStockDown);
		stockNumPanel.add(jtfStockNum);
		stockNumPanel.add(jbStockUp);		
		stockInfoPanel.add(blank[3]);stockInfoPanel.add(blank[4]);stockInfoPanel.add(blank[5]);
		stockInfoPanel.add(stockStatusPanel);
		stockStatusPanel.add(jlbPerc);
		//stockStatusPanel.add(jlbArrow);
		
		buttonPanel.add(jbBack);
		jbBack.addActionListener(this);
		buttonPanel.add(jbDelete);
		jbDelete.addActionListener(this);
		
		shellChartPanel.add(jcbxTimePeriod);
		jcbxTimePeriod.addItemListener(this);
		shellChartPanel.setBackground(Color.WHITE);
		add(shellChartPanel, BorderLayout.CENTER);
		shellChartPanel.add(createContent());
	}
	
	private JFreeChart createChart(XYDataset dataset) {
	        JFreeChart chart = ChartFactory.createXYLineChart("Value of "+stockName+" ", 
	            timePeriod, "Value", dataset);
	    return chart;
	}
	
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
        System.out.println(stockName);
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
        shellChartPanel.add(jcbxTimePeriod);
		jcbxTimePeriod.addItemListener(this);
		shellChartPanel.add(chartPanel);
		shellChartPanel.repaint();
	}
	
	
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











