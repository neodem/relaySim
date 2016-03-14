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
            if (bitA && bitB) {
                output.addBit(i, carry);
                carry = true;
            } else if (bitA || bitB) {
                if (carry) {
                    carry = false;
                }
                output.addBit(i, 1);
            } else {
                output.addBit(i, carry);
                carry = false;
            }
        }

        AluOutput result = new AluOutput();

        result.setOutput(output);
        result.setCarryOut(carry);

        return result;
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
