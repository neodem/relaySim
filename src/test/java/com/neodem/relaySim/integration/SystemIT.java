package com.neodem.relaySim.integration;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.alu.ALU;
import com.neodem.relaySim.objects.bus.Bus;
import com.neodem.relaySim.objects.register.Register;
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
    private ALU alu;

    @Resource(name = "accumulatorInBus")
    private Bus accumulatorBus;

    @Resource(name = "aluControlBus")
    private Bus aluControlBus;

    @Resource(name = "aluABus")
    private Bus aluAInBus;

    @Resource(name = "aluBBus")
    private Bus aluBInBus;

    @Resource(name = "aluOutBus")
    private Bus aluOutBus;

    @Resource
    private Register accumulator;

    @Test
    public void wiringTest() throws Exception {
        // load 0,0,0,0 into A side of ALU (hijacking the bus, which is normally connected only to the Accumulator)
        aluAInBus.updateData(BitField.create(0,0,0,0));

        // load 0,0,1,0 into B side of ALU
        aluBInBus.updateData(BitField.create(0,0,1,0));

        // set ALU operation to ADD with no carryIn
        aluControlBus.updateData(BitField.create(0,0,0,0));

        // check result (should be the same as B)
        assertThat(aluOutBus.getData()).isEqualTo(BitField.create(0,0,0,1,0));

        // load 1,0,0,1 into the Accumulator
        accumulatorBus.updateData(BitField.create(1, 0, 0, 1));

        // check ALU out (should not change)
        assertThat(aluOutBus.getData()).isEqualTo(BitField.create(0,0,0,1,0));

        // trigger the store op of the Register
        accumulator.store();

        // check ALU out (should do the add)
        assertThat(aluOutBus.getData()).isEqualTo(BitField.create(0,1,0,1,1));
    }
}
