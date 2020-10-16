package com.neodem.relaySim.objects.component.memory;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.data.ListBasedBitField;
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

        mem = new RegularMemory("testRAM", 12, 4, 100);
        addressBus = mem.getAddressBus();
        dataBus = mem.getDataBus();
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
            addressBus.updateData(ListBasedBitField.createFromInt(i));
            mem.read(true);
            assertThat(dataBus.getData().intValue()).isEqualTo(0);
        }
    }

    @Test
    public void memoryShouldStoreValue() throws Exception {
        addressBus.updateData(ListBasedBitField.createFromInt(7));
        mem.read(true);
        assertThat(dataBus.getData().intValue()).isEqualTo(0);

        dataBus.updateData(ListBasedBitField.create(1, 0, 0, 1));
        mem.write(true);

        mem.read(true);
        assertThat(dataBus.getData().intValue()).isEqualTo(9);
    }

}
