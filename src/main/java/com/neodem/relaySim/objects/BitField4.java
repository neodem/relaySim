package com.neodem.relaySim.objects;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on : 3/13/16
 */
public class BitField4 extends BitField {
    public BitField4() {
        super(4);
    }

    public BitField4(int bit3, int bit2, int bit1, int bit0) {
        super(4);
        set(bit3, bit2, bit1, bit0);
    }

    /**
     * set from 0-15, more than that will set it to 15
     *
     * @param val
     * @todo move into the parent with appropriate 2^size overflow protection
     */
    public void set(int val) {
        if (val > 15) val = 15;
        super.set(val);
    }

    public void set(int bit3, int bit2, int bit1, int bit0) {
        setBit(0, bit0);
        setBit(1, bit1);
        setBit(2, bit2);
        setBit(3, bit3);
    }
}
