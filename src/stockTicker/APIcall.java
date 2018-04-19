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
import java.util.Calendar;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.lang.Long;
import java.lang.Object;
import java.util.Vector;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APIcall {
		
		public String stockName;
	
		public ArrayList<String> singleResults = new ArrayList<String>();
		public double openPrice;
		public double highPrice;
		public double lowPrice;
		public double closePrice;
		public String stockFullName;
		public ArrayList<String> batchResults = new ArrayList<String>();
		public double cPrice;
		public SortedMap<Date, Double> hist = new TreeMap<Date, Double>(Collections.reverseOrder());
		public double percent;
		
		public ArrayList<String> sNames = new ArrayList<String>();
		public String url_string;
		public String resultJ;
		
		private Connection conn = null;
		Vector<String> sFullAbr = new Vector<String>();
		Vector<String> sFull = new Vector<String>();
		
		//Constructor
		public APIcall() {

		}
		
		public APIcall(String stockAb) {
			stockName = stockAb;
		}
		
		public void getAllNames() {
			url_string = "https://api.iextrading.com/1.0/ref-data/symbols";
			
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
				
				
				sc.close();
				JSONParser parse = new JSONParser();
				JSONArray jobj = (JSONArray)parse.parse(inline); 
				
				JSONObject x;
				String ab;
				String nm;
				for (Object o : jobj) {
					x = (JSONObject)o;
					sFullAbr.add((String)x.get("symbol"));
					sFull.add((String)x.get("name"));
					
				}
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
		String get = "";
		public void setValues() {
			url_string = "https://api.iextrading.com/1.0/stock/" + stockName + "/quote";
			JSONObject jobj = JSONparse(url_string);
			
			get = (jobj.get("high")).toString();
			highPrice = Double.parseDouble(get);
			get = (jobj.get("low")).toString();
			lowPrice = (Double.parseDouble(get));
			get = (jobj.get("open")).toString();
			openPrice = (Double.parseDouble(get));
			get = (jobj.get("close")).toString();
			closePrice = (Double.parseDouble(get));
			get = (jobj.get("latestPrice")).toString();
			cPrice = (Double.parseDouble(get));
			stockFullName = (String)jobj.get("companyName");
			get = (jobj.get("previousClose")).toString();
			double lastClose = (Double.parseDouble(get));
			percent = (cPrice-lastClose)/lastClose * 100;
		}

		//returns a mapping <time:value> for 1 day history (most recent time to open time)
		//key: 0-day, 1-month, 2-year, 3-5year, 4-ytd
		public SortedMap<Date, Double> history(int keyLen) {
			SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd-HH:mm");
			try {
				if (keyLen == 0) {
					url_string = "https://api.iextrading.com/1.0/stock/"+ stockName +"/chart/1d";
					from = new SimpleDateFormat("yyyyMMdd-HH:mm");
				}
				else if (keyLen == 1) {
					url_string = "https://api.iextrading.com/1.0/stock/"+stockName+"/chart/1m";
					from = new SimpleDateFormat("yyyy-MM-dd");
				}
				else if (keyLen == 2) {
					url_string = "https://api.iextrading.com/1.0/stock/"+stockName+"/chart/1y";
					from = new SimpleDateFormat("yyyy-MM-dd");
				}
				else if (keyLen == 3) {
					url_string = "https://api.iextrading.com/1.0/stock/"+stockName+"/chart/5y";
					from = new SimpleDateFormat("yyyy-MM-dd");
				}
				else if (keyLen ==  4) {
					url_string = "https://api.iextrading.com/1.0/stock/"+stockName+"/chart/ytd";
					from = new SimpleDateFormat("yyyy-MM-dd");
				}
				else {
					System.out.println("incorrect key in function");
					return null;
				}
				
				JSONArray jobj;
				
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
					
					
					sc.close();
					JSONParser parse = new JSONParser();
					jobj = (JSONArray)parse.parse(inline); 
				
				
				SortedMap<Date, Double> hist = new TreeMap<Date, Double>(Collections.reverseOrder());
				String date;
				String minute = "";
				Date d;
				String parseDate;
				double val;
				for (Object o : jobj) {
					JSONObject tempO = (JSONObject)o;
					try {
						date = (String)tempO.get("date");
						if(keyLen == 0) {
							minute = (String)tempO.get("minute");
							parseDate = date + "-" + minute;
						}
						else {
							parseDate = date;
						}
						d = from.parse(parseDate);
					}
					catch(Exception e){
						e.printStackTrace();
						d = new Date();
					}
					String get1 = (tempO.get("high")).toString();
					val = Double.parseDouble(get1);
					if (val >= 0) {
						hist.put(d,val);
					}
				}
				return hist;
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
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
			}
			return null;
		}
		
		public ArrayList<APIcall> batchValues(ArrayList<String> abs){
			url_string = "https://api.iextrading.com/1.0/stock/market/batch?symbols=";
			for (int i = 0; i < abs.size(); i++) {
				url_string = url_string + abs.get(i);
				if(i != abs.size() -1) {
					url_string = url_string + ",";
				}
			}
			url_string = url_string + "&types=quote&range=1m&last=1";
					
			JSONObject jobj = JSONparse(url_string);
			ArrayList<APIcall> values = new ArrayList<APIcall>();
			
			JSONObject get;
			String get2;
			JSONObject tempjob;
			
			for (int j = 0; j < abs.size(); j++) {
				
				APIcall temp = new APIcall();
				get = (JSONObject)(jobj.get(abs.get(j)));
				tempjob = (JSONObject)get.get("quote");
				
				temp.stockName = abs.get(j);
				get2 = (tempjob.get("high")).toString();
				temp.highPrice = Double.parseDouble(get2);
				get2 = (tempjob.get("low")).toString();
				temp.lowPrice = (Double.parseDouble(get2));
				get2 = (tempjob.get("open")).toString();
				temp.openPrice = (Double.parseDouble(get2));
				get2 = (tempjob.get("close")).toString();
				temp.closePrice = (Double.parseDouble(get2));
				get2 = (tempjob.get("latestPrice")).toString();
				temp.cPrice = (Double.parseDouble(get2));
				temp.stockFullName = (String)tempjob.get("companyName");
				get2 = (tempjob.get("previousClose")).toString();
				double lastClose = (Double.parseDouble(get2));
				temp.percent = (temp.cPrice-lastClose)/lastClose * 100;
				
				values.add(temp);
			}
			
			return values;
		}
		
}
