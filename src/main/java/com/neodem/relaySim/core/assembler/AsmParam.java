package com.neodem.relaySim.core.assembler;

import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class AsmParam {
    private BitField immediate = null;
    private BitField address = null;

    public AsmParam(String paramString) {
        String clean = StringUtils.strip(paramString);

        if (StringUtils.startsWith(clean, "$")) {
            String hexString = StringUtils.substringAfter(clean, "$");
            address = BitFieldBuilder.createFromHex(hexString);
        } else if (StringUtils.startsWith(clean, "#")) {
            String intString = StringUtils.substringAfter(clean, "#");
            int intValue = Integer.parseInt(intString);
            immediate = BitFieldBuilder.createFromInt(intValue);
        }
    }

    public boolean isAddress() {
        return address != null;
    }

    public BitField getAddress() {
        return address;
    }

    public boolean isImmediate() {
        return immediate != null;
    }

    public BitField getImmediate() {
        return immediate;
    }
}
