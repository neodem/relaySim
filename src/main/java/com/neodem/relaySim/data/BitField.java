package com.neodem.relaySim.data;

import java.util.List;

/**
 * A BitField is an object representing some ordered collection
 * of bits. It is 0 indexed from right to left:
 * example : 0001, bit0 == 1
 * <p>
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/13/16
 */
public interface BitField {

    /**
     * @return the size of the field
     */
    int size();

    /**
     * set all values of the BitField with the rightmost value being index 0.
     * example set(0,0,0,1) would set bit0 == 1
     *
     * @param values the values to set the BitField (only 0 and 1 are accepted)
     * @return the BitField
     */
    void setBits(int... values);

    /**
     * set a given bit in the field
     *
     * @param index
     * @param value true == 1, false == 0
     * @throws IndexOutOfBoundsException if you try to set a bit greater than the size of the field
     */
    void setBit(int index, boolean value);

    /**
     * set a given bit in the field
     *
     * @param index
     * @param value (only 0 and 1 are accepted)
     * @throws IndexOutOfBoundsException if you try to set a bit greater than the size of the field
     */
    void setBit(int index, int value);

    /**
     * replace all values of the bitfield with this integer value
     *
     * @param val
     * @return
     */
    void setToValue(int val);

    /**
     * invert all the bits
     */
    void invertAllBits();

    /**
     * get a given bit as a boolean
     *
     * @param index
     * @return a boolean for the bit (true == 1, false == 0)
     * @throws IndexOutOfBoundsException if you ask for a bit our of range
     */
    boolean getBitAsBoolean(int index);

    /**
     * get a given bit as a 0 or 1
     *
     * @param index
     * @return a value for the bit (1 == 1, 0 == 0)
     * @throws IndexOutOfBoundsException if you ask for a bit our of range
     */
    int getBit(int index);

    /**
     * get the field as an integer
     *
     * @return the int value of the bitfield
     */
    int intValue();

    /**
     * return a nice string representation of the field
     *
     * @return
     */
    String asString();

    /**
     * get the LSBs of the BitField (if possible)
     *
     * @param digits the number of LSBs to return
     * @return a new BitField of the LSBs
     */
    BitField getLSBs(int digits);

    /**
     * get the MSBs of the BitField (if possible)
     *
     * @param digits the number of MSBs to return
     * @return a new BitField of the MSBs
     */
    BitField getMSBs(int digits);

    /**
     * return a new BitField. Inclusive
     *
     * @param from index of the from (0==LSB)
     * @param to   index of the to (max is the size-1 of the field)
     * @return a new BitField of the chosen bits
     * @throws IndexOutOfBoundsException if you ask for a bit our of range
     */
    BitField getSubField(int from, int to);

    /**
     * a convenience method to add to the LS Bits of the BitField.
     * this will resize the BitField, shift everything to the left and insert
     * the new BitField
     *
     * @param fieldToAddToTheRight
     */
    void shiftAndAddToRight(BitField fieldToAddToTheRight);

    /**
     * shift to the left and pad the new LSBs with 0's
     *
     * @param numberToShift the number of bits to shift
     */
    void shiftLeft(int numberToShift);

    /**
     * shift to the right and have the LSBs dissapear
     *
     * @param numberToShift the number of bits to shift
     */
    void rightShift(int numberToShift);

    /**
     * resize, but trimming/adding to the MSB side
     * <p>
     * if newSize > currentSize, we pad with 0's on the MSBs
     * if newSize < currentSize, we simply drop the bits on the left
     *
     * @param newSize the new size of the BitField
     */
    void resize(int newSize);

    /**
     * copy the BitField
     *
     * @return a copy of the bitField
     */
    BitField copy();

    /**
     * convert the bit field and return as a collection of bytes made up from the BitField from the LSB to the MSB. Padding in the MSB if needed
     * (byte == 8 bits).
     * <p>
     * For example:
     * 00001000 would return a list with a single byte value of 8
     * 1000001000 would return a list with the first value being a byte with a value of 8, then one with a value of 2
     *
     * @return a collection with the LSB in position 0 and the MSB as the last value
     */
    List<Byte> getAsBytes();

}
