package com.neodem.relaySim.data;

import com.neodem.relaySim.data.bitfield.BitField;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/12/16
 */
public class BusConnection implements BusListener {
    private Bus busA;
    private Bus busB;

    @Override
    public void dataChanged(Bus b) {
        BitField changedData = b.getData();

        if (b.equals(busA)) {
            busA.updateData(changedData);
        } else if (b.equals(busB)) {
            busB.updateData(changedData);
        }
    }

    public void setBusA(Bus busA) {
        this.busA = busA;
        this.busA.addListener(this);
    }

    public void setBusB(Bus busB) {
        this.busB = busB;
        this.busB.addListener(this);
    }
}
