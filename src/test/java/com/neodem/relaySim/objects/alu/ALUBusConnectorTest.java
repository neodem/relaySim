package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.BitField4;
import com.neodem.relaySim.objects.bus.Bus;
import com.neodem.relaySim.objects.bus.BusFactory;
import com.neodem.relaySim.objects.bus.BusNames;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public class ALUBusConnectorTest {

    private ALUBusConnector connector;
    private ALU alu;
    private BusFactory busFactory;

    @BeforeMethod
    public void before() {
        busFactory = new BusFactory();
        alu = new ALU();
        connector = new ALUBusConnector(alu, busFactory);
    }

    @AfterMethod
    public void after() {
        connector = null;
        alu = null;
        busFactory = null;
    }

    @Test
    public void something() throws Exception {
        Bus aluAin = busFactory.getBus(BusNames.ALU_AIN, 4);
        Bus aluBin = busFactory.getBus(BusNames.ALU_BIN, 4);
        Bus aluControl = busFactory.getBus(BusNames.ALU_CTRL, 4);
        Bus aluOut = busFactory.getBus(BusNames.ALU_OUT, 4);

        BitField ain = new BitField4(0,1,1,1);
        aluAin.updateData(ain);

        BitField bin = new BitField4(0,0,0,1);
        aluBin.updateData(bin);

        BitField result = aluOut.getData();

    }
}
