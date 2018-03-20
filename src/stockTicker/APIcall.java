package stockTicker;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class APIcall {
		
		String stockName;	//Hello
		String function;
		String url_string;
		static String API_KEY = "UUX1LQNOWRPP64V9";
		
		public APIcall(String s, String f) {
			stockName = s;
			function = f;
			url_string = "https://www.alphavantage.co/query?function=" + function + "&symbol=" + stockName + "&outputsize=full&apikey=" + API_KEY;
		}
		
		public String returnAPI(String goal) {
			try {
				URL url = new URL (url_string);
				JSONObject json = new JSONObject(url);
				System.out.println(json.keySet());

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				System.out.println("Done");
			}
			return goal + " ";
		}
}
