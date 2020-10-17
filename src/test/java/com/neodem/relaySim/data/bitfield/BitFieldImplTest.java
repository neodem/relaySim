package com.neodem.relaySim.data.bitfield;

import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import com.neodem.relaySim.data.bitfield.BitFieldImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
        bitField.setBits(0, 0, 0, 1);
        assertThat(bitField.getBit(0)).isEqualTo(1);
        assertThat(bitField.getBit(1)).isEqualTo(0);
        assertThat(bitField.getBit(2)).isEqualTo(0);
        assertThat(bitField.getBit(3)).isEqualTo(0);
    }

    @Test
    public void getLSBShouldReturnCorrectValues() throws Exception {
        bitField.setBits(0, 0, 1, 0);
        BitField result = bitField.getLSBs(2);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getBit(0)).isEqualTo(0);
        assertThat(result.getBit(1)).isEqualTo(1);
    }

    @Test
    public void getMSBShouldReturnCorrectValues() throws Exception {
        bitField.setBits(1, 0, 0, 0);
        BitField result = bitField.getMSBs(2);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.getBit(0)).isEqualTo(0);
        assertThat(result.getBit(1)).isEqualTo(1);

        bitField.setBits(1, 0, 1, 0);
        result = bitField.getMSBs(3);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.getBit(0)).isEqualTo(1);
        assertThat(result.getBit(1)).isEqualTo(0);
        assertThat(result.getBit(2)).isEqualTo(1);
    }

    @Test
    public void getSubFieldShouldWork() throws Exception {
        BitField testField = BitFieldBuilder.create(0,0,1,1,0,0);
        BitField result = testField.getSubField(2,4);

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.getBit(0)).isEqualTo(1);
        assertThat(result.getBit(1)).isEqualTo(1);
        assertThat(result.getBit(2)).isEqualTo(0);

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
        assertThat(val).isEqualTo(BitFieldBuilder.create(1,1,0));
    }

    @Test
    public void shiftLeftShouldWork() {
        BitField field = BitFieldBuilder.create(1,0,0,1);
        field.shiftLeft(3);
        assertThat(field).isEqualTo(BitFieldBuilder.create(1,0,0,1,0,0,0));
    }

    @Test
    public void shiftAndAddToRightShouldWork() {
        BitField field = BitFieldBuilder.create(1,0,0,1);
        field.shiftAndAddToRight(BitFieldBuilder.create(1,1,1));
        assertThat(field).isEqualTo(BitFieldBuilder.create(1,0,0,1,1,1,1));
    }

    @Test
    public void combineShouldCreateProperField() {
        BitField first = BitFieldBuilder.create(0,0,0,1,0);
        BitField second = BitFieldBuilder.create(0,1,1);
        BitField third = BitFieldBuilder.create(1,1,1,1);

        BitField combined = BitFieldBuilder.combine(first, second, third);
        assertThat(combined).isEqualTo(BitFieldBuilder.create(0,0,0,1,0, 0,1,1, 1,1,1,1));
    }

    /*

     * make a new BF by combining multiple BFs by placing them next to each other. The first one
     * will be on the left
     * <p>
     * Example:
     * <p>
     * first : 00010
     * second : 011
     * third : 1111
     * <p>
     * result : 000100111111
     *
     * @param fields
     * @return

    public static BitField combine(BitField... fields) {
        BitField result = new BitField(fields[0]);

        for (int i = 1; i < fields.length; i++) {
            result.shiftAndAddToRight(fields[i]);
        }

        return result;
    }
     */
}
