package com.neodem.relaySim.objects.component.alu;

import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.data.BusListener;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import com.neodem.relaySim.objects.component.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

/**
 * control :
 * bit3 : s0
 * bit2 : s1
 * bit1 : bInv
 * bit0 : carryIn
 * <p>
 * output
 * bit4 : carryOut
 * bit3-0 : data
 * <p>
 * <p>
 * Created by vfumo on 3/13/16.
 */
public class BusBasedALU extends Component implements BusListener {

    private static Logger logger = LoggerFactory.getLogger(BusBasedALU.class);

    private Bus aluAin;
    private Bus aluBin;
    private Bus aluControl;
    private Bus aluOut;

    // internal
    private BitField out;
    private BitField inA;
    private BitField inB;
    private BitField control;

    public BusBasedALU() {
        super();
    }

    public BusBasedALU(int size, String name) {
        super(size, name);
        init();
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

    /**
     * helper to generate a control bitfield.
     * <p>
     * bit 3,2 are the operation : 00 == ADD, 01 == OR, 10 == AND, 11 == XOR
     * bit 1 is bInvert
     * bit 0 is carryIn
     *
     * @param op      the desired operation
     * @param bInv    the bInvert value
     * @param carryIn the carryIn value
     * @return a properly formatted control bitfield
     */
    public static BitField codeControlField(ALUOperation op, boolean bInv, boolean carryIn) {
        BitField controlSignal = BitFieldBuilder.createWithSize(4);

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

    public void init() {
        out =  BitFieldBuilder.createWithSize(size + 1);
        inA =  BitFieldBuilder.createWithSize(size);
        inB =  BitFieldBuilder.createWithSize(size);
        control =  BitFieldBuilder.createWithSize(4);
    }

    @Override
    public void dataChanged(Bus c) {
        BitField changedData = c.getData();

        boolean changed = false;
        if (c.equals(aluAin)) {
            logger.debug("AIn updated to : {}", changedData);
            this.inA = changedData;
            changed = true;
        } else if (c.equals(aluBin)) {
            logger.debug("BIn updated to : {}", changedData);
            this.inB = changedData;
            changed = true;
        } else if (c.equals(aluControl)) {
            logger.debug("Ctrl updated to : {}", changedData);
            this.control = changedData;
            changed = true;
        }

        if (changed) {
            prep();

            ALUOperation op = decodeOperation(control);
            boolean bInv = decodeBInvert(control);
            boolean carryIn = decodeCarryIn(control);

            out = compute(inA, inB, op, bInv, carryIn);
            aluOut.updateData(out);
        }
    }

    /**
     * prep for the ALU to compute:
     * <p>
     * 1) size the input busses
     * 2) ??
     */
    private void prep() {
        inA.resize(size);
        inB.resize(size);
    }

    protected BitField compute(BitField inA, BitField inB, ALUOperation op, boolean bInv, boolean carryIn) {

        BitField actaulB = inB.copy();
        if (bInv) {
            actaulB.invertAllBits();
        }

        boolean carryOut = false;
        switch (op) {
            case ADD:
                carryOut = doAddition(inA, actaulB, carryIn);
                break;
            case OR:
                process(inA, actaulB, BusBasedALU::or);
                carryOut = false;
                break;
            case AND:
                process(inA, actaulB, BusBasedALU::and);
                carryOut = false;
                break;
            case XOR:
                process(inA, actaulB, BusBasedALU::xor);
                carryOut = false;
                break;
        }

        out.setBit(4, carryOut);
        if (logger.isDebugEnabled()) {
            logger.debug("compute() " + getStateString(inA, inB, op, bInv, carryIn, out));
        }
        return out;
    }

    /**
     * @param a
     * @param b
     * @param carry
     * @return true if we overflow
     */
    protected boolean doAddition(BitField a, BitField b, boolean carry) {
        for (int i = 0; i < a.size(); i++) {
            boolean bitA = a.getBitAsBoolean(i);
            boolean bitB = b.getBitAsBoolean(i);
            boolean result = add(bitA, bitB, carry);
            carry = carry(bitA, bitB, carry);
            out.setBit(i, result);
        }
        return carry;
    }

    protected void process(BitField a, BitField b, BiFunction<Boolean, Boolean, Boolean> function) {
        for (int i = 0; i < a.size(); i++) {
            boolean bitA = a.getBitAsBoolean(i);
            boolean bitB = b.getBitAsBoolean(i);

            out.setBit(i, function.apply(bitA, bitB));
        }
    }

    private ALUOperation decodeOperation(BitField control) {
        ALUOperation op = null;

        int intValue = control.getMSBs(2).intValue();
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
        return op;
    }

    private boolean decodeBInvert(BitField control) {
        return control.getBitAsBoolean(1);
    }

    private boolean decodeCarryIn(BitField control) {
        return control.getBitAsBoolean(0);
    }

    private String getStateString(BitField inA, BitField inB, ALUOperation op, boolean bInv, boolean carryIn, BitField out) {
        StringBuffer b = new StringBuffer();

        b.append(op);
        b.append(' ');
        if (bInv) b.append('B');
        else b.append(' ');
        if (carryIn) b.append('C');
        else b.append(' ');

        b.append(' ');
        b.append("a=");
        b.append(inA);
        b.append(' ');
        b.append("b=");
        b.append(inB);
        b.append(' ');
        b.append("o=");
        b.append(out);

        return b.toString();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Bus getAluAin() {
        return aluAin;
    }

    public void setAluAin(Bus aluAin) {
        this.aluAin = aluAin;
        this.aluAin.addListener(this);
    }

    public Bus getAluBin() {
        return aluBin;
    }

    public void setAluBin(Bus aluBin) {
        this.aluBin = aluBin;
        this.aluBin.addListener(this);
    }

    public Bus getAluControl() {
        return aluControl;
    }

    public void setAluControl(Bus aluControl) {
        this.aluControl = aluControl;
        this.aluControl.addListener(this);
    }

    public Bus getAluOut() {
        return aluOut;
    }

    public void setAluOut(Bus aluOut) {
        this.aluOut = aluOut;
        this.aluOut.addListener(this);
    }
}
