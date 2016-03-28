package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.*;
import com.neodem.relaySim.objects.bus.Bus;
import com.neodem.relaySim.objects.bus.BusFactory;
import com.neodem.relaySim.objects.bus.BusNames;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public class ALUBusConnector implements Listener {
    private ALU alu;
    private BusFactory busFactory;

    private Bus aluAin;
    private Bus aluBin;
    private Bus aluControl;

    private Bus aluOut;

    public ALUBusConnector(ALU alu, BusFactory busFactory) {
        this.busFactory = busFactory;
        this.alu = alu;

        aluAin = this.busFactory.getBus(BusNames.ALU_AIN, 4);
        aluAin.addListener(this);

        aluBin = this.busFactory.getBus(BusNames.ALU_BIN, 4);
        aluBin.addListener(this);

        aluControl = this.busFactory.getBus(BusNames.ALU_CTRL, 4);
        aluControl.addListener(this);

        aluOut = this.busFactory.getBus(BusNames.ALU_OUT, 4);
        aluOut.addListener(this);
    }

    @Override
    public void changed(Changer c) {
        BitField bitField = c.getData();

        boolean changed = false;
        if (c.equals(aluAin)) {
            alu.setInA(bitField);
            changed = true;
        } else if (c.equals(aluBin)) {
            alu.setInB(bitField);
            changed = true;
        } else if (c.equals(aluControl)) {
            alu.setControl(bitField);
            changed = true;
        }

        if(changed) {
            BitField out = alu.getOut();
            aluOut.updateData(out);
        }
    }
}
