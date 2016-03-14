package com.neodem.relaySim.objects;

import com.neodem.relaySim.tools.BitTools;
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
    public void addShouldWork() throws Exception {
        assertThat(alu.add(false, false, false)).isFalse();
        assertThat(alu.add(true, false, false)).isTrue();
        assertThat(alu.add(false, true, false)).isTrue();
        assertThat(alu.add(true, true, false)).isFalse();

        assertThat(alu.add(false, false, true)).isTrue();
        assertThat(alu.add(true, false, true)).isFalse();
        assertThat(alu.add(false, true, true)).isFalse();
        assertThat(alu.add(true, true, true)).isTrue();
    }

    @Test
    public void carryShouldWork() throws Exception {
        assertThat(alu.carry(false, false, false)).isFalse();
        assertThat(alu.carry(true, false, false)).isFalse();
        assertThat(alu.carry(false, true, false)).isFalse();
        assertThat(alu.carry(true, true, false)).isTrue();

        assertThat(alu.carry(false, false, true)).isFalse();
        assertThat(alu.carry(true, false, true)).isTrue();
        assertThat(alu.carry(false, true, true)).isTrue();
        assertThat(alu.carry(true, true, true)).isTrue();
    }

    @Test
    public void xorShouldWork() throws Exception {
        assertThat(alu.xor(false, false)).isFalse();
        assertThat(alu.xor(true, false)).isTrue();
        assertThat(alu.xor(false, true)).isTrue();
        assertThat(alu.xor(true, true)).isFalse();
    }

    @Test(dataProvider = "aluAdd")
    public void doAdditionShoudAddWithNoCarry(int a0, int a1, int a2, int a3, int b0, int b1, int b2, int b3) {

        AluInput input = new AluInput();
        input.setA(a3, a2, a1, a0);
        input.setB(b3, b2, b1, b0);
        input.setCarryIn(false);
        input.setOperation(ALUOperation.ADD);

        int aInt = input.getA().intValue();
        int bInt = input.getB().intValue();
        int expected = aInt + bInt;

        AluOutput out = alu.compute(input);
        System.out.println(input + " " + out);

        assertThat(out.getOutput().getBit(0)).isEqualTo(BitTools.bit(0,expected));
        assertThat(out.getOutput().getBit(1)).isEqualTo(BitTools.bit(1,expected));
        assertThat(out.getOutput().getBit(2)).isEqualTo(BitTools.bit(2,expected));
        assertThat(out.getOutput().getBit(3)).isEqualTo(BitTools.bit(3,expected));
        assertThat(out.getCarryOut()).isEqualTo(BitTools.bit(4,expected));
    }

    @DataProvider(name = "aluAdd")
    public Object[][] aluAdd() {
        List<List<Integer>> aVals = new ArrayList<>();
        List<List<Integer>> bVals = new ArrayList<>();

        for (int a = 0; a < 16; a++) {
            for (int b = 0; b < 16; b++) {
                List<Integer> val = BitTools.convertToList(a, 4);
                aVals.add(val);

                val = BitTools.convertToList(b, 4);
                bVals.add(val);
            }
        }

        Object[][] all = new Object[256][];
        for (int i = 0; i < 256; i++) {
            List<Integer> row = new ArrayList<>();
            row.addAll(aVals.get(i));
            row.addAll(bVals.get(i));

            all[i] = row.toArray(new Object[row.size()]);
        }

        // a0,a1,a2,a3,b0,b1,b2,b3
        return all;
    }
}
