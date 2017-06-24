package fpts.toolkit.request;

import fpts.model.Market;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A thread that runs the requests to the yahoo servers and updates the market prices
 */
public class UpdateThread {
    private List<List<String>> listOfTickerGroups;
    private Market m;

    public UpdateThread(Market market, ArrayList<String> targetTickers) {
        m = market;
        listOfTickerGroups = new ArrayList<>();
        ArrayList<String> tickerGroup = new ArrayList<>();
        int i = 0;
        int len = 15;
        for (String key : targetTickers) {
            if (i == len) {
                i = 0;
                tickerGroup.add(key);
                listOfTickerGroups.add(tickerGroup);
                tickerGroup = new ArrayList<>();
            } else if (i < len) {
                tickerGroup.add(key);
                i++;
            }
        }
        listOfTickerGroups.add(tickerGroup);
    }

    /**
     * Starts the different threads that request the market information from the api
     * @throws InterruptedException
     */
    public void Update() throws InterruptedException {
        for (List<String> list : listOfTickerGroups) {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            for (String ticker : list) {
                executor.execute(new RequestThread(ticker, m));
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
    }
}
