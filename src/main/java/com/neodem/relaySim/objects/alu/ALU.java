package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.BitField4;

import java.util.function.BiFunction;

/**
 * control :
 * bit0 : s0
 * bit1 : s1
 * bit2 : bInv
 * bit3 : unused
 * <p>
 * Created by vfumo on 3/13/16.
 */
public class ALU {

    private BitField out;
    private BitField inA;
    private BitField inB;
    private BitField control;
    private boolean carryIn;
    private boolean carryOut;

    // internal
    private ALUOperation op = ALUOperation.ADD;
    private boolean bInv = false;
    private BitField actaulB = new BitField4();

    public ALU() {
        out = new BitField4();
        inA = new BitField4();
        inB = new BitField4();
        control = new BitField4();
        carryIn = false;
        carryOut = false;
        compute();
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

    private void compute() {
        validate();
        parseOperation();

        actaulB = new BitField(inB);
        if(bInv) {
            actaulB.invert();
        }

        switch (op) {
            case ADD:
                carryOut = doAddition(inA, actaulB, carryIn);
                break;
            case OR:
                process(inA, actaulB, ALU::or);
                carryOut = false;
                break;
            case AND:
                process(inA, actaulB, ALU::and);
                carryOut = false;
                break;
            case XOR:
                process(inA, actaulB, ALU::xor);
                carryOut = false;
                break;
        }
    }

    private void parseOperation() {
        int intValue = control.getLSB(2).intValue();
        switch (intValue) {
            case 0:
                //00
                op = ALUOperation.ADD;
                break;
            case 1:
                //01
                op = ALUOperation.OR;
                break;
            case 2:
                //10
                op = ALUOperation.AND;
                break;
            case 3:
                //11
                op = ALUOperation.XOR;
                break;
        }

        bInv =  control.getBit(3);
    }

    private void validate() {
        if (inA == null) throw new IllegalArgumentException("inA is null!");
        if (inB == null) throw new IllegalArgumentException("inB is null!");
        if (inA.getSize() != inB.getSize()) throw new IllegalArgumentException("inA and inB are different sizes!");
    }

    /**
     * @param a
     * @param b
     * @param carry
     * @return true if we overflow
     */
    protected boolean doAddition(BitField a, BitField b, boolean carry) {
        for (int i = 0; i < a.getSize(); i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);
            boolean result = add(bitA, bitB, carry);
            carry = carry(bitA, bitB, carry);
            out.setBit(i, result);
        }
        return carry;
    }

    protected void process(BitField a, BitField b, BiFunction<Boolean, Boolean, Boolean> function) {
        for (int i = 0; i < a.getSize(); i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);

            out.setBit(i, function.apply(bitA, bitB));
        }
    }

    public String toString() {
        StringBuffer b = new StringBuffer();

        b.append(op);
        b.append(": ");

        if (carryIn) b.append("C");
        else b.append(" ");

        if (carryIn) b.append("B");
        else b.append(" ");

        b.append("a=");
        b.append(inA);
        b.append(' ');
        b.append("b=");
        b.append(actaulB);
        b.append(' ');
        b.append("o=");
        b.append(out);
        if (carryOut) b.append(" carryOut");

        return b.toString();
    }

    public BitField getOut() {
        return out;
    }

    public boolean getCarryOut() {
        return carryOut;
    }

    public BitField getInA() {
        return inA;
    }

    public void setInA(BitField inA) {
        this.inA = inA;
        compute();
    }

    public BitField getInB() {
        return inB;
    }

    public void setInB(BitField inB) {
        this.inB = inB;
        compute();
    }

    public boolean getCarryIn() {
        return carryIn;
    }

    public void setCarryIn(boolean carryIn) {
        this.carryIn = carryIn;
        compute();
    }

    public BitField getControl() {
        return control;
    }

    public void setControl(BitField control) {
        this.control = control;
        compute();
    }

    public static BitField convertControl(ALUOperation op, boolean bInv) {
        BitField controlSignal = new BitField4();

        switch (op) {
            case ADD:
                // 00
                break;
            case OR:
                //01
               controlSignal.setBit(0, true);
                break;
            case AND:
                //10
                controlSignal.setBit(1, true);
                break;
            case XOR:
                //11
                controlSignal.setBit(0, true);
                controlSignal.setBit(1, true);
                break;
        }

        controlSignal.setBit(3, bInv);

        return controlSignal;
    }
}
