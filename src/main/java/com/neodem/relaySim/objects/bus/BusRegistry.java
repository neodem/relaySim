package com.neodem.relaySim.objects.bus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public class BusRegistry {
    private Map<String, Bus> busses = new HashMap<>();

    /**
     * if bus exists, return it, else make one of size 'size' and
     * store and return it
     *
     * @param id
     * @param size
     * @return
     */
    public Bus getBus(String id, int size) {
        Bus bus = busses.get(id);
        if(bus == null) {
            bus = new Bus(size);
            busses.put(id, bus);
        }

        return bus;
    }
}
