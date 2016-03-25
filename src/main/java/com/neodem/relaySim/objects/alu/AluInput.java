package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;

/**
 * Created by vfumo on 3/13/16.
 */
public class AluInput {
    private ALUOperation operation;
    private boolean carryIn = false;
    private BitField a = new BitField(4);
    private BitField b = new BitField(4);

    public BitField getA() {
        return a;
    }

    public void setA(BitField a) {
        this.a = a;
    }

    public BitField getB() {
        return b;
    }

    public void setB(BitField b) {
        this.b = b;
    }

    public ALUOperation getOperation() {
        return operation;
    }

    public void setOperation(ALUOperation operation) {
        this.operation = operation;
    }

    public void setB(int bit3, int bit2, int bit1, int bit0) {
        b.setBit(0, bit0);
        b.setBit(1, bit1);
        b.setBit(2, bit2);
        b.setBit(3, bit3);
    }

    public void setA(int bit3, int bit2, int bit1, int bit0) {
        a.setBit(0, bit0);
        a.setBit(1, bit1);
        a.setBit(2, bit2);
        a.setBit(3, bit3);
    }

    public boolean getCarryIn() {
        return carryIn;
    }

    public void setCarryIn(boolean i) {
        carryIn = i;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        if (carryIn) b.append("C ");
        else b.append("  ");
        b.append("a=");
        b.append(a);
        b.append(' ');
        b.append("b=");
        b.append(this.b);

        return b.toString();
    }
}
