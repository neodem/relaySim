package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.bus.Bus;
import com.neodem.relaySim.objects.bus.BusNames;
import com.neodem.relaySim.objects.bus.BusRegistry;
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
    private BusRegistry busRegistry;

    private Bus aluAinBus;
    private Bus aluBinBus;
    private Bus aluControlBus;
    private Bus aluOutBus;

    @BeforeMethod
    public void before() {
        busRegistry = new BusRegistry();
        alu = new ALU(busRegistry,4);

        aluAinBus = busRegistry.getBus(BusNames.ALU_AIN, 4);
        aluBinBus = busRegistry.getBus(BusNames.ALU_BIN, 4);
        aluControlBus = busRegistry.getBus(BusNames.ALU_CTRL, 4);
        aluOutBus = busRegistry.getBus(BusNames.ALU_OUT, 4);
    }

    @AfterMethod
    public void after() {
        alu = null;
        busRegistry = null;
        aluAinBus = null;
        aluBinBus = null;
        aluControlBus = null;
        aluOutBus = null;
    }

    @Test
    public void anAddShouldWorkUsingBusses() throws Exception {
        // add, !bInv, !carryIn
        aluControlBus.updateData(new BitField(4).set(0, 0, 0, 0));
        aluAinBus.updateData(new BitField(4).set(0, 1, 1, 1));
        aluBinBus.updateData(new BitField(4).set(0, 0, 0, 1));

        BitField result = aluOutBus.getData();
        assertThat(result).isEqualTo(new BitField(5).set(0, 1, 0, 0, 0));

        // add, !bInv, carryIn
        aluControlBus.updateData(new BitField(4).set(0, 0, 0, 1));
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
    public void doAdditionShoudAddWithNoCarry(BitField a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);

        int aInt = a.intValue();
        int bInt = b.intValue();
        int expected = aInt + bInt;

        alu.setControl(ALU.convertControl(ALUOperation.ADD, false, false));
        alu.compute();

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBitAsBoolean(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBitAsBoolean(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBitAsBoolean(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBitAsBoolean(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @Test(dataProvider = "all4bits")
    public void orShoudWork(BitField a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);

        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt | bInt;
        alu.setControl(ALU.convertControl(ALUOperation.OR, false, false));
        alu.compute();

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBitAsBoolean(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBitAsBoolean(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBitAsBoolean(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBitAsBoolean(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @Test(dataProvider = "all4bits")
    public void andShoudWork(BitField a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);

        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt & bInt;
        alu.setControl(ALU.convertControl(ALUOperation.AND, false, false));
        alu.compute();

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBitAsBoolean(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBitAsBoolean(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBitAsBoolean(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBitAsBoolean(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @Test(dataProvider = "all4bits")
    public void xorShoudWork(BitField a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);

        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt ^ bInt;
        alu.setControl(ALU.convertControl(ALUOperation.XOR, false, false));
        alu.compute();

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBitAsBoolean(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBitAsBoolean(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBitAsBoolean(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBitAsBoolean(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @DataProvider(name = "all4bits")
    public Object[][] aluAdd() {
        List<BitField> aVals = new ArrayList<>();
        List<BitField> bVals = new ArrayList<>();

        for (int a = 0; a < 16; a++) {
            for (int b = 0; b < 16; b++) {
                BitField aField = new BitField(4);
                aField.set(a);
                aVals.add(aField);

                BitField bField = new BitField(4);
                bField.set(b);
                bVals.add(bField);
            }
        }

        Object[][] all = new Object[256][];
        for (int i = 0; i < 256; i++) {
            List<BitField> row = new ArrayList<>();
            row.add(aVals.get(i));
            row.add(bVals.get(i));

            all[i] = row.toArray(new Object[row.size()]);
        }

        return all;
    }
}
