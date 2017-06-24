package fpts.model;

/**
 * Created by Alexander Garrity 3/4/16.
 * <p>
 * Simulate on the portfolio by an specified percentage
 */
public class ConcreteSim implements Simulation {
    float percent;
    Portfolio portfolio;

    /**
     * Constructor for a simulation
     *
     * @param percent   - the percent the user wants the stocks to change
     * @param portfolio - the portfolio to simulate on
     * @param type      - direction of growth.  Should be passed in as int
     *                  1 = bear market(decrease); 2 = bull market(increase); 3 = no growth
     */
    public ConcreteSim(float percent, Portfolio portfolio, int type, String length, String duration) {
        this.portfolio = portfolio;

        percent = percent / 100;

        if (length == "Day"){
            percent = percent / 365;

        }
        else if (length == "Month"){
            percent = percent / 12;
        }

        percent = percent * Float.parseFloat(duration);

        if (type == 1) {
            percent = Math.abs(percent) * -1;
            this.percent = percent;
        } else if (type == 2) {
            percent = Math.abs(percent);
            this.percent = percent;
        } else if (type == 3) {
            this.percent = 0;
        }

    }


    /**
     * Executes a simulation command and returns the new calculated value
     *
     * @param curValue - the value of the portfolio before sim
     * @return - the value of the portfolio after sim
     */
    public float execute(float curValue) {
        float cashValue = 0;

        //Add up the value of all of the cash accounts in the portfolio
        for (int x = 0; x < portfolio.getNumChildren(); x++) {
            Holding h = portfolio.getChild(x);

            if (h instanceof CashAccount) {
                cashValue += h.getValue();
            }
        }
        //calculate the value of all of the Equities
        float eqValue = curValue - cashValue;

        //calculate new value of portfolio
        float newValue = curValue + (eqValue * percent);

        if (newValue < 0) {
            newValue = 0;
        }

        return newValue;
    }
}
