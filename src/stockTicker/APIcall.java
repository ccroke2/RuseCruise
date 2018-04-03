package stockTicker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APIcall {
		
		String stockName;
	
		ArrayList<String> sNames = new ArrayList<String>();
		String url_string;
		String resultJ;
		static String API_KEY = "UUX1LQNOWRPP64V9";
		
		private Connection conn = null;
		
		//Constructor
		public APIcall() {
			try {
			    conn =
			       DriverManager.getConnection("jdbc:mysql://localhost/test?" +
			                                   "user=minty&password=greatsqldb");

			    // Do something with the Connection
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		
		//JSON parse function
		private JSONObject JSONparse(String url_string) {
			try {
			String inline =  "";
			URL url = new URL (url_string);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			conn.setRequestMethod("GET");
			conn.connect();
			Scanner sc = new Scanner(url.openStream());
			while(sc.hasNext()){
				inline+=sc.nextLine();
			}
			System.out.println("\nJSON data in string format");
			
			sc.close();
			JSONParser parse = new JSONParser();
			JSONObject jobj = (JSONObject)parse.parse(inline); 
			return jobj;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				System.out.println("JSON Parse done.");
			}
			return null;
		}
		// returnAPI_single returns a list of the open, high, low, and close values for chosen stock.
		// Parameters are s-> stock symbol and goal->date requested
		public ArrayList<String> returnAPI_single(String s, String goal) {
			try {
				stockName = s;
				url_string = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stockName + "&outputsize=compact&apikey=" + API_KEY;
				
				JSONObject jobj = JSONparse(url_string);
				
				JSONObject rJ = (JSONObject) jobj.get("Time Series (Daily)");
				rJ = (JSONObject) rJ.get(goal);
				
				ArrayList<String> values = new ArrayList<String>();
				values.add((String) rJ.get("1. open"));
				values.add((String) rJ.get("2. high"));
				values.add((String) rJ.get("3. low"));
				values.add((String) rJ.get("4. close"));
				return values;
				
			}
			finally {
				System.out.println("Done");
			}
		}
		
		// returnAPI_batch returns a list of the current values of given stocks in a list
		// Parameter is s->list of stock symbols
		public ArrayList<String> returnAPI_batch(ArrayList<String> s) {
			try {
				sNames = s;
				url_string = "https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=";
				for (int i = 0; i < sNames.size(); i++) {
					url_string += sNames.get(i);
					if (i != sNames.size() - 1) {
						url_string += ",";
					}
				}
				url_string += "&apikey=" + API_KEY;
				
				JSONObject jobj = JSONparse(url_string);
				
				ArrayList<String> values = new ArrayList<String>();
				JSONArray rJ = (JSONArray) jobj.get("Stock Quotes");

				for (Object t : rJ) {
					JSONObject tempO = (JSONObject)t;
					values.add((String) tempO.get("2. price"));
				}
				
				return values;
				
			}
			finally {
				System.out.println("Done");
			}
		}
		
		//Get current price of one stock
		public double singleCurrentPrice(String stock) {
			try {
				url_string = "https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=" + stock + "&apikey=" + API_KEY;
				
				JSONObject jobj = JSONparse(url_string);
				
				JSONArray rJ = (JSONArray) jobj.get("Stock Quotes");
				jobj = (JSONObject) rJ.get(0);
				return Double.parseDouble((String) jobj.get("2. price"));
			}
			finally {
				System.out.println("Done");
			}
		}
		
		//returns a mapping <time:value> for 1 day history (most recent time to open time)
		//key: 0-day, 1-week, 2-month, 3-year, 4-5year
		public SortedMap<Date, Double> history(String stock, int keyLen) {
			try {
				String neededKey = "";
				if (keyLen == 0) {
					url_string = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + stock + "&interval=1min&outputsize=full&apikey=" + API_KEY;
					neededKey = "Time Series (1min)";
				}
				else if (keyLen == 1 || keyLen == 2) {
					url_string = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stock + "&apikey=" + API_KEY;
					neededKey = "Time Series (Daily)";
				}
				else if (keyLen == 3) {
					url_string = "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=" + stock + "&apikey=" + API_KEY;
					neededKey = "Weekly Time Series";
				}
				else if (keyLen == 4) {
					url_string = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=" + stock + "&apikey=" + API_KEY;
					neededKey = "Monthly Time Series";
				}
				else {
					System.out.println("incorrect key in function");
					return null;
				}
				
				JSONObject jobj = JSONparse(url_string);

				ArrayList<Double> values = new ArrayList<Double>();
				ArrayList<Date> dates = new ArrayList<Date>();
				jobj = (JSONObject)jobj.get(neededKey);
				
				String tempVal;
				JSONObject temp0;
				Date d1;
				
				SortedMap<Date, Double> hist = new TreeMap<Date, Double>(Collections.reverseOrder());
				for(Iterator iterator = jobj.keySet().iterator(); iterator.hasNext();) {
				    String key = (String) iterator.next();
				    if (keyLen == 0) {
				    		d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(key);
				    }
				    else if(keyLen > 0 && keyLen < 5) {
				    		d1 = new SimpleDateFormat("yyyy-MM-dd").parse(key);
				    }
				    else {
				    		System.out.println("incorrect date format");
				    		break;
				    }
				    dates.add(d1);
				    temp0 = (JSONObject)jobj.get(key);
				    values.add(Double.parseDouble((String)temp0.get("4. close")));
				    hist = sorting(dates, values, keyLen);
				}
				return hist;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				System.out.println("Done");
			}
			return null;
		}
		
		//key: 0-day, 1-week, 2-month, 3-year, 4-5year
		private SortedMap<Date, Double> sorting(ArrayList<Date> d, ArrayList<Double> s, int key) {
			SortedMap<Date, Double> hist = new TreeMap<Date, Double>(Collections.reverseOrder());
			for (int i = 0; i< d.size(); i++) {
				hist.put(d.get(i), s.get(i));
			}
			Date goalDate = hist.firstKey();
			
			long millisecondDif = 0;
			if (key == 0) { //day
				millisecondDif = 25200000L;
			}
			else if (key == 1) { //week
				millisecondDif = 1209600000L;
			}
			else if (key == 2) { //month
				millisecondDif = 2592000000L;
			}
			else if (key == 3) { //year
				millisecondDif = 31556952000L;
			}
			else if (key == 4) { //5 year
				millisecondDif = 157784630000L;
			}
			else {
				System.out.println("incorrect key in function");
				return null;
			}
			
			Iterator<Date> iter = hist.keySet().iterator();
	        while (iter.hasNext()) {
	            Date str = iter.next();
	            	if(goalDate.getTime() - str.getTime() > millisecondDif) {	
	            		iter.remove();
	            	}
	        }
	        while(key == 1 && hist.size() > 5) {
	        		hist.remove(latestDate(hist.keySet()));
	        }
			return hist;
		}
		
		private Date latestDate(Set<Date> s) {
			Date[] s2 = s.toArray(new Date[s.size()]);
			Date lateDate = s2[0];
			for (int i = 0; i < s2.length; i++) {
				if (lateDate.after(s2[i])) {
					lateDate = s2[i];
				}
			}
			return lateDate;
		}
		//Gets percent change in stock
		//Calculated by (PrevClose-Current)/PrevClose * 100
		public double stockPercent(String stock) {
			
			// Get current price with batch
			double curPrice = singleCurrentPrice(stock);
			
			//Get previous close with single
			//Should check if market is closed for the day
			
			TimeZone timeZone = TimeZone.getTimeZone("US/Eastern");
			
			DateFormat checkTime = new SimpleDateFormat("HH");
			checkTime.setTimeZone(timeZone);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setTimeZone(timeZone);
			Date time = new Date();
			int t = Integer.parseInt(checkTime.format(time));
			
			//default to today
			Date date = new Date();
			String d = (String)dateFormat.format(date);
			
			//Markets close at 4pm
			if(t < 16) {
				//yesterday's date
				date = new Date(System.currentTimeMillis()-24*60*60*1000);
				d = (String)dateFormat.format(date);
			}
			ArrayList<String> closePriceList = new ArrayList<String>();
			closePriceList = returnAPI_single(stock,d);
			double closePrice = Double.parseDouble(closePriceList.get(3));
			
			return (curPrice - closePrice)/closePrice * 100;
		}
}
