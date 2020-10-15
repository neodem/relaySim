package com.neodem.relaySim.objects.component.alu;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.tools.BitTools;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by vfumo on 3/13/16.
 */
public class SoftwareALUTest {

    private SoftwareALU alu;

    @BeforeTest
    public void before() {
        alu = new SoftwareALU(4);
    }

    @AfterTest
    public void after() {
        alu = null;
    }

    @Test
    public void anAddShouldWorkNoCarry() throws Exception {

        boolean s0 = false;
        boolean s1 = false;
        boolean cIn = false;
        boolean bInv = false;

        BitField a = BitField.create(0, 0, 0, 0);
        BitField b = BitField.create(0, 0, 0, 0);
        ALUResult result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(0, 0, 0, 1);
        b = BitField.create(0, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 0, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(0, 0, 0, 0);
        b = BitField.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 0, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(0, 0, 1, 0);
        b = BitField.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 1, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(1, 0, 1, 0);
        b = BitField.create(0, 1, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(1, 1, 1, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();
    }

    @Test
    public void anAddShouldWorkWithCarryIn() throws Exception {
        boolean s0 = false;
        boolean s1 = false;
        boolean cIn = true;
        boolean bInv = false;

        BitField a = BitField.create(0, 0, 0, 0);
        BitField b = BitField.create(0, 0, 0, 0);
        ALUResult result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 0, 1));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(0, 0, 0, 1);
        b = BitField.create(0, 0, 0, 0);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 1, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(0, 0, 0, 0);
        b = BitField.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 1, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(0, 0, 1, 0);
        b = BitField.create(0, 0, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 1, 0, 0));
        assertThat(result.isCarryOut()).isFalse();
        assertThat(result.isOverflow()).isFalse();

        a = BitField.create(1, 0, 1, 0);
        b = BitField.create(0, 1, 0, 1);
        result = alu.operate(s0, s1, cIn, bInv, a, b);
        assertThat(result.getResult()).isEqualTo(BitField.create(0, 0, 0, 0));
        assertThat(result.isCarryOut()).isTrue();
        assertThat(result.isOverflow()).isFalse();
    }

    @Test
    public void orShouldWorkAsExpected() throws Exception {
        assertThat(alu.or(false, false)).isFalse();
        assertThat(alu.or(true, false)).isTrue();
        assertThat(alu.or(false, true)).isTrue();
        assertThat(alu.or(true, true)).isTrue();
    }

    @Test
    public void xorShouldWorkAsExpected() throws Exception {
        assertThat(alu.xor(false, false)).isFalse();
        assertThat(alu.xor(true, false)).isTrue();
        assertThat(alu.xor(false, true)).isTrue();
        assertThat(alu.xor(true, true)).isFalse();
    }

    @Test
    public void andShouldWorkAsExpected() throws Exception {
        assertThat(alu.and(false, false)).isFalse();
        assertThat(alu.and(true, false)).isFalse();
        assertThat(alu.and(false, true)).isFalse();
        assertThat(alu.and(true, true)).isTrue();
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
