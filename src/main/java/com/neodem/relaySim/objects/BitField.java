package com.neodem.relaySim.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vfumo on 3/13/16.
 */
public class BitField {
    private List<Boolean> data;

    public BitField(int size) {
        this.data = new ArrayList<>(size);
    }

    public void addBit(int pos, boolean val) {
        data.add(pos, val);
    }

    public void addBit(int pos, int value) {
        boolean val = true;
        if (value == 0) val = false;
        data.add(pos, val);
    }

    public boolean getBit(int pos) {
        return data.get(pos);
    }
    
    public int getBitAsInt(int pos) {
        boolean bit = getBit(pos);
        return bit ? 1 : 0;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();

        for (int i = data.size() - 1; i >= 0; i--) {
            boolean val = data.get(i);
            if (val) b.append('1');
            else b.append('0');
        }

        return b.toString();
    }
}
