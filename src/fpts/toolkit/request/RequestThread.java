package fpts.toolkit.request;

import fpts.model.Market;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by George Herde on 3/28/16.
 */
class RequestThread implements Runnable {
    private String key;
    private Market market;

    public RequestThread(String k, Market m) {
        this.key = k;
        this.market = m;
    }

    @Override
    public void run() {
        String responseString = processUpdate(this.key);
        updateMarket(this.market, this.key, obtainPrice(responseString));
    }

    private String processUpdate(String key) {
        String response = "";
        try {
            response = yahooRequest(key);
        } catch (IOException ignored) {}
        return response;
    }

    private String yahooRequest(String ticker) throws IOException {
        // Create a URL and open a connection
        URL YahooURL = new URL(generateURL(ticker));
        HttpURLConnection con = (HttpURLConnection) YahooURL.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();
        return response.toString();
    }

    private String generateURL(String ticker) {
        // URL from YQL console command: select LastTradePriceOnly from yahoo.finance.quotes where symbol in ("*TICKER*")
        return "https://query.yahooapis.com/v1/public/yql?q=select%20LastTrade" +
                "PriceOnly%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22" +
                ticker +
                "%22)&diagnostics=false&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    }

    private String obtainPrice(String string) {
        Pattern p = Pattern.compile("<LastTradePriceOnly>(\\d+\\.\\d+)</LastTradePriceOnly>");
        Matcher m = p.matcher(string);
        String price = "0.00";
        while (m.find()) {
            price = m.group(1);
        }
        return price;
    }

    private synchronized void updateMarket(Market mar, String key, String price) {
//        System.out.print(mar.getMarket().get(key).get(2));
        mar.setMarketItemPrice(key, price);
//        System.out.print("\tKey:\t" + key + "\tPrice:\t" + price);
//        System.out.print("\t"+ mar.getMarket().get(key).get(2) + "\n");
    }

}

