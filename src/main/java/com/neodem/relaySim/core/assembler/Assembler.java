package com.neodem.relaySim.core.assembler;

import com.neodem.relaySim.core.MemoryBlock;
import com.neodem.relaySim.data.BitField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class Assembler {

    private final static BitField DEFAULT_START = BitField.create(0,0,0,0, 0,0,0,1, 0,0,0,0);
    private final static BitField LDA_IMMEDIATE = BitField.create(0,0,0,1);
    private final static BitField LDA_ADDRESS   = BitField.create(0,0,1,0);
    private final static BitField ADC_IMMEDIATE = BitField.create(0,0,1,1);
    private final static BitField ADC_ADDRESS   = BitField.create(0,1,0,0);
    private final static BitField STA_ADDRESS   = BitField.create(0,1,0,1);

    private BitField org;
    private List<BitField> data = new ArrayList<>();

    public MemoryBlock assemble(String assemblyProgram) {
        String[] lines = StringUtils.split(assemblyProgram, '\n');

        org = DEFAULT_START;

        for(String line : lines) {
            if(StringUtils.startsWith(line, "ORG")) handleOrg(new AsmParam(StringUtils.substringAfter(line, "ORG")));
            else if(StringUtils.startsWith(line, "LDA")) handleLDA(new AsmParam(StringUtils.substringAfter(line, "LDA")));
            else if(StringUtils.startsWith(line, "ADC")) handleADC(new AsmParam(StringUtils.substringAfter(line, "ADC")));
            else if(StringUtils.startsWith(line, "STA")) handleSTA(new AsmParam(StringUtils.substringAfter(line, "STA")));
        }

        return new MemoryBlock(org, data);
    }

    private void handleOrg(AsmParam param) {
        if(param.isAddress()) {
            org = param.getAddress();
        }
    }

    private void handleLDA(AsmParam param) {
        if(param.isImmediate()) {
            data.add(LDA_IMMEDIATE);
            data.add(param.getImmediate());
        } else if(param.isAddress()) {
            data.add(LDA_ADDRESS);
            data.add(param.getAddress());
        }
    }

    private void handleADC(AsmParam param) {
        if(param.isImmediate()) {
            data.add(ADC_IMMEDIATE);
            data.add(param.getImmediate());
        } else if(param.isAddress()) {
            data.add(ADC_ADDRESS);
            data.add(param.getAddress());
        }
    }

    private void handleSTA(AsmParam param) {
        if(param.isAddress()) {
            data.add(STA_ADDRESS);
            data.add(param.getAddress());
        }
    }
}
