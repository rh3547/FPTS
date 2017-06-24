package fpts.model;

import fpts.toolkit.CSVParser;

import java.util.*;

/**
 * Created by George Herde on 3/2/16.
 * <p>
 * The market that the system uses to lookup stocks, etc.
 * This class handles importing the market from CSV.
 */
public class Market {
    //Key(String): Ticker  Value(List): [Ticker, Name, Price, Sector, Market]  (market is not always included)
    private HashMap<String, List<String>> market = new HashMap<>();
    private HashMap<String, List<List<String>>> sectors = new HashMap<>();

    /**
     * Market constructor that reads a csv file with equities to get current stock prices
     */
    public Market() {
        Set<String> checkedSector = new HashSet<>();
        for (List<String> line : CSVParser.parseFile("src/lib/market/equities.csv", false)) {
            for (int i = 0; i < line.size(); i++) {
                if (i == 0) {
                    if (line.get(i).contains("^")) {
                        String replace = line.get(i).replace("^", "'");
                        line.set(i, replace);
                    }
                }
                line.set(i, line.get(i).trim());
            }
            market.put(line.get(0), line);
            if (line.size() >= 4) {
                List<List<String>> item;
                if (checkedSector.add(line.get(3))) {
                    item = new ArrayList<>();
                } else {
                    item = sectors.get(line.get(3));
                }
                item.add(line);
                sectors.put(line.get(3), item);
            }
            if (line.size() == 5) {
                List<List<String>> item;
                if (checkedSector.add(line.get(4))) {
                    item = new ArrayList<>();
                } else {
                    item = sectors.get(line.get(4));
                }
                item.add(line);
                sectors.put(line.get(4), item);
            }
        }
    }

    public void setMarket(HashMap<String, List<String>> market) {
        this.market = market;
    }

    public void setMarketItemPrice(String key, String price) {
        List<String> li = this.market.get(key);
        li.set(2, price);
        this.market.put(key, li);
    }

    public HashMap<String, List<String>> getMarket() {
        return market;
    }

    public HashMap<String, List<List<String>>> getSectors() {
        return sectors;
    }

    public List<List<String>> getSector(String sector) {
        return sectors.get(sector);
    }

    /**
     * Get the price per share of a index or sector share
     *
     * @param sector - the sector to get a price on
     * @return - the price per share
     */
    public float getSectorPrice(String sector) {
        //Divisor used to calculate price of dow, hard coded because you cant query yahoo for the dow
        float DOW_JONES_DIVISOR = (float) 0.146;
        float price = 0;
        for (List<String> marketInformation : sectors.get(sector)) {
            price += Float.parseFloat(marketInformation.get(2));
        }
        if (sector.equals("DOW")){
            return price / getSector(sector).size() / DOW_JONES_DIVISOR;
        }
        else {
            return price / getSector(sector).size();
        }
    }

    /**
     * Gets the price of a given stock
     * @param ticker the ticker symbol representing a stock
     * @return - the up to date market price
     */
    public float getStockPrice(String ticker) {
        ticker = ticker.trim();
        for (String tickerKey : market.keySet()) {
            if (Objects.equals(ticker, tickerKey)) {
                return Float.parseFloat(market.get(tickerKey).get(2));
            }
        }
        return (float) 0.00;
    }
}
