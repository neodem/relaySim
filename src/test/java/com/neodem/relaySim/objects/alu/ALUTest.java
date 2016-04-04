package com.neodem.relaySim.objects.alu;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.BitField4;
import com.neodem.relaySim.objects.bus.Bus;
import com.neodem.relaySim.objects.bus.BusFactory;
import com.neodem.relaySim.objects.bus.BusNames;
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
    private BusFactory busFactory;

    @BeforeMethod
    public void before() {
        busFactory = new BusFactory();
        alu = new ALU(busFactory);
    }

    @AfterMethod
    public void after() {
        alu = null;
        busFactory = null;
    }

    @Test
    public void something() throws Exception {
        Bus aluAin = busFactory.getBus(BusNames.ALU_AIN, 4);
        Bus aluBin = busFactory.getBus(BusNames.ALU_BIN, 4);
        Bus aluControl = busFactory.getBus(BusNames.ALU_CTRL, 4);
        Bus aluOut = busFactory.getBus(BusNames.ALU_OUT, 4);

        BitField ain = new BitField4(0, 1, 1, 1);
        aluAin.updateData(ain);

        BitField bin = new BitField4(0, 0, 0, 1);
        aluBin.updateData(bin);

        BitField result = aluOut.getData();

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
    public void doAdditionShoudAddWithNoCarry(BitField4 a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);
        alu.setCarryIn(false);

        int aInt = a.intValue();
        int bInt = b.intValue();
        int expected = aInt + bInt;

        alu.setControl(ALU.convertControl(ALUOperation.ADD, false));

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBit(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBit(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBit(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBit(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @Test(dataProvider = "all4bits")
    public void orShoudWork(BitField4 a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);
        alu.setCarryIn(false);

        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt | bInt;
        alu.setControl(ALU.convertControl(ALUOperation.OR, false));

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBit(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBit(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBit(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBit(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @Test(dataProvider = "all4bits")
    public void andShoudWork(BitField4 a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);
        alu.setCarryIn(false);

        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt & bInt;
        alu.setControl(ALU.convertControl(ALUOperation.AND, false));

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBit(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBit(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBit(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBit(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @Test(dataProvider = "all4bits")
    public void xorShoudWork(BitField4 a, BitField b) {
        alu.setInA(a);
        alu.setInB(b);
        alu.setCarryIn(false);

        int aInt = a.intValue();
        int bInt = b.intValue();

        int expected = aInt ^ bInt;
        alu.setControl(ALU.convertControl(ALUOperation.XOR, false));

        System.out.println(alu);

        BitField out = alu.getOut();

        assertThat(out.getBit(0)).isEqualTo(BitTools.bit(0, expected));
        assertThat(out.getBit(1)).isEqualTo(BitTools.bit(1, expected));
        assertThat(out.getBit(2)).isEqualTo(BitTools.bit(2, expected));
        assertThat(out.getBit(3)).isEqualTo(BitTools.bit(3, expected));
        assertThat(alu.getCarryOut()).isEqualTo(BitTools.bit(4, expected));
    }

    @DataProvider(name = "all4bits")
    public Object[][] aluAdd() {
        List<BitField> aVals = new ArrayList<>();
        List<BitField> bVals = new ArrayList<>();

        for (int a = 0; a < 16; a++) {
            for (int b = 0; b < 16; b++) {
                BitField aField = new BitField4();
                aField.set(a);
                aVals.add(aField);

                BitField bField = new BitField4();
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
