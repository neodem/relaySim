package com.neodem.relaySim.objects.component.mux;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.data.Bus;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/12/16
 */
public class MultiplexorTest {

    private Multiplexor mux;

    private Bus input0;
    private Bus input1;
    private Bus output;

    @BeforeTest
    public void before() {
        input0 = new Bus(4, "input0");
        input1 = new Bus(4, "input1");
        output = new Bus(4, "output");

        mux = new Multiplexor(4, "MUX");
        mux.setInput0(input0);
        mux.setInput1(input1);
        mux.setOutput(output);

        mux.init();
    }

    @AfterTest
    public void after() {
        mux = null;
        input0 = null;
        input1 = null;
        output = null;
    }

    @Test
    public void testSwitching() throws Exception {
        input0.updateData(BitField.createFromInt(9,4));
        input1.updateData(BitField.createFromInt(1,4));
        mux.setSelected(true);
        assertThat(output).isEqualTo(BitField.createFromInt(1,4));
        mux.setSelected(false);
        assertThat(output).isEqualTo(BitField.createFromInt(9,4));

    }
}
