package com.neodem.relaySim.objects.bus;

import com.neodem.relaySim.objects.Changer;
import com.neodem.relaySim.objects.ChangingComponent;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public class Bus extends ChangingComponent implements Changer {

    public Bus() {
    }

    public Bus(int size, String name) {
        super(size, name);
        init();
    }
}
