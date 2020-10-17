package com.neodem.relaySim.data;

import com.neodem.relaySim.tools.BitTools;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * all BitField objects need a size of at least 1.
 * indexing is from right to left:
 * example : 0001, bit0 == 1
 * <p>
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
     * make a new BF by combining multiple BFs by placing them next to each other. The first one
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
    public static BitField combine(BitField... fields) {
        BitField result = new BitFieldImpl(fields[0]);

        for (int i = 1; i < fields.length; i++) {
            result.shiftAndAddToRight(fields[i]);
        }

        return result;
    }

    protected static class BitFieldImpl implements BitField {

        // 0 is the LSB
        private List<Boolean> data;
        private int size;

        /**
         * copy constructor
         *
         * @param bitField the BitField to make a copy of
         */
        protected BitFieldImpl(BitField bitField) {
            this(bitField.size());
            for (int i = 0; i < size; i++) {
                setBit(i, bitField.getBit(i));
            }
        }

        /**
         * init a new BitField with a given size and set all values to 0
         *
         * @param size the size of the BitField
         */
        protected BitFieldImpl(int size) {
            if (size < 1) throw new IllegalArgumentException("BitField should have a size of at least 1");
            this.size = size;
            this.data = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                this.data.add(false);
            }
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
            data.set(index, value);
        }

        @Override
        public void setBit(int index, int value) {
            if(index < 0) throw new IndexOutOfBoundsException("index must be equal to or greater than 0");
            if(index >= size) throw new IndexOutOfBoundsException("index must be less than size");

            if(value != 1 || value != 0) throw new IllegalArgumentException("value may only be a 0 or 1");

            boolean val = true;
            if (value == 0) val = false;
            data.set(index, val);
        }

        @Override
        public void setToValue(int val) {
            List<Integer> bits = BitTools.convertToList(val, size);
            for (int i = 0; i < size; i++) {
                setBit(i, bits.get(i));
            }
        }

        @Override
        public void invertAllBits() {
            for (int i = 0; i < size; i++) {
                boolean newBit = !getBitAsBoolean(i);
                setBit(i, newBit);
            }
        }

        @Override
        public boolean getBitAsBoolean(int pos) {
            if (pos >= size)
                throw new IllegalArgumentException(String.format("trying to get bit %d of a %d bit field", pos, size));
            return data.get(pos);
        }

        @Override
        public int getBit(int pos) {
            if (pos >= size)
                throw new IllegalArgumentException(String.format("trying to get bit %d of a %d bit field", pos, size));
            boolean bit = getBitAsBoolean(pos);
            return bit ? 1 : 0;
        }

        @Override
        public int intValue() {
            return BitTools.makeInt(data);
        }

        @Override
        public String asString() {
            StringBuffer b = new StringBuffer();

            for (int i = size - 1; i >= 0; i--) {
                boolean val = data.get(i);
                if (val) b.append('1');
                else b.append('0');
            }

            return b.toString();
        }

        @Override
        public BitField getLSBs(int digits) {
            if (digits >= size)
                throw new IllegalArgumentException("can't get more bits (" + digits + ") than the size (" + size + ") of the field!");

            BitField result = new BitFieldImpl(digits);

            for (int i = 0; i < digits; i++) {
                result.setBit(i, getBit(i));
            }

            return result;
        }

        @Override
        public BitField getMSBs(int digits) {
            if (digits >= size)
                throw new IllegalArgumentException("can't get more bits (" + digits + ") than the size (" + size + ") of the field!");

            BitField result = new BitFieldImpl(digits);

            for (int i = 0; i < digits; i++) {
                int j = size - digits + i;
                result.setBit(i, getBit(j));
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
                throw new IllegalArgumentException("can't get more bits (" + requestedSize + ") than the size (" + size + ") of the field!");

            BitField result = new BitFieldImpl(requestedSize);

            for (int i = 0; i < requestedSize; i++) {
                int j = from + i;
                result.setBit(i, getBit(j));
            }

            return result;
        }

        @Override
        public void shiftAndAddToRight(BitField fieldToAddToTheRight) {
            int bitsToAdd = fieldToAddToTheRight.size();
            shiftLeft(bitsToAdd);
            for (int i = 0; i < bitsToAdd; i++) {
                data.set(i, fieldToAddToTheRight.getBitAsBoolean(i));
            }
        }

        @Override
        public void shiftLeft(int numberToShift) {
            int oldSize = this.size;
            resize(numberToShift + oldSize);

            int offset = this.size - oldSize;

            // shift old stuff left
            for (int i = oldSize - 1; i >= 0; i--) {
                Boolean value = data.get(i);
                data.set(i + offset, value);
            }

            // pad with 0 for stuff moved
            for (int i = 0; i < numberToShift; i++) {
                data.set(i, false);
            }
        }

        @Override
        public void rightShift(int numberToShift) {

        }

        @Override
        public void resize(int newSize) {
            if (newSize > size) {
                int bitsToAdd = newSize - size;
                for (int i = 0; i < bitsToAdd; i++) {
                    data.add(size + i, false);
                }
                this.size = newSize;
            } else if (newSize < size) {
                int bitsToRemove = size - newSize;
                for (int i = 0; i < bitsToRemove; i++) {
                    data.remove(size - i - 1);
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
            //        ListBasedBitField copy = this.copy();
//
//        // 0 is lsb, size is the msb
//        Collection<Byte> bytes = new ArrayList<>();
//
//        while(copy.size() >= 8) {
//            ListBasedBitField rightField = copy.getSubField(0,7);
//
//            // if field is greater than 8, pull out the rightmost 8 and make a byte from it.
//            // take the result minus the 8 and do again recursivlye
//        }
//
//        // if the field is less than 8, pad it with 0's and get byte and end
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            BitFieldImpl bitField = (BitFieldImpl) o;

            return new EqualsBuilder()
                    .append(size, bitField.size)
                    .append(data, bitField.data)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(271, 151)
                    .append(data)
                    .append(size)
                    .toHashCode();
        }
    }
}
