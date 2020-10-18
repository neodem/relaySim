package com.neodem.relaySim.data.bitfield;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/10/16
 */
public class BitFieldImplTest {

    private BitFieldImpl bitField;

    @BeforeMethod
    public void setUp() throws Exception {
        bitField = new BitFieldImpl(4);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        bitField = null;
    }

    @Test
    public void getAndSetShouldWork() {
        assertThat(bitField.getBit(1)).isFalse();
        bitField.setBit(1, true);
        assertThat(bitField.getBit(1)).isTrue();
    }

    @Test
    public void setBitsShouldWork() {
        bitField.setBits(0, 0, 0, 1);

        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(2)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(3)).isEqualTo(0);

        bitField.setBits(0, 1, 1, 1);

        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(2)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(3)).isEqualTo(0);
    }

    @Test
    public void intValueShouldWork() {
        bitField.setBits(0, 1, 1, 1);
        assertThat(bitField.intValue()).isEqualTo(7);
    }

    @Test
    public void setToValueShouldWork() {
        bitField.setToValue(7);

        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(2)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(3)).isEqualTo(0);
    }

    @Test
    public void invertAllBitsShouldWork() {
        bitField.setBits(1, 0, 0, 1);

        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(2)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(3)).isEqualTo(1);

        bitField.invertAllBits();

        assertThat(bitField.getBitAsInt(0)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(2)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(3)).isEqualTo(0);
    }

    @Test
    public void convertToBitsShouldConvertToArrayOfBits() {
        int[] ints = bitField.convertToBits(7, 4);
        assertThat(ints).hasSize(4);
        assertThat(ints[0]).isEqualTo(1);
        assertThat(ints[1]).isEqualTo(1);
        assertThat(ints[2]).isEqualTo(1);
        assertThat(ints[3]).isEqualTo(0);
    }

    @Test
    public void constructorShouldInitToZeros() throws Exception {
        assertThat(bitField.getBitAsInt(0)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(2)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(3)).isEqualTo(0);
    }

    @Test
    public void bitFieldShouldSetProperValues() throws Exception {
        bitField.setBit(0, true);
        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(0);
        bitField.setBit(1, true);
        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(1);
        bitField.setBit(1, false);
        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(0);
        bitField.setBit(0, false);
        assertThat(bitField.getBitAsInt(0)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(0);
    }

    @Test
    public void set() throws Exception {
        bitField.setBits(0, 0, 0, 1);
        assertThat(bitField.getBitAsInt(0)).isEqualTo(1);
        assertThat(bitField.getBitAsInt(1)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(2)).isEqualTo(0);
        assertThat(bitField.getBitAsInt(3)).isEqualTo(0);
    }

    @Test
    public void getLSBShouldReturnCorrectValues() throws Exception {
        bitField.setBits(0, 0, 1, 0);
        BitField result = bitField.getLSBs(2);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getBitAsInt(0)).isEqualTo(0);
        assertThat(result.getBitAsInt(1)).isEqualTo(1);
    }

    @Test
    public void getMSBShouldReturnCorrectValues() throws Exception {
        bitField.setBits(1, 0, 0, 0);
        BitField result = bitField.getMSBs(2);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getBitAsInt(0)).isEqualTo(0);
        assertThat(result.getBitAsInt(1)).isEqualTo(1);

        bitField.setBits(1, 0, 1, 0);
        result = bitField.getMSBs(3);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.getBitAsInt(0)).isEqualTo(1);
        assertThat(result.getBitAsInt(1)).isEqualTo(0);
        assertThat(result.getBitAsInt(2)).isEqualTo(1);
    }

    @Test
    public void getSubFieldShouldWork() throws Exception {
        BitField testField = BitFieldBuilder.create(0, 0, 1, 1, 0, 0);
        BitField result = testField.getSubField(2, 4);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.getBitAsInt(0)).isEqualTo(1);
        assertThat(result.getBitAsInt(1)).isEqualTo(1);
        assertThat(result.getBitAsInt(2)).isEqualTo(0);
    }

    @Test
    public void getSubFieldWithPaddingShouldPad() throws Exception {
        BitField testField = BitFieldBuilder.create(1, 1);
        BitField result = testField.getSubFieldWithPadding(0, 3);

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.getBitAsInt(0)).isEqualTo(1);
        assertThat(result.getBitAsInt(1)).isEqualTo(1);
        assertThat(result.getBitAsInt(2)).isEqualTo(0);
        assertThat(result.getBitAsInt(3)).isEqualTo(0);
    }

    @Test
    public void bitfieldsEqualsShouldWork() throws Exception {
        bitField.setBits(1, 0, 1, 1);
        assertThat(bitField).isEqualTo(BitFieldBuilder.create(1, 0, 1, 1));
    }

    @Test
    public void resizeSmallerShouldWork() throws Exception {
        BitField bitField = BitFieldBuilder.create(1, 0, 1, 1, 1, 0);
        bitField.resize(4);
        assertThat(bitField).isEqualTo(BitFieldBuilder.create(1, 1, 1, 0));
    }

    @Test
    public void resizeBiggerShouldWork() throws Exception {
        BitField bitField = BitFieldBuilder.create(1, 0, 1, 1, 1, 0);
        bitField.resize(10);
        assertThat(bitField).isEqualTo(BitFieldBuilder.create(0, 0, 0, 0, 1, 0, 1, 1, 1, 0));
    }

    @Test
    public void createFromInt() throws Exception {
        BitField val = BitFieldBuilder.createFromInt(6);
        assertThat(val).isEqualTo(BitFieldBuilder.create(1, 1, 0));
    }

    @Test
    public void shiftRightShouldWork() {
        BitField field = BitFieldBuilder.create(1, 0, 0, 1);
        field.shiftRight(2);
        assertThat(field).isEqualTo(BitFieldBuilder.create(1, 0));
    }

    @Test
    public void shiftLeftShouldWork() {
        BitField field = BitFieldBuilder.create(1, 0, 0, 1);
        field.shiftLeft(3);
        assertThat(field).isEqualTo(BitFieldBuilder.create(1, 0, 0, 1, 0, 0, 0));
    }

    @Test
    public void shiftAndAddToRightShouldWork() {
        BitField field = BitFieldBuilder.create(1, 0, 0, 1);
        field.shiftAndAddToRight(BitFieldBuilder.create(1, 1, 1));
        assertThat(field).isEqualTo(BitFieldBuilder.create(1, 0, 0, 1, 1, 1, 1));
    }

    @Test
    public void combineShouldCreateProperField() {
        BitField first = BitFieldBuilder.create(0, 0, 0, 1, 0);
        BitField second = BitFieldBuilder.create(0, 1, 1);
        BitField third = BitFieldBuilder.create(1, 1, 1, 1);

        BitField combined = BitFieldBuilder.combineShiftLeft(first, second, third);
        assertThat(combined).isEqualTo(BitFieldBuilder.create(0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1));
    }

    @Test
    public void getAsBytesShouldWorkWithSmallFields() {
        BitField field = BitFieldBuilder.create(0, 0, 0, 1, 0);
        List<Byte> asBytes = field.getAsBytes();
        assertThat(asBytes).isNotNull().hasSize(1);
        assertThat(asBytes.get(0)).isEqualTo((byte) 2);
    }

    @Test
    public void getAsBytesShouldWorkWith8bitfield() {
        BitField field = BitFieldBuilder.create(0, 0, 0, 0, 0, 0, 1, 0);
        List<Byte> asBytes = field.getAsBytes();
        assertThat(asBytes).isNotNull().hasSize(1);
        assertThat(asBytes.get(0)).isEqualTo((byte) 2);
    }

    @Test
    public void getAsBytesShouldWorkWith9bitfield() {
        BitField field = BitFieldBuilder.create(1, 0, 0, 0, 0, 0, 0, 1, 0);
        List<Byte> asBytes = field.getAsBytes();
        assertThat(asBytes).isNotNull().hasSize(2);
        assertThat(asBytes.get(0)).isEqualTo((byte) 2);
        assertThat(asBytes.get(1)).isEqualTo((byte) 1);
    }

    @Test
    public void getAsBytesShouldWork() {
        BitField field = BitFieldBuilder.create(1, 1, 0, 0, 0, 0, 0, 1, 0);
        List<Byte> asBytes = field.getAsBytes();
        assertThat(asBytes).isNotNull().hasSize(2);
        assertThat(asBytes.get(0)).isEqualTo((byte) 130);
        assertThat(asBytes.get(1)).isEqualTo((byte) 1);
    }
}
