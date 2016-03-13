package com.neodem.relaySim.objects;

import com.neodem.relaySim.objects.tools.BitTools;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by vfumo on 3/13/16.
 */
public class ALUTest {

    private ALU alu;

    @BeforeMethod
    public void before() {
        alu = new ALU();
    }

    @AfterMethod
    public void after() {
        alu = null;
    }

    @Test
    public void doAdditionShoudAddStuff() throws Exception {
        ALUConnector c = new ALUConnector();
        c.setAInput(0, 0, 0, 1);
        c.setBInput(0, 0, 0, 1);
        c.setCarryIn(0);

        alu.doAddition(c);

        assertThat(c.getCarryOut()).isEqualTo(0);
        assertThat(c.getOutput().get(0)).isEqualTo(0);
        assertThat(c.getOutput().get(1)).isEqualTo(1);
        assertThat(c.getOutput().get(2)).isEqualTo(0);
        assertThat(c.getOutput().get(3)).isEqualTo(0);
    }

    @Test
    public void doAdditionShoudAddStuff1() throws Exception {
        ALUConnector c = new ALUConnector();
        c.setAInput(0, 0, 1, 0);
        c.setBInput(0, 0, 1, 0);
        c.setCarryIn(0);

        alu.doAddition(c);

        assertThat(c.getCarryOut()).isEqualTo(0);
        assertThat(c.getOutput().get(0)).isEqualTo(0);
        assertThat(c.getOutput().get(1)).isEqualTo(0);
        assertThat(c.getOutput().get(2)).isEqualTo(1);
        assertThat(c.getOutput().get(3)).isEqualTo(0);
    }

    @Test(dataProvider = "aluAdd")
    public void doAdditionShoudAddStuff3() throws Exception {
        ALUConnector c = new ALUConnector();
        c.setAInput(0, 0, 1, 1);
        c.setBInput(0, 0, 0, 1);
        c.setCarryIn(0);

        alu.doAddition(c);

        assertThat(c.getOutput().get(0)).isEqualTo(0);
        assertThat(c.getOutput().get(1)).isEqualTo(0);
        assertThat(c.getOutput().get(2)).isEqualTo(0);
        assertThat(c.getOutput().get(3)).isEqualTo(0);
        assertThat(c.getCarryOut()).isEqualTo(1);
    }

    private void put(int value, List<List<Integer>> list) {
        List<Integer> val = BitTools.convertToList(value, 4);
        list.add(val);
    }

    @DataProvider(name = "aluAdd")
    public Object[][] aluAdd() {
        List<List<Integer>> aVals = new ArrayList<>();
        List<List<Integer>> bVals = new ArrayList<>();
        List<List<Integer>> outVals = new ArrayList<>();

        int out;
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 8; b++) {
                out = a + b;
                put(a, aVals);
                put(b, bVals);
                put(out, outVals);
            }
        }

        List<List<Integer>> vals = new ArrayList();
        for (int i = 0; i < 65; i++) {
            List<Integer> row = new ArrayList<>();
            row.addAll(aVals.get(i));
            row.addAll(bVals.get(i));
            row.addAll(outVals.get(i));
            vals.add(row);
        }


        return new Object[][]{
                // a3,a2,a1,a0 b3,b2,b1,b0, o3,o2,o1,o0, cin,cout
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
    }


}
