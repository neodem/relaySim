package com.neodem.relaySim.data;

import com.neodem.relaySim.tools.BitTools;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * all BitField objects need a size of at least 1.
 * indexing is from right to left:
 * example : 0001, bit0 == 1
 * <p>
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/13/16
 */
public class BitField {

    // 0 is the LSB
    private List<Boolean> data;
    private int size;

    /**
     * copy constructor
     *
     * @param bitField the BitField to make a copy of
     */
    public BitField(BitField bitField) {
        this(bitField.getSize());
        for (int i = 0; i < size; i++) {
            setBit(i, bitField.getBit(i));
        }
    }

    /**
     * init a new BitField with a given size and set all values to 0
     *
     * @param size the size of the BitField
     */
    public BitField(int size) {
        if (size < 1) throw new IllegalArgumentException("BitField should have a size of at least 1");
        this.size = size;
        this.data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.data.add(false);
        }
    }

    public static BitField create(int... values) {
        BitField result = new BitField(values.length);
        result.set(values);
        return result;
    }

    public static BitField createFromInt(int value) {
        String bitstring = Integer.toString(value, 2);
        int size = bitstring.length();
        BitField result = new BitField(size);
        for (int i = 0; i < size; i++) {
            boolean val = bitstring.charAt(i) == '1';
            result.setBit(size - i - 1, val);
        }
        return result;
    }

    public static BitField createFromHex(String hexString) {
        int fieldSize = 4 * hexString.length();
        int value = Integer.parseInt(hexString, 16);
        return createFromInt(value, fieldSize);
    }

    public static BitField createFromHex(String hexString, int fieldSize) {
        int value = Integer.parseInt(hexString, 16);
        return createFromInt(value, fieldSize);
    }

    public static BitField createFromInt(int value, int fieldSize) {
        String bitstring = Integer.toString(value, 2);
        int size = bitstring.length();
        BitField result = new BitField(size);
        for (int i = 0; i < size; i++) {
            boolean val = bitstring.charAt(i) == '1';
            result.setBit(size - i - 1, val);
        }

        return result.resize(fieldSize);
    }

    /**
     * set all values of the BitField with the rightmost value being index 0.
     * example set(0,0,0,1) would set bit0 == 1
     *
     * @param values the values to set the BitField
     * @return the BitField
     */
    public BitField set(int... values) {
        if (values.length != size)
            throw new IllegalArgumentException(String.format("size mismatch. You need to set %d bits", size));

        for (int i = 0; i < values.length; i++) {
            setBit(size - i - 1, values[i]);
        }
        return this;
    }

    /**
     * invert all the bits
     */
    public BitField invert() {
        for (int i = 0; i < size; i++) {
            boolean newBit = !getBitAsBoolean(i);
            setBit(i, newBit);
        }
        return this;
    }

    public BitField setBit(int pos, boolean val) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to set bit %d of a %d bit field", pos, size));
        data.set(pos, val);
        return this;
    }

    public BitField setBit(int pos, int value) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to set bit %d of a %d bit field", pos, size));
        boolean val = true;
        if (value == 0) val = false;
        data.set(pos, val);
        return this;
    }

    public boolean getBitAsBoolean(int pos) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to get bit %d of a %d bit field", pos, size));
        return data.get(pos);
    }

    public int getBit(int pos) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to get bit %d of a %d bit field", pos, size));
        boolean bit = getBitAsBoolean(pos);
        return bit ? 1 : 0;
    }

    public int intValue() {
        return BitTools.makeInt(data);
    }

    /**
     * convert the given value into a BitField
     *
     * @param val
     * @return
     */
    public BitField setValue(int val) {
        List<Integer> bits = BitTools.convertToList(val, size);
        for (int i = 0; i < size; i++) {
            setBit(i, bits.get(i));
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();

        for (int i = size - 1; i >= 0; i--) {
            boolean val = data.get(i);
            if (val) b.append('1');
            else b.append('0');
        }

        return b.toString();
    }

    public int getSize() {
        return size;
    }

    /**
     * get the LSBs of the BitField (if possible)
     *
     * @param digits
     * @return
     */
    public BitField getLSB(int digits) {
        if (digits >= size)
            throw new IllegalArgumentException("can't get more bits (" + digits + ") than the size (" + size + ") of the field!");

        BitField result = new BitField(digits);

        for (int i = 0; i < digits; i++) {
            result.setBit(i, getBit(i));
        }

        return result;
    }

    public BitField getMSB(int digits) {
        if (digits >= size)
            throw new IllegalArgumentException("can't get more bits (" + digits + ") than the size (" + size + ") of the field!");

        BitField result = new BitField(digits);

        for (int i = 0; i < digits; i++) {
            int j = size - digits + i;
            result.setBit(i, getBit(j));
        }

        return result;
    }

    /**
     * return a new BitField. Inclusive
     * @param from
     * @param to
     * @return
     */
    public BitField getSubField(int from, int to) {
        if (from > to)
            throw new IllegalArgumentException("from needs to be less than to");

        if(from == to)
            throw new IllegalArgumentException("making from and to the same would return a subfield of size 0");

        int requestedSize = to-from+1;
        if (requestedSize >= size)
            throw new IllegalArgumentException("can't get more bits (" + requestedSize + ") than the size (" + size + ") of the field!");

        BitField result = new BitField(requestedSize);

        for (int i = 0; i < requestedSize; i++) {
            int j = from + i;
            result.setBit(i, getBit(j));
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BitField bitField = (BitField) o;

        return new EqualsBuilder()
                .append(size, bitField.size)
                .append(data, bitField.data)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(271, 151)
                .append(data)
                .append(size)
                .toHashCode();
    }

    public int size() {
        return size;
    }

    /**
     * if newSize > currentSize, we pad with 0's on the left
     * if newSize < currentSize, we simply drop the bits on the left
     *
     * @param newSize
     */
    public BitField resize(int newSize) {
        if (newSize > size) {
            int bitsToAdd = newSize - size;
            for (int i = 0; i < bitsToAdd; i++) {
                data.add(size + i, false);
            }
            this.size = newSize;
        } else if (newSize < size) {
            int bitsToRemove = size - newSize;
            for (int i = 0; i < bitsToRemove; i++) {
                data.remove(size - i - 1);
            }
            this.size = newSize;
        }

        return this;
    }

    public BitField copy() {
        return new BitField(this);
    }

}
