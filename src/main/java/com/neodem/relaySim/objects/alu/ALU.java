package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.BitField4;
import com.neodem.relaySim.objects.Component;

import java.util.function.BiFunction;

/**
 * Created by vfumo on 3/13/16.
 */
public class ALU implements Component {

    private BitField out = new BitField4();
    private BitField inA = new BitField4();
    private BitField inB = new BitField4();
    private ALUOperation op = ALUOperation.ADD;
    private boolean carryIn = false;
    private boolean carryOut = false;

    private void compute() {
        switch (op) {
            case ADD:
                carryOut = doAddition(inA, inB, carryIn);
                break;
            case OR:
                process(inA, inB, ALU::or);
                carryOut = false;
                break;
            case AND:
                process(inA, inB, ALU::and);
                carryOut = false;
                break;
            case XOR:
                process(inA, inB, ALU::xor);
                carryOut = false;
                break;
        }
    }

    /**
     *
     * @param a
     * @param b
     * @param carry
     * @return true if we overflow
     */
    protected boolean doAddition(BitField a, BitField b, boolean carry) {
        for (int i = 0; i < 4; i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);
            boolean result = add(bitA, bitB, carry);
            carry = carry(bitA, bitB, carry);
            out.setBit(i, result);
        }
        return carry;
    }


    protected void process(BitField a, BitField b, BiFunction<Boolean, Boolean, Boolean> function) {
        for (int i = 0; i < 4; i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);

            out.setBit(i, function.apply(bitA, bitB));
        }
    }

    protected static Boolean or(Boolean a, Boolean b) {
        return a || b;
    }

    protected static Boolean and(Boolean a, Boolean b) {
        return a && b;
    }

    protected static Boolean xor(Boolean a, Boolean b) {
        if (a || b) return !(a && b);
        return false;
    }

    protected static boolean add(boolean a, boolean b, boolean carry) {
        boolean result = xor(a, b);
        if (carry) return !result;
        return result;
    }

    protected static boolean carry(boolean a, boolean b, boolean carry) {
        if (carry) return (a || b);
        return (a && b);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();

        b.append(op);
        b.append(": ");

        if (carryIn) b.append("C ");
        else b.append("  ");
        b.append("a=");
        b.append(inA);
        b.append(' ');
        b.append("b=");
        b.append(inB);
        b.append(' ');
        b.append("o=");
        b.append(out);
        if(carryOut) b.append(" carryOut");

        return b.toString();
    }

    public void setOperation(ALUOperation op) {
        this.op = op;
        compute();
    }

    public void setInA(BitField inA) {
        this.inA = inA;
        compute();
    }

    public void setInB(BitField inB) {
        this.inB = inB;
        compute();
    }

    public void setCarryIn(boolean carryIn) {
        this.carryIn = carryIn;
        compute();
    }

    public BitField getOut() {
        return out;
    }

    public ALUOperation getOp() {
        return op;
    }

    public boolean getCarryOut() {
        return carryOut;
    }

    public BitField getInA() {
        return inA;
    }

    public BitField getInB() {
        return inB;
    }

    public boolean getCarryIn() {
        return carryIn;
    }
}
