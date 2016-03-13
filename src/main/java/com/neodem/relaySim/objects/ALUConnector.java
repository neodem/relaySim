package com.neodem.relaySim.objects;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by vfumo on 3/13/16.
 */
public class ALUConnector extends Connector {
    private ALUOperation operation;
    private boolean carryOut;
    private Boolean carryIn = false;
    private List<Boolean> output;

    public ALUOperation getOperation() {
        return operation;
    }

    public List<Boolean> getAInputs() {
        return Lists.newArrayList(false, false, false, true); // 0001
    }

    public List<Boolean> getBInputs() {
        return Lists.newArrayList(false, false, false, true); // 0001
    }

    public Boolean getCarryIn() {
        return carryIn;
    }

    public void setCarryOut(boolean carryOut) {
        trigger();
        this.carryOut = carryOut;
    }

    public void setOutput(List<Boolean> output) {
        trigger();
        this.output = output;
    }

}
