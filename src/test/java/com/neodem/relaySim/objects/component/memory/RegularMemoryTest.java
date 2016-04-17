package com.neodem.relaySim.objects.component.memory;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.data.Bus;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class RegularMemoryTest {

    private Memory mem;

    private Bus addressBus;
    private Bus dataBus;

    @BeforeTest
    public void before() throws Exception {
        addressBus = new Bus(12, "memAddress");
        dataBus = new Bus(4, "memData");

        mem = new RegularMemory(12, 4, 100);
        mem.setAddressBus(addressBus);
        mem.setDataBus(dataBus);
        mem.chipSelect(true);
    }

    @AfterTest
    public void after() {
        mem = null;
        addressBus = null;
        dataBus = null;
    }

    @Test
    public void memoryShouldInitToZero() throws Exception {
        for (int i = 0; i < 100; i++) {
            addressBus.updateData(BitField.createFromInt(i));
            mem.read(true);
            assertThat(dataBus.getData().intValue()).isEqualTo(0);
        }
    }

    @Test
    public void memoryShouldStoreValue() throws Exception {
        addressBus.updateData(BitField.createFromInt(7));
        mem.read(true);
        assertThat(dataBus.getData().intValue()).isEqualTo(0);

        dataBus.updateData(BitField.create(1, 0, 0, 1));
        mem.write(true);

        mem.read(true);
        assertThat(dataBus.getData().intValue()).isEqualTo(9);
    }

}
