package com.neodem.relaySim.objects.component.alu;

import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.data.BusListener;
import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import com.neodem.relaySim.objects.component.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This will bridge the sofware interface over to the more formal hardware based interface
 * <p>
 * ALUOperation op, etc
 * <p>
 * Created by vfumo on 3/13/16.
 */
public class BusBasedALUBridge extends Component implements BusListener {

    private static Logger logger = LoggerFactory.getLogger(BusBasedALUBridge.class);

    private Bus aluAin;
    private Bus aluBin;
    private Bus aluControl;
    private Bus aluOut;

    // internal
    private BitField out;
    private BitField inA;
    private BitField inB;
    private BitField control;

    private final ALU alu;

    public BusBasedALUBridge(int size, String name, ALU alu) {
        super(size, name);
        this.alu = alu;
        init();
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
        out = BitFieldBuilder.createWithSize(size + 1);
        inA = BitFieldBuilder.createWithSize(size);
        inB = BitFieldBuilder.createWithSize(size);
        control = BitFieldBuilder.createWithSize(4);
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

    // ALUResult operate(boolean s0, boolean s1, boolean cIn, boolean bInv, BitField a, BitField b);

    protected BitField compute(BitField inA, BitField inB, ALUOperation op, boolean bInv, boolean carryIn) {
        ALUResult result = null;

        switch (op) {
            case ADD:
                result = alu.operate(false, false, carryIn, bInv, inA, inB);
                break;
            case OR:
                result = alu.operate(false, true, carryIn, bInv, inA, inB);
                break;
            case AND:
                result = alu.operate(true, false, carryIn, bInv, inA, inB);
                break;
            case XOR:
                result = alu.operate(true, true, carryIn, bInv, inA, inB);
                break;
        }

        if (result != null) {
            out = result.getResult().copy();
            out.resize(5);
            out.setBit(4, result.isCarryOut());
        }

        return out;
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
        return control.getBit(1);
    }

    private boolean decodeCarryIn(BitField control) {
        return control.getBit(0);
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
