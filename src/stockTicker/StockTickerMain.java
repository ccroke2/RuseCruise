package stockTicker;

//import java.io.InputStream;
import java.net.URL;
//import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
//import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
//import org.json.simple.JSONArray;

//UUX1LQNOWRPP64V9 Alpha Vantage API key
public class StockTickerMain {

		public static void main(String[] args) {
			try {
				URL url = new URL ("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=TCS&outputsize=full&apikey=UUX1LQNOWRPP64V9");
				JSONObject json = new JSONObject(url);
				System.out.println(json);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				System.out.println("Done");
			}
		}
}
