package com.neodem.relaySim.tools;

import com.neodem.relaySim.data.BitField;

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

    /**
     * @param value
     * @return
     * @paraam len
     */
    public static List<Integer> convertToList(int value, int len) {
        double pow = Math.pow(2, len);
        if (pow < value) {
            throw new IllegalArgumentException("The Value is too big for a " + len + " bit long String");
        }

        List<Integer> result = new ArrayList<>(len);
        int shift = len - 1;
        for (; shift >= 0; shift--) {
            int bit = (value >> shift) & 1;
            if (bit == 1) {
                result.add(0,1);
            } else {
                result.add(0,0);
            }
        }

        return result;
    }

    public static int makeInt(List<Boolean> data) {
        int result = 0;

        for(int i=0; i<data.size(); i++) {
            boolean bit = data.get(i);
            if(bit) {
                double pow = Math.pow(2, i);
                result += pow;
            }
        }

        return result;
    }

    public static boolean bit(int bitIndex, int value) {
        int mask =  1 << bitIndex;
        int masked_n = value & mask;
        int thebit = masked_n >> bitIndex;

        return thebit == 1;
    }

    /**
     * make a consectutive list of BitField objects for all the possible values
     *
     * eg: 00, 01, 10, 11
     *
     * @param size size of the bitfield
     * @return a consecutive list of BitField objects for all the possible values
     */
    public static List<BitField> makeListOfFields(int size) {

        // the number of fields we will need
        double number = Math.pow(2,size);

        List<BitField> allFields = new ArrayList<>();

        for(int i=0 ; i < number ; i++) {
            BitField field = new BitField(size);
            field.setValue(i);
            allFields.add(field);
        }

        return allFields;
    }
}

