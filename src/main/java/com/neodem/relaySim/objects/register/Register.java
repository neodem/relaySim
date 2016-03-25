package com.neodem.relaySim.objects.register;

import com.neodem.relaySim.objects.Component;
import com.neodem.relaySim.objects.Connector;

/**
 * Created by : Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on : 3/25/16
 */
public class Register implements Component {
    private Connector value;

    public Connector getValue() {
        return value;
    }

    public void setValue(Connector in) {
        this.value = in;
    }
}
