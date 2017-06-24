package fpts.controller;

import fpts.model.CommandQueue;
import fpts.model.ConcreteSim;
import fpts.model.Portfolio;
import fpts.view.CreateSimulationView;
import fpts.view.SimulationView;
import fpts.view.View;


/**
 * Created by Ryan on 3/11/2016.
 */
public class SimulationController {
    /**
     * Instance of the Queue for all the simulation commands to be put into
     */
    CommandQueue commands = new CommandQueue();

    private static SimulationController instance = new SimulationController();

    /**
     * Private constructor prevents instance from being created.
     */
    private SimulationController() {}

    /**
     * Get the only instance of SimulationController.
     * @return SimulationController
     */
    public static SimulationController getInstance() {
        return instance;
    }

    /**
     * Show the view for creating a new simulation.
     */
    public void showCreateSimulationView() {
        View view = new CreateSimulationView();

        view.initialize();
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * Show the view for a "running" simulation.
     */
    public void showSimulationView(String stringPercent, String type, String length, String duration) {
        View view = new SimulationView();

        addSim(stringPercent, type, length, duration);

        float curValue = UserController.getInstance().getAuthedUser().getPortfolio().getValue();

        Context context = new Context();
        context.addData("newValue", nextSimulation(curValue));
        context.addData("percent", stringPercent);
        context.addData("type", type);
        context.addData("unit", length);
        context.addData("duration", duration);
        context.addData("currentValue", curValue);

        view.initialize(context);
        view.addGuiComponents();

        GuiController.getInstance().setView(view);
    }

    /**
     * The following functions instantiate an instance of ConcreteSim based on the type and value of growth the user wants
     * ConcreteSim constructor uses the following key for type:
     * 1 = bear market(decrease); 2 = bull market(increase); 3 = no growth
     * @param stringPercent - the per annum percent specificed by the user
     * @param type - bear vs bull vs no growth
     * @param length - year vs month vs day
     * @param duration - number of "lenghts"
     */
    public void addSim(String stringPercent, String type, String length, String duration){
        Portfolio portfolio = UserController.getInstance().getAuthedUser().getPortfolio();
        float percent = Float.parseFloat(stringPercent);

        if (type == "Bear Market"){
            commands.commandQueue.add(new ConcreteSim(percent, portfolio, 1, length, duration));
        }
        else if (type == "Bull Market"){
            commands.commandQueue.add(new ConcreteSim(percent, portfolio, 2, length, duration));
        }
        else if (type == "No Growth"){
            commands.commandQueue.add(new ConcreteSim(percent, portfolio, 3, length, duration));
        }
    }

    /**
     * run the next simulation waiting in the
     * @return the new value of the portfolio
     */
    public float nextSimulation(float value){
        return commands.commandQueue.poll().execute(value);
    }
}
