package com.neodem.relaySim.tools;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by vfumo on 3/13/16.
 */
public class BitToolsTest {
    @Test
    public void testBit() throws Exception {
        assertThat(BitTools.bit(0,0)).isFalse();
        assertThat(BitTools.bit(0,1)).isTrue();
        assertThat(BitTools.bit(1,1)).isFalse();
        assertThat(BitTools.bit(2,4)).isTrue();
    }
}
