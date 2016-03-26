package com.neodem.relaySim.objects;

import com.neodem.relaySim.tools.BitTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vfumo on 3/13/16.
 */
public class BitField {

    // 0 is the LSB
    private List<Boolean> data;
    private int size;

    /**
     * copy constructor
     * @param bitField
     */
    public BitField(BitField bitField) {
        this.size = bitField.getSize();
        this.data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            setBit(i, bitField.getBit(i));
        }
    }

    public BitField(int size) {
        this.size = size;
        this.data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            setBit(i, false);
        }
    }

    /**
     * invert all the bits
     */
    public void invert() {
        for (int i = 0; i < size; i++) {
            boolean newBit = !getBit(i);
            setBit(i, newBit);
        }
    }

    public void setBit(int pos, boolean val) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to set bit %d of a %d bit field", pos, size));
        data.add(pos, val);
    }

    public void setBit(int pos, int value) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to set bit %d of a %d bit field", pos, size));
        boolean val = true;
        if (value == 0) val = false;
        data.add(pos, val);
    }

    public boolean getBit(int pos) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to get bit %d of a %d bit field", pos, size));
        return data.get(pos);
    }

    public int getBitAsInt(int pos) {
        if (pos >= size)
            throw new IllegalArgumentException(String.format("trying to get bit %d of a %d bit field", pos, size));
        boolean bit = getBit(pos);
        return bit ? 1 : 0;
    }

    public int intValue() {
        return BitTools.makeInt(data);
    }

    public void set(int val) {
        List<Integer> bits = BitTools.convertToList(val, size);
        for (int i = 0; i < size; i++) {
            setBit(i, bits.get(i));
        }
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
}
