package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.bus.Bus;
import com.neodem.relaySim.objects.bus.BusNames;
import com.neodem.relaySim.tools.BitTools;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by vfumo on 3/13/16.
 */
public class ALUTest {

    private ALU alu;

    private Bus aluAinBus;
    private Bus aluBinBus;
    private Bus aluControlBus;
    private Bus aluOutBus;

    @BeforeTest
    public void before() {
        aluAinBus = new Bus(BusNames.ALU_AIN, 4);
        aluBinBus = new Bus(BusNames.ALU_BIN, 4);
        aluControlBus = new Bus(BusNames.ALU_CTRL, 4);
        aluOutBus = new Bus(BusNames.ALU_OUT, 4);

        alu = new ALU(4);
        alu.setAluAin(aluAinBus);
        alu.setAluBin(aluBinBus);
        alu.setAluControl(aluControlBus);
        alu.setAluOut(aluOutBus);
    }

    @AfterTest
    public void after() {
        alu = null;
        aluAinBus = null;
        aluBinBus = null;
        aluControlBus = null;
        aluOutBus = null;
    }

    @Test
    public void anAddShouldWorkUsingBusses() throws Exception {
        // add, !bInv, !carryIn
        aluControlBus.updateData(new BitField(4).set(0, 0, 0, 0));
        aluAinBus.updateData(new BitField(4).set(0, 1, 1, 1)); // 7
        aluBinBus.updateData(new BitField(4).set(0, 0, 0, 1)); // 1

        assertThat(aluOutBus.getData()).isEqualTo(BitField.create(0, 1, 0, 0, 0)); //8

        // add, !bInv, carryIn
        aluControlBus.updateData(new BitField(4).set(0, 0, 0, 1));
        assertThat(aluOutBus.getData()).isEqualTo(BitField.create(0, 1, 0, 0, 1)); //9

        // add, bInv, carryIn
        aluControlBus.updateData(new BitField(4).set(0, 0, 1, 0));
        // bInv -> b == 1110 or 14
        assertThat(aluOutBus.getData()).isEqualTo(BitField.create(1, 0, 1, 0, 1)); // 21 (treating carryOut as the 5th bit)
    }

    @Test
    public void orShouldWorkAsExpected() throws Exception {
        assertThat(ALU.or(false, false)).isFalse();
        assertThat(ALU.or(true, false)).isTrue();
        assertThat(ALU.or(false, true)).isTrue();
        assertThat(ALU.or(true, true)).isTrue();
    }

    @Test
    public void xorShouldWorkAsExpected() throws Exception {
        assertThat(ALU.xor(false, false)).isFalse();
        assertThat(ALU.xor(true, false)).isTrue();
        assertThat(ALU.xor(false, true)).isTrue();
        assertThat(ALU.xor(true, true)).isFalse();
    }

    @Test
    public void andShouldWorkAsExpected() throws Exception {
        assertThat(ALU.and(false, false)).isFalse();
        assertThat(ALU.and(true, false)).isFalse();
        assertThat(ALU.and(false, true)).isFalse();
        assertThat(ALU.and(true, true)).isTrue();
    }

    @Test
    public void addShouldWork() throws Exception {
        assertThat(ALU.add(false, false, false)).isFalse();
        assertThat(ALU.add(true, false, false)).isTrue();
        assertThat(ALU.add(false, true, false)).isTrue();
        assertThat(ALU.add(true, true, false)).isFalse();

        assertThat(ALU.add(false, false, true)).isTrue();
        assertThat(ALU.add(true, false, true)).isFalse();
        assertThat(ALU.add(false, true, true)).isFalse();
        assertThat(ALU.add(true, true, true)).isTrue();
    }

    @Test
    public void carryShouldWork() throws Exception {
        assertThat(ALU.carry(false, false, false)).isFalse();
        assertThat(ALU.carry(true, false, false)).isFalse();
        assertThat(ALU.carry(false, true, false)).isFalse();
        assertThat(ALU.carry(true, true, false)).isTrue();

        assertThat(ALU.carry(false, false, true)).isFalse();
        assertThat(ALU.carry(true, false, true)).isTrue();
        assertThat(ALU.carry(false, true, true)).isTrue();
        assertThat(ALU.carry(true, true, true)).isTrue();
    }

    @Test(dataProvider = "all4bits")
    public void computeAddShouldWork(BitField a, BitField b) {

        int aInt = a.intValue();
        int bInt = b.intValue();
        int expected = aInt + bInt;

        BitField out = alu.compute(a, b, ALUOperation.ADD, false, false);

        int result = out.intValue();
        assertThat(result).isEqualTo(expected);
    }

    @Test(dataProvider = "all4bits")
    public void computeAddShouldWorkWithCarryIn(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();
        int expected = aInt + bInt + 1;

        BitField out = alu.compute(a, b, ALUOperation.ADD, false, true);

        int result = out.intValue();
        assertThat(result).isEqualTo(expected);
    }

    @Test(dataProvider = "all4bits")
    public void orShoudWork(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt | bInt;

        BitField out = alu.compute(a, b, ALUOperation.OR, false, false);

        int result = out.intValue();
        assertThat(result).isEqualTo(expected);
    }

    @Test(dataProvider = "all4bits")
    public void andShoudWork(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt & bInt;

        BitField out = alu.compute(a, b, ALUOperation.AND, false, false);

        int result = out.intValue();
        assertThat(result).isEqualTo(expected);
    }

    @Test(dataProvider = "all4bits")
    public void xorShoudWork(BitField a, BitField b) {
        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt ^ bInt;

        BitField out = alu.compute(a, b, ALUOperation.XOR, false, false);

        int result = out.intValue();
        assertThat(result).isEqualTo(expected);
    }

    @DataProvider(name = "all4bits")
    public Object[][] aluAdd() {
        List<BitField> aVals = BitTools.makeListOfFields(4);
        List<BitField> bVals = BitTools.makeListOfFields(4);

        Object[][] all = new Object[256][];
        int index = 0;
        for (int a = 0; a < 16; a++) {
            for(int b = 0; b < 16; b++) {
                List<BitField> row = new ArrayList<>();
                row.add(aVals.get(a));
                row.add(bVals.get(b));

                all[index++] = row.toArray(new Object[row.size()]);
            }
        }

        return all;
    }
}
