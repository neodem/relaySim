package com.neodem.relaySim.objects;

import java.util.function.BiFunction;

/**
 * Created by vfumo on 3/13/16.
 */
public class ALU {

    public AluOutput compute(AluInput input) {
        ALUOperation operation = input.getOperation();
        AluOutput result = null;
        switch (operation) {
            case ADD:
                result = doAddition(input);
                break;
            case OR:
                result = process(input, ALU::or);
                break;
            case AND:
                result = process(input, ALU::and);
                break;
            case XOR:
                result = process(input, ALU::xor);
                break;
        }

        return result;
    }

    protected AluOutput doAddition(AluInput input) {
        BitField a = input.getA();
        BitField b = input.getB();
        boolean carry = input.getCarryIn();
        BitField output = new BitField(4);
        for (int i = 0; i < 4; i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);

            boolean result = add(bitA, bitB, carry);
            carry = carry(bitA, bitB, carry);

            output.setBit(i, result);
        }

        AluOutput result = new AluOutput();

        result.setOutput(output);
        result.setCarryOut(carry);

        return result;
    }

    protected AluOutput process(AluInput input, BiFunction<Boolean, Boolean, Boolean> function) {
        BitField a = input.getA();
        BitField b = input.getB();
        BitField output = new BitField(4);
        for (int i = 0; i < 4; i++) {
            boolean bitA = a.getBit(i);
            boolean bitB = b.getBit(i);

            boolean result = function.apply(bitA, bitB);

            output.setBit(i, result);
        }

        AluOutput result = new AluOutput();
        result.setOutput(output);

        return result;
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
}
