package com.neodem.relaySim.objects.component.register;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.objects.component.Component;
import com.neodem.relaySim.data.BusListener;
import com.neodem.relaySim.data.Bus;

/**
 * Created by : Vincent Fumo (neodem@gmail.com)
 * Created on : 3/25/16
 */
public class Register extends Component implements BusListener {

    private Bus inBus;
    private Bus outBus;
    private Bus controlBus;

    // internal state
    private BitField input;

    private boolean initCalled = false;

    @Override
    public void dataChanged(Bus b) {
        if (!initCalled) throw new RuntimeException("init has not been called!");

        BitField control = b.getData();
        if(control.intValue() == 1) {
            input = new BitField(inBus.getData());
            outBus.updateData(input);
        }
    }

    public void init() {
        input = BitField.createFromInt(0);
        outBus.updateData(input);
        initCalled = true;
    }

    public void setInBus(Bus inBus) {
        this.inBus = inBus;
    }

    public void setOutBus(Bus outBus) {
        this.outBus = outBus;
    }

    public void setControlBus(Bus controlBus) {
        this.controlBus = controlBus;
        this.controlBus.addListener(this);
    }

    public Bus getInBus() {
        return inBus;
    }

    public Bus getOutBus() {
        return outBus;
    }

    public Bus getControlBus() {
        return controlBus;
    }
}
