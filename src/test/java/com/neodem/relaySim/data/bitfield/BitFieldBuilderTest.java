package com.neodem.relaySim.data.bitfield;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/17/20
 */
public class BitFieldBuilderTest {

    @Test
    public void createFromBytesShouldCreateFromOneByte() {

        BitField result = BitFieldBuilder.createFromBytes((byte)7);

        assertThat(result.size()).isEqualTo(8);
        assertThat(result.getBitAsInt(0)).isEqualTo(1);
        assertThat(result.getBitAsInt(1)).isEqualTo(1);
        assertThat(result.getBitAsInt(2)).isEqualTo(1);
        assertThat(result.getBitAsInt(3)).isEqualTo(0);
        assertThat(result.getBitAsInt(4)).isEqualTo(0);
        assertThat(result.getBitAsInt(5)).isEqualTo(0);
        assertThat(result.getBitAsInt(6)).isEqualTo(0);
        assertThat(result.getBitAsInt(7)).isEqualTo(0);
    }
    @Test
    public void createFromBytesShouldCreateFromTwoBytes() {

        BitField result = BitFieldBuilder.createFromBytes((byte)7, (byte)5);

        assertThat(result.size()).isEqualTo(16);
        assertThat(result.getBitAsInt(0)).isEqualTo(1);
        assertThat(result.getBitAsInt(1)).isEqualTo(0);
        assertThat(result.getBitAsInt(2)).isEqualTo(1);
        assertThat(result.getBitAsInt(3)).isEqualTo(0);
        assertThat(result.getBitAsInt(4)).isEqualTo(0);
        assertThat(result.getBitAsInt(5)).isEqualTo(0);
        assertThat(result.getBitAsInt(6)).isEqualTo(0);
        assertThat(result.getBitAsInt(7)).isEqualTo(0);
        assertThat(result.getBitAsInt(8)).isEqualTo(1);
        assertThat(result.getBitAsInt(9)).isEqualTo(1);
        assertThat(result.getBitAsInt(10)).isEqualTo(1);
        assertThat(result.getBitAsInt(11)).isEqualTo(0);
        assertThat(result.getBitAsInt(12)).isEqualTo(0);
        assertThat(result.getBitAsInt(13)).isEqualTo(0);
        assertThat(result.getBitAsInt(14)).isEqualTo(0);
        assertThat(result.getBitAsInt(15)).isEqualTo(0);
    }
}
