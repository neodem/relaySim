package com.neodem.relaySim.data.bitfield;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/17/20
 */
public class BitFieldImpl implements BitField {

    private BitSet bitSet;
    private int size;

    /**
     * init a new BitField with a given size and set all values to 0
     *
     * @param size the size of the BitField
     */
    protected BitFieldImpl(int size) {
        if (size < 1) throw new IllegalArgumentException("BitField should have a size of at least 1");
        bitSet = new BitSet(size);
        this.size = size;
    }

    /**
     * copy constructor
     *
     * @param bitField the BitField to make a copy of
     */
    protected BitFieldImpl(BitField bitField) {
        this(bitField.size());
        for (int i = 0; i < bitField.size(); i++) {
            setBit(i, bitField.getBitAsInt(i));
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void setBit(int index, boolean value) {
        if (index < 0) throw new IndexOutOfBoundsException("index must be equal to or greater than 0");
        if (index >= size) throw new IndexOutOfBoundsException("index must be less than size");

        if (value) bitSet.set(index);
        else bitSet.clear(index);
    }

    @Override
    public void setBits(int... values) {
        if (values.length != size)
            throw new IllegalArgumentException(String.format("size mismatch. You need to set %d bits", size));

        for (int i = 0; i < values.length; i++) {
            setBit(size - i - 1, values[i]);
        }
    }

    @Override
    public void setBit(int index, int value) {
        if (index < 0) throw new IndexOutOfBoundsException("index must be equal to or greater than 0");
        if (index >= size) throw new IndexOutOfBoundsException("index must be less than size");

        if (value != 1 && value != 0) throw new IllegalArgumentException("value may only be a 0 or 1");

        if (value == 1) setBit(index, true);
        else setBit(index, false);
    }

    @Override
    public void setToValue(int val) {
        int[] bits = convertToBits(val, size);
        bitSet.clear();
        for (int i = 0; i < bits.length; i++) {
            setBit(i, bits[i]);
        }
    }

    @Override
    public void invertAllBits() {
        bitSet.flip(0, size);
    }

    @Override
    public boolean getBit(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(String.format("trying to get bit %d of a %d bit field", index, size));
        return bitSet.get(index);
    }

    @Override
    public int getBitAsInt(int pos) {
        if (pos >= size)
            throw new IndexOutOfBoundsException(String.format("trying to get bit %d of a %d bit field", pos, size));
        boolean bit = getBit(pos);
        return bit ? 1 : 0;
    }

    @Override
    public int intValue() {
        int result = 0;

        for (int i = 0; i < size; i++) {
            boolean bit = getBit(i);
            if (bit) {
                double pow = Math.pow(2, i);
                result += pow;
            }
        }

        return result;
    }

    @Override
    public String asString() {
        StringBuffer b = new StringBuffer();

        for (int i = size - 1; i >= 0; i--) {
            boolean val = getBit(i);
            if (val) b.append('1');
            else b.append('0');
        }

        return b.toString();
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public BitField getLSBs(int digits) {
        if (digits >= size)
            throw new IndexOutOfBoundsException("can't get more bits (" + digits + ") than the size (" + size + ") of the field!");

        BitField result = new BitFieldImpl(digits);

        for (int i = 0; i < digits; i++) {
            result.setBit(i, getBitAsInt(i));
        }

        return result;
    }

    @Override
    public BitField getMSBs(int digits) {
        if (digits >= size)
            throw new IndexOutOfBoundsException("can't get more bits (" + digits + ") than the size (" + size + ") of the field!");

        BitField result = new BitFieldImpl(digits);

        for (int i = 0; i < digits; i++) {
            int j = size - digits + i;
            result.setBit(i, getBitAsInt(j));
        }

        return result;
    }

    @Override
    public BitField getSubField(int from, int to) {
        if (from > to)
            throw new IllegalArgumentException("from needs to be less than to");

        if (from == to)
            throw new IllegalArgumentException("making from and to the same would return a subfield of size 0");

        int requestedSize = to - from + 1;
        if (requestedSize >= size)
            throw new IndexOutOfBoundsException("can't get more bits (" + requestedSize + ") than the size (" + size + ") of the field!");

        BitField result = new BitFieldImpl(requestedSize);

        for (int i = 0; i < requestedSize; i++) {
            int j = from + i;
            result.setBit(i, getBitAsInt(j));
        }

        return result;
    }

    @Override
    public BitField getSubFieldWithPadding(int from, int to) {
        if (from > to)
            throw new IllegalArgumentException("from needs to be less than to");

        if (from == to)
            throw new IllegalArgumentException("making from and to the same would return a subfield of size 0");

        int requestedSize = to - from + 1;
        BitField result = new BitFieldImpl(requestedSize);

        for (int i = 0; i < requestedSize; i++) {
            boolean value = bitSet.get(from + i);
            result.setBit(i, value);
        }

        return result;
    }

    @Override
    public void shiftAndAddToRight(BitField fieldToAddToTheRight) {
        int bitsToAdd = fieldToAddToTheRight.size();
        shiftLeft(bitsToAdd);
        for (int i = 0; i < bitsToAdd; i++) {
            setBit(i, fieldToAddToTheRight.getBit(i));
        }
    }

    @Override
    public void shiftLeft(int numberToShift) {
        int oldSize = this.size;
        resize(numberToShift + oldSize);

        int offset = this.size - oldSize;

        // shift old stuff left
        for (int i = oldSize - 1; i >= 0; i--) {
            Boolean value = getBit(i);
            setBit(i + offset, value);
        }

        // pad
        bitSet.clear(0, numberToShift);
    }

    @Override
    public void shiftRight(int numberToShift) {
        for (int i = 0; i < numberToShift; i++) {
            Boolean value = getBit(i + numberToShift);
            setBit(i, value);
        }
        resize(size - numberToShift);
    }

    @Override
    public void resize(int newSize) {
        if (newSize > size) {
            this.size = newSize;
        } else if (newSize < size) {
            int bitsToRemove = size - newSize;
            for (int i = 0; i < bitsToRemove; i++) {
                bitSet.clear(size - i - 1);
            }
            this.size = newSize;
        }
    }

    @Override
    public BitField copy() {
        return new BitFieldImpl(this);
    }

    @Override
    public List<Byte> getAsBytes() {
        //0 is lsb, size is the msb
        List<Byte> bytes = new ArrayList<>();

        int divider = 8;
        if (size > 8) divider = size;
        int numberToGet = (divider % 8) + 1;

        for (int i = 0; i < numberToGet; i++) {
            int from = i * 8;
            int to = from + 7;
            BitField subField = getSubFieldWithPadding(from, to);
            byte b = convertToByte(subField);
            bytes.add(b);
        }

        return bytes;
    }

    public static byte convertToByte(BitField field) {
        if (field.size() != 8) throw new IllegalArgumentException("field must be size 8");

        byte b = 0;
        for (int i = 0; i < 8; i++) {
            if (field.getBit(i)) {
                b |= 1 << (i % 8);
            }
        }

        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BitFieldImpl bitField = (BitFieldImpl) o;

        return new EqualsBuilder()
                .append(bitSet, bitField.bitSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(271, 151)
                .append(bitSet)
                .toHashCode();
    }

    /**
     * will take an integer value and return
     *
     * @param value
     * @return
     * @paraam len
     */
    protected int[] convertToBits(int value, int len) {
        double pow = Math.pow(2, len);
        if (pow < value) {
            throw new IllegalArgumentException("The Value is too big for a " + len + " bit long String");
        }

        int[] result = new int[len];
        int shift = len - 1;
        for (int i = 1; shift >= 0; shift--, i++) {
            int bit = (value >> shift) & 1;
            if (bit == 1) {
                result[len - i] = 1;
            } else {
                result[len - i] = 0;
            }
        }

        return result;
    }
}
