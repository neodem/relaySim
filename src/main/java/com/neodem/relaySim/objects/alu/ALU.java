package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.Changer;
import com.neodem.relaySim.objects.Listener;
import com.neodem.relaySim.objects.bus.Bus;
import com.neodem.relaySim.objects.bus.BusRegistry;
import com.neodem.relaySim.objects.bus.BusNames;

import java.util.function.BiFunction;

/**
 * control :
 * bit3 : s0
 * bit2 : s1
 * bit1 : bInv
 * bit0 : carryIn
 *
 * output
 * bit4 : carryOut
 * bit3-0 : data
 *
 * <p>
 * Created by vfumo on 3/13/16.
 */
public class ALU implements Listener {

    private BusRegistry busRegistry;
    private Bus aluAin;
    private Bus aluBin;
    private Bus aluControl;
    private Bus aluOut;

    // internal
    private BitField out;
    private BitField inA;
    private BitField inB;
    private BitField actaulB;
    private BitField control;
    private boolean carryIn;
    private boolean carryOut;
    private ALUOperation op;
    private boolean bInv;

    public ALU(BusRegistry busRegistry, int size) {
        out = new BitField(size + 1);
        inA = new BitField(size);
        inB = new BitField(size);
        control = new BitField(4);
        carryIn = false;
        carryOut = false;
        op = ALUOperation.ADD;
        bInv = false;
        actaulB = new BitField(inB);

        this.busRegistry = busRegistry;

        aluAin = this.busRegistry.getBus(BusNames.ALU_AIN, 4);
        aluAin.addListener(this);

        aluBin = this.busRegistry.getBus(BusNames.ALU_BIN, 4);
        aluBin.addListener(this);

        aluControl = this.busRegistry.getBus(BusNames.ALU_CTRL, 4);
        aluControl.addListener(this);

        aluOut = this.busRegistry.getBus(BusNames.ALU_OUT, 5);
        aluOut.addListener(this);

        compute();
    }

    @Override
    public void changed(Changer c) {
        BitField bitField = c.getData();

        boolean changed = false;
        if (c.equals(aluAin)) {
            this.inA = bitField;
            changed = true;
        } else if (c.equals(aluBin)) {
            this.inB = bitField;
            changed = true;
        } else if (c.equals(aluControl)) {
            this.control = bitField;
            changed = true;
        }

        if(changed) {
            compute();
            BitField out = getOut();
            aluOut.updateData(out);
        }
    }

    protected void compute() {
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

        out.setBit(4, carryOut);
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
            boolean bitA = a.getBitAsBoolean(i);
            boolean bitB = b.getBitAsBoolean(i);
            boolean result = add(bitA, bitB, carry);
            carry = carry(bitA, bitB, carry);
            out.setBit(i, result);
        }
        return carry;
    }

    protected void process(BitField a, BitField b, BiFunction<Boolean, Boolean, Boolean> function) {
        for (int i = 0; i < a.getSize(); i++) {
            boolean bitA = a.getBitAsBoolean(i);
            boolean bitB = b.getBitAsBoolean(i);

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

    private void parseOperation() {
        int intValue = control.getMSB(2).intValue();
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

        bInv =  control.getBitAsBoolean(1);
        carryIn = control.getBitAsBoolean(0);
    }

    /**
     * helper to generate a control bitfield.
     *
     * bit 3,2 are the operation : 00 == ADD, 01 == OR, 10 == AND, 11 == XOR
     * bit 1 is bInvert
     * bit 0 is carryIn
     *
     * @param op the desired operation
     * @param bInv the bInvert value
     * @param carryIn the carryIn value
     * @return a properly formatted control bitfield
     */
    public static BitField convertControl(ALUOperation op, boolean bInv, boolean carryIn) {
        BitField controlSignal = new BitField(4);

        switch (op) {
            case ADD:
                // 00
                break;
            case OR:
                //01
               controlSignal.setBit(2, true);
                break;
            case AND:
                //10
                controlSignal.setBit(3, true);
                break;
            case XOR:
                //11
                controlSignal.setBit(2, true);
                controlSignal.setBit(3, true);
                break;
        }

        controlSignal.setBit(1, bInv);
        controlSignal.setBit(0, carryIn);

        return controlSignal;
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

    // accessors for test purposes
    protected BitField getOut() {
        return out;
    }
    protected boolean getCarryOut() {
        return carryOut;
    }
    protected void setInA(BitField inA) {
        this.inA = inA;
    }
    protected void setInB(BitField inB) {
        this.inB = inB;
    }
    protected void setControl(BitField control) {
        this.control = control;
    }
}
