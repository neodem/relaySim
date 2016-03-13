package com.neodem.relaySim.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vfumo on 3/13/16.
 */
public class ALU implements ConnectorListener {

    private ALUConnector connector;

    public void modified() {
        calculate();
    }

    private void calculate() {
        ALUOperation operation = connector.getOperation();
        switch (operation) {
            case ADD:
                doAddition(connector);
                break;
            case OR:
                doOr(connector);
                break;
            case AND:
                doAnd(connector);
                break;
            case XOR:
                doXor(connector);
                break;
        }
    }

    protected void doAddition(ALUConnector connector) {
        List<Integer> a = connector.getAInput();
        List<Integer> b = connector.getBInput();
        int carry = connector.getCarryIn();
        List<Integer> output = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            int bitA = a.get(i);
            int bitB = b.get(i);
            if (bitA == 1 && bitB == 1) {
                output.add(i, carry);
                carry = 1;
            } else if (bitA == 1 || bitB == 1) {
                if (carry == 1) {
                    carry = 0;
                }
                output.add(i, 1);
            } else {
                output.add(i, carry);
                carry = 0;
            }
        }
        connector.setOutput(output);
        connector.setCarryOut(carry);
    }

    private void doOr(ALUConnector connector) {
    }

    private void doAnd(ALUConnector connector) {
    }

    private void doXor(ALUConnector connector) {
    }

    public void setConnector(ALUConnector connector) {
        this.connector = connector;
        this.connector.registerListener(this);
    }
}
