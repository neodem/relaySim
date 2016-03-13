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

    private void doAddition(ALUConnector connector) {
        List<Boolean> a = connector.getAInputs();
        List<Boolean> b = connector.getBInputs();
        add(a, b, connector);
    }

    private void add(List<Boolean> a, List<Boolean> b, ALUConnector connector) {
        boolean carry = connector.getCarryIn();
        List<Boolean> output = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            Boolean bitA = a.get(i);
            Boolean bitB = b.get(i);
            if (bitA && bitB) {
                output.add(i, carry);
                carry = true;
            } else if (bitA || bitB) {
                if (carry) {
                    carry = false;
                }
                output.add(i, true);
            }
                else output.add(i, carry);

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
