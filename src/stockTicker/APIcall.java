package stockTicker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

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
		
		//Constructor
		public APIcall() {
		
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
