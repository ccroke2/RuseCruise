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
			APIcall newCall = new APIcall("GOOGL", "TIME_SERIES_DAILY");
			newCall.returnAPI("Meta Data");
		}
}
