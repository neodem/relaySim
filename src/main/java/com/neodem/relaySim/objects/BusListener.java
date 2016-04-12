package com.neodem.relaySim.objects;

import com.neodem.relaySim.objects.bus.Bus;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public interface BusListener {
    void dataChanged(Bus b);
}
