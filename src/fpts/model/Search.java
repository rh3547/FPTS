package fpts.model;

import fpts.controller.MarketController;

import java.util.LinkedList;

/**
 * Created by George Herde on 3/2/16.
 * Edited by Alex Garrity on 3/11/16
 * <p>
 * Stores an instance of the marked
 * Has two searches functions
 * one for searching the market
 * one for searching the users portfolio
 */
public class Search {
    private Market market = MarketController.getInstance().m;

    /**
     * Searches through all the holdings in the portfolio based on the string inputted by the user
     * Ordered first to last based on the number of matches in the search string and the holdings variables
     *
     * @param search    - the string to be searched on
     * @param portfolio - the users portfolio to search through
     * @return - an linkedList of any holdings in the portfolio that have any variable that match any part of the search string
     * If the search has some sort of match with the name it is added to the front of the list
     */
    public LinkedList<Holding> portfolioSearch(String search, Portfolio portfolio) {
        search = search.toLowerCase();

        LinkedList<Holding> holdings = new LinkedList<>();
        LinkedList<Holding> fourMatch = new LinkedList<>();
        LinkedList<Holding> threeMatch = new LinkedList<>();
        LinkedList<Holding> twoMatch = new LinkedList<>();
        LinkedList<Holding> oneMatch = new LinkedList<>();

        for (Holding h : portfolio.getChildren()) {
            int match = 0;

            //if they search the exact name of a cash account put the holding at the front
            if (search.contains(h.getName().toLowerCase()) || h.getName().toLowerCase().contains(search) ){
                match = 4;

            } else if (h instanceof Equity) {
                //for each word in the search increment for each match
                for (String s : search.split(" ")) {
                    if (((Equity) h).getTickerSymbol().toLowerCase().contains(s) || ((Equity) h).getIndex().toLowerCase().contains(s) || ((Equity) h).getSector().toLowerCase().contains(s)) {
                        match++;
                    } else if (s.contains(((Equity) h).getTickerSymbol().toLowerCase()) || s.contains(((Equity) h).getIndex().toLowerCase()) || s.contains(((Equity) h).getSector().toLowerCase())) {
                        match++;
                    }
                }
            } else if (h instanceof IndexShare) {
                if (h.getChildren() != null) {
//                    for (Holding e : h.getChildren()) {
//                        if (search.contains(h.getName())) {
//                            match = 4;
//                        }
//                        for (String s : search.split(" ")) {
//                            if (((Equity) e).getIndex().toLowerCase().contains(s) || ((Equity) e).getSector().toLowerCase().contains(s)) {
//                                match++;
//                            } else if (s.contains(((Equity) e).getTickerSymbol().toLowerCase()) || s.contains(((Equity) e).getIndex().toLowerCase()) || s.contains(((Equity) e).getSector().toLowerCase())) {
//                                match++;
//                            }
//                        }
//                    }
                }
            }

            //add to proper sub-list based on number of matches
            if (match == 1) {
                oneMatch.add(h);
            } else if (match == 2) {
                twoMatch.add(h);
            } else if (match == 3) {
                threeMatch.add(h);
            } else if (match >= 4) {
                fourMatch.add(h);
            }

        }

        //go through each list adding them to the front of holdings
        //High matches go last so they're at the front
        if (!oneMatch.isEmpty()) {
            for (Holding i : oneMatch) {
                holdings.addFirst(i);
            }
        }  if (!twoMatch.isEmpty()) {
            for (Holding i : twoMatch) {
                holdings.addFirst(i);
            }
        }  if (!threeMatch.isEmpty()) {
            for (Holding i : threeMatch) {
                holdings.addFirst(i);
            }
        }  if (!fourMatch.isEmpty()) {
            for (Holding i : fourMatch) {
                holdings.addFirst(i);
            }
        }

        return holdings;
    }


    /**
     * Searches through all the equities in the market based on the string inputted by the user
     * Ordered first to last based on the number of matches in the search string and the equities variables
     *
     * @param search - the string to be searched on
     * @return - a linkedList of any equities that have any variable that match any part of the search string
     * The linkedList contains the key to the market HashMap of the equity
     * If the search has some sort of match with the ticker symbol it is added to the front of the list
     */
    public LinkedList<String> marketSearch(String search) {
        search = search.toLowerCase();

        LinkedList<String> equities = new LinkedList<>();
        LinkedList<String> fiveMatch = new LinkedList<>();
        LinkedList<String> fourMatch = new LinkedList<>();
        LinkedList<String> threeMatch = new LinkedList<>();
        LinkedList<String> twoMatch = new LinkedList<>();
        LinkedList<String> oneMatch = new LinkedList<>();


        for (String h : market.getMarket().keySet()) {
            int match = 0;
            //if the search contains the name add it to the front
            if (search.contains(h.toLowerCase()) || h.toLowerCase().contains(search)) {
                match = 5;
            }
            for (String e : market.getMarket().get(h)) {
                if (search.contains(e.toLowerCase()) || e.toLowerCase().contains(search)) {
                    match++;
                }
            }

            //add to proper sublist based on number of matches
            if (match == 1) {
                oneMatch.add(h);
            } else if (match == 2) {
                twoMatch.add(h);
            } else if (match == 3) {
                threeMatch.add(h);
            } else if (match == 4) {
                fourMatch.add(h);
            } else if (match >= 5) {
                fiveMatch.add(h);
            }

        }
        //go through each list adding them to the front of holdings
        //High matches go last so they're at the front
        if (!oneMatch.isEmpty()) {
            for (String i : oneMatch) {
                equities.addFirst(i);
            }
        }  if (!twoMatch.isEmpty()) {
            for (String i : twoMatch) {
                equities.addFirst(i);
            }
        }  if (!threeMatch.isEmpty()) {
            for (String i : threeMatch) {
                equities.addFirst(i);
            }
        }  if (!fourMatch.isEmpty()) {
            for (String i : fourMatch) {
                equities.addFirst(i);
            }
        }  if (!fiveMatch.isEmpty()) {
            for (String i : fiveMatch) {
                equities.addFirst(i);
            }
        }

        return equities;
    }
}
