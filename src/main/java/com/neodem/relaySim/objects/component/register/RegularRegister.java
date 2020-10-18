package com.neodem.relaySim.objects.component.register;

import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import com.neodem.relaySim.objects.component.Component;

/**
 * Created by : Vincent Fumo (neodem@gmail.com)
 * Created on : 3/25/16
 */
public class RegularRegister extends Component implements Register {

    private Bus inBus;
    private Bus outBus;

    // internal state
    private BitField input;
    private boolean selected = false;

    public RegularRegister(int size, Bus inBus) {
        super(size);
        this.inBus = inBus;
    }

    public RegularRegister(int size, String name, Bus inBus) {
        super(size, name);
        this.inBus = inBus;
    }

    @Override
    public void chipSelect(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void write(boolean write) {
        if (!initCalled)
            throw new RuntimeException("init has not been called! This component has not been initialized!");
        if (selected && write) {
            input = inBus.getData().copy();
            outBus.updateData(input);
        }
    }

    @Override
    public void init() {
        super.init();
        input = BitFieldBuilder.createFromInt(0);
        outBus.updateData(input);
    }

    @Override
    public void setInBus(Bus inBus) {
        this.inBus = inBus;
    }

    @Override
    public void setOutBus(Bus outBus) {
        this.outBus = outBus;
    }

    @Override
    public Bus getInBus() {
        return inBus;
    }

    @Override
    public Bus getOutBus() {
        return outBus;
    }

}
