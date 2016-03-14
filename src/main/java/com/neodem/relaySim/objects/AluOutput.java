package com.neodem.relaySim.objects;

/**
 * Created by vfumo on 3/13/16.
 */
public class AluOutput {
    private boolean carryOut;
    private BitField output = new BitField(4);

    public boolean getCarryOut() {
        return carryOut;
    }

    public void setCarryOut(boolean carryOut) {
        this.carryOut = carryOut;
    }

    public BitField getOutput() {
        return output;
    }

    public void setOutput(BitField output) {
        this.output = output;
    }

    public int getCarryOutAsInt() {
        return carryOut ? 1 : 0;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("o=");
        b.append(output);
        if (carryOut) b.append(" carryOut");

        return b.toString();
    }
}
