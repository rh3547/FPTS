package fpts.model;

/**
 * Created by George Herde on 3/2/16.
 * Edited by Alexander Garrity 3/4/16.
 */
public interface Simulation {
    /**
     * author: Alexander Garrity
     * @return - the new value of the WHOLE portfolio
     */
    float execute(float curValue);
}
