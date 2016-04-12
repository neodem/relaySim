package com.neodem.relaySim.objects.register;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.Component;
import com.neodem.relaySim.objects.BusListener;
import com.neodem.relaySim.objects.bus.Bus;

/**
 * Created by : Vincent Fumo (neodem@gmail.com)
 * Created on : 3/25/16
 */
public class Register extends Component implements BusListener {

    private Bus inBus;
    private Bus outBus;

    // internal state
    private BitField input;

    @Override
    public void dataChanged(Bus b) {
        input = b.getData();
    }

    public void init() {
        input = inBus.getData();
    }

    public void store() {
        outBus.updateData(new BitField(this.input));
    }

    public void setInBus(Bus inBus) {
        this.inBus = inBus;
        this.inBus.addListener(this);
    }

    public void setOutBus(Bus outBus) {
        this.outBus = outBus;
        this.outBus.addListener(this);
    }
}
