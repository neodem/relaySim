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

    @Test(dataProvider = "aluAdd")
    public void doAdditionShoudAddStuff3(int a0, int a1, int a2, int a3, int b0, int b1, int b2, int b3,
                                         int o0, int o1, int o2, int o3, int cout) throws Exception {

        AluInput input = new AluInput();
        input.setA(a3, a2, a1, a0);
        input.setB(b3, b2, b1, b0);
        input.setCarryIn(false);
        input.setOperation(ALUOperation.ADD);

        AluOutput out = alu.compute(input);
        System.out.println(input + " " + out);

        assertThat(out.getOutput().getBitAsInt(0)).isEqualTo(o0);
        assertThat(out.getOutput().getBitAsInt(1)).isEqualTo(o1);
        assertThat(out.getOutput().getBitAsInt(2)).isEqualTo(o2);
        assertThat(out.getOutput().getBitAsInt(3)).isEqualTo(o3);
        assertThat(out.getCarryOutAsInt()).isEqualTo(cout);
    }

    @DataProvider(name = "aluAdd")
    public Object[][] aluAdd() {
        List<List<Integer>> aVals = new ArrayList<>();
        List<List<Integer>> bVals = new ArrayList<>();
        List<List<Integer>> outVals = new ArrayList<>();

        int out;
        for (int a = 0; a < 16; a++) {
            for (int b = 0; b < 16; b++) {
                out = a + b;

                List<Integer> val = BitTools.convertToList(a, 4);
                aVals.add(val);

                val = BitTools.convertToList(b, 4);
                bVals.add(val);

                val = BitTools.convertToList(out, 5);
                outVals.add(val);
            }
        }

        Object[][] all = new Object[256][];
        for (int i = 0; i < 256; i++) {
            List<Integer> row = new ArrayList<>();
            row.addAll(aVals.get(i));
            row.addAll(bVals.get(i));
            row.addAll(outVals.get(i));

            all[i] = row.toArray(new Object[row.size()]);
        }

        // a0,a1,a2,a3,b0,b1,b2,b3,o0,o1,o2,o3,cout
        return all;
    }


}
