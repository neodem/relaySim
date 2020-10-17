package com.neodem.relaySim.integration;

import com.neodem.relaySim.data.BitFieldBuilder;
import com.neodem.relaySim.objects.component.alu.BusBasedALU;
import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.objects.component.register.RegularRegister;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/12/16
 */
@ContextConfiguration(locations = "/test-system-context.xml")
public class SystemIT extends AbstractTestNGSpringContextTests {

    @Resource
    private BusBasedALU alu;

    @Resource
    private RegularRegister accumulator;

    @Test
    public void wiringTest() throws Exception {
        Bus aluAin = alu.getAluAin();
        Bus aluBin = alu.getAluBin();
        Bus aluOut = alu.getAluOut();
        Bus aluControl = alu.getAluControl();

        Bus accumulatorIn = accumulator.getInBus();
        Bus accumulatorOut = accumulator.getOutBus();

        // start state has everything at 0
        assertThat(aluOut.getData().intValue()).isEqualTo(0);
        assertThat(aluAin.getData().intValue()).isEqualTo(0);
        assertThat(aluBin.getData().intValue()).isEqualTo(0);

        assertThat(accumulatorIn.getData().intValue()).isEqualTo(0);
        assertThat(accumulatorOut.getData().intValue()).isEqualTo(0);

        // set ALU operation to ADD with no carryIn
        aluControl.updateData(BitFieldBuilder.create(0,0,0,0));

        // load 1,0,0,1 into B side of ALU
        aluBin.updateData(BitFieldBuilder.create(1,0,0,1));

        // store output of the ALU (which is wired into the accumulator)

        accumulator.chipSelect(true);
        accumulator.write(true);

        // check ALU out (should have the new value/out from the ALU)
        assertThat(accumulatorOut.getData()).isEqualTo(BitFieldBuilder.create(1,0,0,1));
    }
}
