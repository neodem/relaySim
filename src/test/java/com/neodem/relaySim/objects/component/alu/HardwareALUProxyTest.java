package com.neodem.relaySim.objects.component.alu;

import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import com.neodem.relaySim.tools.BitTools;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/17/20
 */
public class HardwareALUProxyTest {

    private HardwareALU alu;

    @BeforeTest
    public void before() {
        alu = new HardwareALU();
    }

    @AfterTest
    public void after() {
        alu = null;
    }

    @Test
    public void testAdd() {

        boolean s0 = false;
        boolean s1 = false;
        boolean cIn = false;
        boolean bInv = false;

        BitField a = BitFieldBuilder.create(1, 1, 1, 0);
        BitField b = BitFieldBuilder.create(0, 0, 0, 1);
        ALUResult result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(1, 1, 1, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();
    }


    @Test
    public void whyDoesThisNotWork() {

        boolean s0 = false;
        boolean s1 = false;
        boolean cIn = false;
        boolean bInv = false;

        BitField a = BitFieldBuilder.create(1, 0, 1, 0);
        BitField b = BitFieldBuilder.create(0, 1, 0, 1);
        ALUResult result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(1, 1, 1, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();
    }

    @Test
    public void anAddShouldWorkNoCarry() throws Exception {

        boolean s0 = false;
        boolean s1 = false;
        boolean cIn = false;
        boolean bInv = false;

        BitField a = BitFieldBuilder.create(0, 0, 0, 0);
        BitField b = BitFieldBuilder.create(0, 0, 0, 0);
        ALUResult result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 0, 1);
        b = BitFieldBuilder.create(0, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 0, 0);
        b = BitFieldBuilder.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 1, 0);
        b = BitFieldBuilder.create(0, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 1, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 0, 0);
        b = BitFieldBuilder.create(0, 0, 1, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 1, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 1, 0, 0);
        b = BitFieldBuilder.create(0, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 1, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 0, 0);
        b = BitFieldBuilder.create(0, 1, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 1, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(1, 0, 0, 0);
        b = BitFieldBuilder.create(0, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(1, 0, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 0, 0);
        b = BitFieldBuilder.create(1, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(1, 0, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 1, 0);
        b = BitFieldBuilder.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 1, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(1, 0, 1, 0);
        b = BitFieldBuilder.create(0, 1, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(1, 1, 1, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();
    }

    @Test
    public void anAddShouldWorkWithCarryIn() throws Exception {
        boolean s0 = false;
        boolean s1 = false;
        boolean cIn = true;
        boolean bInv = false;

        BitField a = BitFieldBuilder.create(0, 0, 0, 0);
        BitField b = BitFieldBuilder.create(0, 0, 0, 0);
        ALUResult result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 0, 1);
        b = BitFieldBuilder.create(0, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 1, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 0, 0);
        b = BitFieldBuilder.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 1, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 0, 1, 0);
        b = BitFieldBuilder.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 1, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(1, 0, 1, 0);
        b = BitFieldBuilder.create(0, 1, 0, 1);
        result = alu.operate(s0, s1, false, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 0));
        assertThat(result.isCarryOut()).isTrue();
        assertThat(result.isOverflow()).isFalse();

        a = BitFieldBuilder.create(0, 1, 0, 1);
        b = BitFieldBuilder.create(1, 1, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 1));
        assertThat(result.isCarryOut()).isTrue();
        assertThat(result.isOverflow()).isFalse();
    }

    @Test
    public void anAddShouldWorkCheckCarryOut() throws Exception {
        boolean s0 = false;
        boolean s1 = false;
        boolean cIn = false;
        boolean bInv = false;

        BitField a = BitFieldBuilder.create(1, 0, 1, 0);
        BitField b = BitFieldBuilder.create(0, 1, 0, 1);
        ALUResult result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 0));
        assertThat(result.isCarryOut()).isTrue();
        assertThat(result.isOverflow()).isFalse();

    }

    @Test(dataProvider = "all4bits")
    public void addShouldWork(BitField a, BitField b) {

        int aInt = a.intValue();
        int bInt = b.intValue();
        int expected = aInt + bInt;

        ALUResult out = alu.operate(false, false, false, false, a, b);

        int result = out.getResult().intValue();

        if (expected >= 16) {
            assertThat(result).isEqualTo(expected - 16);
            assertThat(out.isCarryOut()).isTrue();
        } else {
            assertThat(result).isEqualTo(expected);
            assertThat(out.isCarryOut()).isFalse();
        }
        assertThat(out.isOverflow()).isFalse();
    }

    @Test(dataProvider = "all4bits")
    public void addShouldWorkWithCarryIn(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();
        int expected = aInt + bInt + 1;

        ALUResult out = alu.operate(false, false, true, false, a, b);

        int result = out.getResult().intValue();
        if (expected >= 16) {
            assertThat(result).isEqualTo(expected - 16);
            assertThat(out.isCarryOut()).isTrue();
        } else {
            assertThat(result).isEqualTo(expected);
            assertThat(out.isCarryOut()).isFalse();
        }
        assertThat(out.isOverflow()).isFalse();
    }

    @Test(dataProvider = "all4bits")
    public void orShoudWork(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt | bInt;

        ALUResult out = alu.operate(false, true, false, false, a, b);

        int result = out.getResult().intValue();
        assertThat(result).isEqualTo(expected);
        assertThat(out.isCarryOut()).isFalse();
        assertThat(out.isOverflow()).isFalse();
    }

    @Test(dataProvider = "all4bits")
    public void andShoudWork(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt & bInt;

        ALUResult out = alu.operate(true, false, false, false, a, b);

        int result = out.getResult().intValue();
        assertThat(result).isEqualTo(expected);
        assertThat(out.isCarryOut()).isFalse();
        assertThat(out.isOverflow()).isFalse();
    }

    @Test(dataProvider = "all4bits")
    public void xorShoudWork(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt ^ bInt;

        ALUResult out = alu.operate(true, true, false, false, a, b);

        int result = out.getResult().intValue();
        assertThat(result).isEqualTo(expected);
        assertThat(out.isCarryOut()).isFalse();
        assertThat(out.isOverflow()).isFalse();
    }

    @DataProvider(name = "all4bits")
    public Object[][] aluAdd() {
        List<BitField> aVals = BitTools.makeListOfFields(4);
        List<BitField> bVals = BitTools.makeListOfFields(4);

        Object[][] all = new Object[256][];
        int index = 0;
        for (int a = 0; a < 16; a++) {
            for (int b = 0; b < 16; b++) {
                List<BitField> row = new ArrayList<>();
                row.add(aVals.get(a));
                row.add(bVals.get(b));

                all[index++] = row.toArray(new Object[row.size()]);
            }
        }

        return all;
    }
}
