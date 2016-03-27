package com.neodem.relaySim.objects.register;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.Component;
import com.neodem.relaySim.objects.Connector;

/**
 * Created by : Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on : 3/25/16
 */
public class Register implements Component {
    private BitField input;
    private BitField register;

    public Register(int size) {
        input = new BitField(size);
        register = new BitField(size);
    }

    public BitField get() {
        return register;
    }

    public void set(BitField value) {
        this.input = value;
    }

    public void store() {
        this.register = new BitField(this.input);
    }
}