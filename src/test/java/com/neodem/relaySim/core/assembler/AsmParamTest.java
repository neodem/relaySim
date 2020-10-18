package com.neodem.relaySim.core.assembler;

import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class AsmParamTest {

    @Test
    public void createAddressShouldWork() throws Exception {
        AsmParam p = new AsmParam("$010");
        assertThat(p.isAddress()).isTrue();
        assertThat(p.getAddress()).isEqualTo(BitFieldBuilder.create(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0));
    }

    @Test
    public void createImmediateShouldWork() throws Exception {
        AsmParam p = new AsmParam("#12");
        assertThat(p.isImmediate()).isTrue();
        assertThat(p.getImmediate().intValue()).isEqualTo(12);
    }
}
