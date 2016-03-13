package com.neodem.relaySim.objects.tools;

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

    public static String makeString(List<Integer> bitList) {
        StringBuffer b = new StringBuffer();

        for(int i=bitList.size()-1; i>=0; i--) {
            int val = bitList.get(i);
            if(val == 1) b.append('1');
            else b.append('0');
        }

        return b.toString();
    }
}

