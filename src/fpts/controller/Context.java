package fpts.controller;

import java.util.HashMap;

/**
 * Created by rhochmuth on 3/4/2016.
 *
 * Context is used to pass data between views and controllers.  It is essentially a dictionary (HashMap) that can be
 * universally recognized throughout the system.
 */
public class Context {

    private HashMap<String, Object> data = new HashMap<>();   // The HashMap that the context data is stored in

    /**
     * Empty constructor.
     */
    public Context() { }

    /**
     * Add an item to this context.
     * @param key String to identify the object by
     * @param obj Object to be stored
     */
    public void addData(String key, Object obj) {
        this.data.put(key, obj);
    }

    /**
     * Retrieve an item from this context.
     * @param key The key for the target object to be obtained
     */
    public Object getData(String key) {
        return this.data.get(key);
    }

    /**
     * Remove all items from this context.
     */
    public void clearContext() {
        this.data.clear();
    }
}
