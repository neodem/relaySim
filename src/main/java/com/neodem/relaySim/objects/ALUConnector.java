package com.neodem.relaySim.objects;

import com.neodem.relaySim.objects.tools.BitTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vfumo on 3/13/16.
 */
public class ALUConnector extends Connector {
    private ALUOperation operation;
    private int carryOut;
    private int carryIn = 0;
    private List<Integer> output;
    private List<Integer> aInput = new ArrayList<>(4);
    private List<Integer> bInput = new ArrayList<>(4);

    public int getCarryOut() {
        return carryOut;
    }

    public List<Integer> getOutput() {
        return output;
    }

    public ALUOperation getOperation() {
        return operation;
    }

    public List<Integer> getAInput() {
        return aInput;
    }

    public List<Integer> getBInput() {
        return bInput;
    }

    public void setBInput(int bit3, int bit2, int bit1, int bit0) {
        trigger();
        bInput.add(0, bit0);
        bInput.add(1, bit1);
        bInput.add(2, bit2);
        bInput.add(3, bit3);
    }

    public void setAInput(int bit3, int bit2, int bit1, int bit0) {
        trigger();
        aInput.add(0, bit0);
        aInput.add(1, bit1);
        aInput.add(2, bit2);
        aInput.add(3, bit3);
    }

    public int getCarryIn() {
        return carryIn;
    }

    public void setCarryIn(int i) {
        trigger();
        carryIn = i;
    }

    public void setCarryOut(int carryOut) {
        trigger();
        this.carryOut = carryOut;
    }

    public void setOutput(List<Integer> output) {
        trigger();
        this.output = output;
    }

    public String display() {
        StringBuffer b = new StringBuffer();
        b.append("a=");
        b.append(BitTools.makeString(aInput));
        b.append(' ');
        b.append("b=");
        b.append(BitTools.makeString(bInput));
        b.append(' ');
        b.append("o=");
        b.append(BitTools.makeString(output));
        if(carryOut == 1) b.append(" carryOut");

        return b.toString();
    }
}
