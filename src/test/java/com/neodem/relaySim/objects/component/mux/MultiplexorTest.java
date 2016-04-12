package com.neodem.relaySim.objects.component.mux;

import com.neodem.relaySim.data.Bus;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

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
    }

    @AfterTest
    public void after() {
        mux = null;
        input0 = null;
        input1 = null;
        output = null;
    }

}
