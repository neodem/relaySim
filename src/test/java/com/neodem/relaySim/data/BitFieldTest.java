package com.neodem.relaySim.data;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/10/16
 */
public class BitFieldTest {

    private BitField bitField;

    @BeforeMethod
    public void setUp() throws Exception {
        bitField = new BitField(4);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        bitField = null;
    }

    @Test
    public void constructorShouldInitToZeros() throws Exception {
        assertThat(bitField.getBit(0)).isEqualTo(0);
        assertThat(bitField.getBit(1)).isEqualTo(0);
        assertThat(bitField.getBit(2)).isEqualTo(0);
        assertThat(bitField.getBit(3)).isEqualTo(0);
    }

    @Test
    public void bitFieldShouldSetProperValues() throws Exception {
        bitField.setBit(0, true);
        assertThat(bitField.getBit(0)).isEqualTo(1);
        assertThat(bitField.getBit(1)).isEqualTo(0);
        bitField.setBit(1, true);
        assertThat(bitField.getBit(0)).isEqualTo(1);
        assertThat(bitField.getBit(1)).isEqualTo(1);
        bitField.setBit(1, false);
        assertThat(bitField.getBit(0)).isEqualTo(1);
        assertThat(bitField.getBit(1)).isEqualTo(0);
        bitField.setBit(0, false);
        assertThat(bitField.getBit(0)).isEqualTo(0);
        assertThat(bitField.getBit(1)).isEqualTo(0);
    }

    @Test
    public void set() throws Exception {
        bitField.set(0, 0, 0, 1);
        assertThat(bitField.getBit(0)).isEqualTo(1);
        assertThat(bitField.getBit(1)).isEqualTo(0);
        assertThat(bitField.getBit(2)).isEqualTo(0);
        assertThat(bitField.getBit(3)).isEqualTo(0);
    }

    @Test
    public void getLSBShouldReturnCorrectValues() throws Exception {
        bitField.set(0, 0, 1, 0);
        BitField result = bitField.getLSB(2);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getBit(0)).isEqualTo(0);
        assertThat(result.getBit(1)).isEqualTo(1);
    }

    @Test
    public void getMSBShouldReturnCorrectValues() throws Exception {
        bitField.set(1, 0, 0, 0);
        BitField result = bitField.getMSB(2);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getBit(0)).isEqualTo(0);
        assertThat(result.getBit(1)).isEqualTo(1);

        bitField.set(1, 0, 1, 0);
        result = bitField.getMSB(3);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.getBit(0)).isEqualTo(1);
        assertThat(result.getBit(1)).isEqualTo(0);
        assertThat(result.getBit(2)).isEqualTo(1);
    }

    @Test
    public void getSubFieldShouldWork() throws Exception {
        BitField testField = BitField.create(0,0,1,1,0,0);
        BitField result = testField.getSubField(2,4);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.getBit(0)).isEqualTo(1);
        assertThat(result.getBit(1)).isEqualTo(1);
        assertThat(result.getBit(2)).isEqualTo(0);

    }

    @Test
    public void bitfieldsEqualsShouldWork() throws Exception {
        bitField.set(1, 0, 1, 1);
        assertThat(bitField).isEqualTo(BitField.create(1, 0, 1, 1));
    }

    @Test
    public void resizeSmallerShouldWork() throws Exception {
        BitField bitField = BitField.create(1, 0, 1, 1, 1, 0);
        bitField.resize(4);
        assertThat(bitField).isEqualTo(BitField.create(1, 1, 1, 0));
    }

    @Test
    public void resizeBiggerShouldWork() throws Exception {
        BitField bitField = BitField.create(1, 0, 1, 1, 1, 0);
        bitField.resize(10);
        assertThat(bitField).isEqualTo(BitField.create(0, 0, 0, 0, 1, 0, 1, 1, 1, 0));
    }

    @Test
    public void createFromInt() throws Exception {
        BitField val = BitField.createFromInt(6);
        assertThat(val).isEqualTo(BitField.create(1,1,0));
    }
}
