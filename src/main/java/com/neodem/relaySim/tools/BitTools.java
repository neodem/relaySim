package com.neodem.relaySim.tools;

import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vfumo on 3/13/16.
 */
public class BitTools {
    /**
     * return a string of bits representing the value given
     *
     * @param value the value to convert
     * @param len   the string length (should be big enough to hold the value)
     * @return
     */
    public static String toBinaryString(int value, int len) {
        double pow = Math.pow(2, len);
        if (pow < value) {
            throw new IllegalArgumentException("The Value is too big for a " + len + " bit long String");
        }
        StringBuilder binary = new StringBuilder();
        int shift = len - 1;
        for (; shift >= 0; shift--) {
            int bit = (value >> shift) & 1;
            if (bit == 1) {
                binary.append("1");
            } else {
                binary.append("0");
            }
        }
        return binary.toString();
    }


    public static boolean bit(int bitIndex, int value) {
        int mask = 1 << bitIndex;
        int masked_n = value & mask;
        int thebit = masked_n >> bitIndex;

        return thebit == 1;
    }

    /**
     * make a consectutive list of BitField objects for all the possible values
     * <p>
     * eg: 00, 01, 10, 11
     *
     * @param size size of the bitfield
     * @return a consecutive list of BitField objects for all the possible values
     */
    public static List<BitField> makeListOfFields(int size) {

        // the number of fields we will need
        double number = Math.pow(2, size);

        List<BitField> allFields = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            BitField field = BitFieldBuilder.createWithSize(size);
            field.setToValue(i);
            allFields.add(field);
        }

        return allFields;
    }
}

