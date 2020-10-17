package com.neodem.relaySim.data;

import java.util.List;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/16/20
 */
public class MagicBitField implements BitField {

    private int data;
    private int size;

    /**
     * init a field with a given size and set all values to 0
     *
     * @param initialSize
     */
    public MagicBitField(int initialSize) {
        this.size = initialSize;
        this.data = 0;
    }

    @Override
    public int size() {
        return size;
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
    public void setBit(int index, boolean value) {
        if(index < 0) throw new IndexOutOfBoundsException("index must be equal to or greater than 0");
        if(index >= size) throw new IndexOutOfBoundsException("index must be less than size");

        if(value)
            data |= ( 1 << index);
        else
            data |= ( 0 << index);
    }

    @Override
    public void setBit(int index, int value) {
        if(index < 0) throw new IndexOutOfBoundsException("index must be equal to or greater than 0");
        if(index >= size) throw new IndexOutOfBoundsException("index must be less than size");

        if(value != 1 || value != 0) throw new IllegalArgumentException("value may only be a 0 or 1");

        if(value == 1)
            data |= ( 1 << index);

        else
            data |= ( 0 << index);
    }

    @Override
    public void setValue(int val) {

    }

    @Override
    public void invertAllBits() {

    }

    @Override
    public boolean getBitAsBoolean(int pos) {
        return false;
    }

    @Override
    public int getBit(int pos) {
        return 0;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public String asString() {
        StringBuffer b = new StringBuffer();

        for (int i = size - 1; i >= 0; i--) {
            boolean val = getBitAsBoolean(i);
            if (val) b.append('1');
            else b.append('0');
        }

        return b.toString();
    }

    @Override
    public BitField getLSBs(int digits) {
        return null;
    }

    @Override
    public BitField getMSBs(int digits) {
        return null;
    }

    @Override
    public BitField getSubField(int from, int to) {
        return null;
    }

    @Override
    public void shiftAndAddToRight(BitField fieldToAddToTheRight) {

    }

    @Override
    public void shiftLeft(int numberToShift) {

    }

    @Override
    public void rightShift(int numberToShift) {

    }

    @Override
    public void resize(int newSize) {

    }

    @Override
    public BitField copy() {
        return null;
    }

    @Override
    public List<Byte> getAsBytes() {
        return null;
    }
}
