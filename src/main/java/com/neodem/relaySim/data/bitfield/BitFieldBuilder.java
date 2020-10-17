package com.neodem.relaySim.data.bitfield;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/13/16
 */
public class BitFieldBuilder {

    public static BitField createWithSize(int size) {
        return new BitFieldImpl(size);
    }

    public static BitField create(boolean... values) {
        BitField result = new BitFieldImpl(values.length);
        for (int i = 0; i < values.length; i++) {
            result.setBit(values.length - i - 1, values[i]);
        }
        return result;
    }

    public static BitField create(int... values) {
        BitField result = new BitFieldImpl(values.length);
        result.setBits(values);
        return result;
    }

    public static BitField createFromInt(int value) {
        String bitstring = Integer.toString(value, 2);
        int size = bitstring.length();
        BitField result = new BitFieldImpl(size);
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
        BitField result = createFromInt(value);
        result.resize(fieldSize);
        return result;
    }

    /**
     * make a new BF by combining multiple BFs by adding then shifting left to accomodate the next one
     *
     * eg. placing them next to each other. The first one
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
     */
    public static BitField combineShiftLeft(BitField... fields) {
        BitField result = new BitFieldImpl(fields[0]);

        for (int i = 1; i < fields.length; i++) {
            result.shiftAndAddToRight(fields[i]);
        }

        return result;
    }

    /**
     * will create a bitField from the given bytes. it will have a size of 8 * bytes.length. Each byte
     * after the first one will shift everything left and then do it's add.. Thus, the first byte in this
     * array is the MSB.
     *
     * @param bytes
     * @return
     */
    public static BitField createFromBytes(byte... bytes) {
        BitField[] bitFields = new BitField[bytes.length];

        for(int i =0 ; i < bytes.length; i++) {
            bitFields[i] = createFromInt(bytes[i], 8);
        }

        return combineShiftLeft(bitFields);
    }
}
