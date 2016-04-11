package com.neodem.relaySim.tools;

import com.neodem.relaySim.objects.BitField;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by vfumo on 3/13/16.
 */
public class BitToolsTest {
    @Test
    public void testBit() throws Exception {
        assertThat(BitTools.bit(0, 0)).isFalse();
        assertThat(BitTools.bit(0, 1)).isTrue();
        assertThat(BitTools.bit(1, 1)).isFalse();
        assertThat(BitTools.bit(2, 4)).isTrue();
    }

    @Test
    public void makeListOfFieldsShouldWorkForSize1() throws Exception {
        List<BitField> result = BitTools.makeListOfFields(1);
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(BitField.create(0));
        assertThat(result.get(1)).isEqualTo(BitField.create(1));
    }

    @Test
    public void makeListOfFieldsShouldWorkForSize2() throws Exception {
        List<BitField> result = BitTools.makeListOfFields(2);
        assertThat(result).hasSize(4);
        assertThat(result.get(0)).isEqualTo(BitField.create(0, 0));
        assertThat(result.get(1)).isEqualTo(BitField.create(0, 1));
        assertThat(result.get(2)).isEqualTo(BitField.create(1, 0));
        assertThat(result.get(3)).isEqualTo(BitField.create(1, 1));
    }

    @Test
    public void makeListOfFieldsShouldWorkForSize3() throws Exception {
        List<BitField> result = BitTools.makeListOfFields(3);
        assertThat(result).hasSize(8);
        assertThat(result.get(0)).isEqualTo(BitField.create(0, 0, 0));
        assertThat(result.get(1)).isEqualTo(BitField.create(0, 0, 1));
        assertThat(result.get(2)).isEqualTo(BitField.create(0, 1, 0));
        assertThat(result.get(3)).isEqualTo(BitField.create(0, 1, 1));
        assertThat(result.get(4)).isEqualTo(BitField.create(1, 0, 0));
        assertThat(result.get(5)).isEqualTo(BitField.create(1, 0, 1));
        assertThat(result.get(6)).isEqualTo(BitField.create(1, 1, 0));
        assertThat(result.get(7)).isEqualTo(BitField.create(1, 1, 1));
    }
}