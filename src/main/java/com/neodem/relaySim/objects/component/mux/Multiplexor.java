package com.neodem.relaySim.objects.component.mux;

import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.data.BusListener;
import com.neodem.relaySim.objects.component.Component;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/12/16
 */
public class Multiplexor extends Component implements BusListener {
    private Bus input0;
    private Bus input1;
    private Bus output;

    // if selected, input1 goes to out
    // if !selected, input0 goes to out
    private boolean selected;

    public Multiplexor(int size, String name) {
        super(size, name);
    }

    public void init() {
        setSelected(false);
    }

    @Override
    public void dataChanged(Bus b) {
        if((b.equals(input0) && !selected) || (b.equals(input1) && selected)) {
            output.updateData(b.getData());
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(this.selected) output.updateData(input1.getData().copy());
        else output.updateData(input0.getData().copy());
    }

    public Bus getInput0() {
        return input0;
    }

    public void setInput0(Bus input0) {
        this.input0 = input0;
        this.input0.addListener(this);
    }

    public Bus getInput1() {
        return input1;
    }

    public void setInput1(Bus input1) {
        this.input1 = input1;
        this.input1.addListener(this);
    }

    public Bus getOutput() {
        return output;
    }

    public void setOutput(Bus output) {
        this.output = output;
    }

    public boolean isSelected() {
        return selected;
    }
}
