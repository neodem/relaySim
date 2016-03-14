package com.neodem.relaySim.objects;

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
                result = doOr(input);
                break;
            case AND:
                result = doAnd(input);
                break;
            case XOR:
                result = doXor(input);
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

    protected boolean add(boolean a, boolean b, boolean carry) {
        boolean result = xor(a, b);
        if (carry) return !result;
        return result;
    }

    protected boolean carry(boolean a, boolean b, boolean carry) {
        if (carry) return (a || b);
        return (a && b);
    }

    protected boolean xor(boolean a, boolean b) {
        if (a || b) return !(a && b);
        return false;
    }

    private AluOutput doOr(AluInput input) {
        return null;
    }

    private AluOutput doAnd(AluInput input) {
        return null;
    }

    private AluOutput doXor(AluInput input) {
        return null;
    }

}
