package com.neodem.relaySim.objects.bus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vfumo on 3/27/16.
 */
public class BusFactory {
    private Map<String, Bus> busses = new HashMap<>();

    public Bus getBus(String id, int size) {
        Bus bus = busses.get(id);
        if(bus == null) {
            bus = new Bus(size);
            busses.put(id, bus);
        }

        return bus;
    }
}
