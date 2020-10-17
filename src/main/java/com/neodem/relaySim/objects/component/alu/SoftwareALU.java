package com.neodem.relaySim.objects.component.alu;

import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/15/20
 */
public class SoftwareALU implements ALU {

    private static Logger logger = LoggerFactory.getLogger(SoftwareALU.class);

    private final int aluSize;

    public SoftwareALU() {
        this(4);
    }

    public SoftwareALU(int aluSize) {
        this.aluSize = aluSize;
    }

    @Override
    public ALUResult operate(boolean s0, boolean s1, boolean cIn, boolean bInv, BitField a, BitField b) {
        BitField actaulB = b.copy();
        if (bInv) {
            actaulB.invertAllBits();
        }

        ALUResult result;

        if(s0) {
            if(s1) {
                result = process(a, actaulB, this::xor);
            } else {
                result = process(a, actaulB, this::and);
            }
        } else {
            if(s1) {
                result = process(a, actaulB, this::or);
            } else {
                result = doAddition(a, actaulB, cIn);
            }
        }

        logger.info("{}", makeStatus(s0, s1, cIn, bInv, a, b, result));
        return result;
    }

    private String makeStatus(boolean s0, boolean s1, boolean cIn, boolean bInv, BitField a, BitField bIn, ALUResult res) {
        StringBuffer b = new StringBuffer();

        b.append(s0 ? '1' : '0');
        b.append(s1 ? '1' : '0');
        b.append(bInv ? 'B' : '.');
        b.append(cIn ? 'C' : '.');

        b.append(' ');
        b.append("a=");
        b.append(a);
        b.append(' ');
        b.append("b=");
        b.append(bIn);
        b.append(' ');
        b.append("o=");

        b.append(res.isOverflow() ? "OVER" : "");
        b.append(res.isCarryOut() ? "1" : "");
        b.append(res.getResult());

        return b.toString();
    }

    protected ALUResult process(BitField a, BitField b, BiFunction<Boolean, Boolean, Boolean> function) {
        BitField out =  BitFieldBuilder.createWithSize(aluSize);

        for (int i = 0; i < a.size(); i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);
            boolean result = function.apply(bitA, bitB);
            out.setBit(i, result);
        }

        return new ALUResult(out);
    }

    /**
     * @param a
     * @param b
     * @param carryIn
     * @return true if we overflow
     */
    protected ALUResult doAddition(BitField a, BitField b, boolean carryIn) {

        BitField out = BitFieldBuilder.createWithSize(aluSize);
        boolean carry = carryIn;

        for (int i = 0; i < a.size(); i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);
            boolean result = add(bitA, bitB, carry);
            out.setBit(i, result);

            carry = carry(bitA, bitB, carry);
        }
        return new ALUResult(out, carry);
    }

    protected boolean or(boolean a, boolean b) {
        return a || b;
    }

    protected boolean and(boolean a, boolean b) {
        return a && b;
    }

    protected boolean xor(boolean a, boolean b) {
        if (a || b) return !(a && b);
        return false;
    }

    protected boolean add(boolean a, boolean b, boolean carryIn) {
        boolean result = xor(a, b);
        if (carryIn) return !result;
        return result;
    }

    protected boolean carry(boolean a, boolean b, boolean carryIn) {
        if (carryIn) return (a || b);
        return (a && b);
    }
}
