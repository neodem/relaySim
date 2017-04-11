package com.neodem.roulette;

import java.util.Random;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 5/26/16
 */
public class Generator {
    private static final Random RANDOM = new Random(System.currentTimeMillis() - 1230L);

    public static final short DOUBLE_ZERO = 37;

    public short getNext() {
        int value = RANDOM.nextInt(38);
        return (short) value;
    }
}
