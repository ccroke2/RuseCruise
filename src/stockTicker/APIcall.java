package stockTicker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APIcall {
		
		String stockName;	//Hello, WORRRRRLLLLLLDDD, world
	
		ArrayList<String> sNames = new ArrayList<String>();
		String url_string;
		String resultJ;
		String resultS;
		static String API_KEY = "UUX1LQNOWRPP64V9";
		
		public APIcall() {
		
		}
		// returnAPI_single returns a list of the open,high,low, and close values for chosen stock.
		// Parameters are s-> stock symbol and goal->date requested
		public ArrayList<String> returnAPI_single(String s, String goal) {
			try {
				stockName = s;
				url_string = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stockName + "&outputsize=compact&apikey=" + API_KEY;
				
				String inline =  "";
				URL url = new URL (url_string);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
				conn.setRequestMethod("GET");
				conn.connect();
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
				inline+=sc.nextLine();
				}
				System.out.println("\nJSON data in string format");
				//System.out.println(inline);
				sc.close();
				JSONParser parse = new JSONParser();
				JSONObject jobj = (JSONObject)parse.parse(inline); 
				
				resultS = inline;
				//System.out.println(jobj);
				
				
				JSONObject rJ = (JSONObject) jobj.get("Time Series (Daily)");
				rJ = (JSONObject) rJ.get(goal);
				
				ArrayList<String> values = new ArrayList<String>();
				values.add((String) rJ.get("1. open"));
				values.add((String) rJ.get("2. high"));
				values.add((String) rJ.get("3. low"));
				values.add((String) rJ.get("4. close"));
				return values;
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				System.out.println("Done");
			}
			return null;
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
				
				String inline =  "";
				URL url = new URL (url_string);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
				conn.setRequestMethod("GET");
				conn.connect();
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
				inline+=sc.nextLine();
				}
				System.out.println("\nJSON data in string format");
				//System.out.println(inline);
				//System.out.println();
				sc.close();
				JSONParser parse = new JSONParser();
				JSONObject jobj = (JSONObject)parse.parse(inline); 
				
				ArrayList<String> values = new ArrayList<String>();
				JSONArray rJ = (JSONArray) jobj.get("Stock Quotes");
				for (Object t : rJ) {
					JSONObject tempO = (JSONObject)t;
					values.add((String) tempO.get("2. price"));
				}
			    
				
				resultS = inline;
				
				//System.out.println(jobj);
				
				return values;
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				System.out.println("Done");
			}
			return null;
		}
}
