package com.neodem.relaySim.core.assembler;

import com.neodem.relaySim.core.MemoryBlock;
import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.data.BitFieldBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * all instructions fit into 4 4 bit words. the first being the operaion and the rest are the address
 * or the immediate value, or padding.
 *
 * ADDRESS INSTRUCTION :
 *  [INS][ADDRESS 11-8][ADDRESS 7-4][ADDRESS 3-0]
 * IMMEDIATE INSTRUCTION:
 *  [INS][IMMEDIATE][0000][0000]
 * OTHER INSTRUCTION:
 *  [INS][0000][0000][0000]
 *
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class Assembler {

    private final static BitField DEFAULT_START = BitFieldBuilder.create(0,0,0,0, 0,0,0,1, 0,0,0,0);

    private final static BitField LDA_IMMEDIATE = BitFieldBuilder.create(0,0,0,1);
    private final static BitField LDA_ADDRESS   = BitFieldBuilder.create(0,0,1,0);

    private final static BitField ADC_IMMEDIATE = BitFieldBuilder.create(0,0,1,1);
    private final static BitField ADC_ADDRESS   = BitFieldBuilder.create(0,1,0,0);

    private final static BitField STA_ADDRESS   = BitFieldBuilder.create(0,1,0,1);

    private final static BitField HLT           = BitFieldBuilder.create(0,0,0,0);

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
            else if(StringUtils.startsWith(line, "HLT")) handleHLT();
        }

        return new MemoryBlock(org, data);
    }

    private void handleOrg(AsmParam param) {
        if(param.isAddress()) {
            org = param.getAddress().copy();
            org.resize(12);
        }
    }

    private void handleHLT() {
        data.add(HLT.copy());
        data.add(BitFieldBuilder.create(0,0,0,0));
        data.add(BitFieldBuilder.create(0,0,0,0));
        data.add(BitFieldBuilder.create(0,0,0,0));
    }

    private void handleLDA(AsmParam param) {
        if(param.isImmediate()) {
            data.add(LDA_IMMEDIATE.copy());
            BitField copy = param.getImmediate().copy();
            copy.resize(4);
            data.add(copy);
            data.add(BitFieldBuilder.create(0,0,0,0));
            data.add(BitFieldBuilder.create(0,0,0,0));
        } else if(param.isAddress()) {
            data.add(LDA_ADDRESS.copy());
            data.add(param.getAddress().getMSBs(4));
            data.add(param.getAddress().getSubField(4,7));
            data.add(param.getAddress().getLSBs(4));
        }
    }

    private void handleADC(AsmParam param) {
        if(param.isImmediate()) {
            data.add(ADC_IMMEDIATE.copy());
            BitField copy = param.getImmediate().copy();
            copy.resize(4);
            data.add(copy);
            data.add(BitFieldBuilder.create(0,0,0,0));
            data.add(BitFieldBuilder.create(0,0,0,0));
        } else if(param.isAddress()) {
            data.add(ADC_ADDRESS.copy());
            data.add(param.getAddress().getMSBs(4));
            data.add(param.getAddress().getSubField(4,7));
            data.add(param.getAddress().getLSBs(4));
        }
    }

    private void handleSTA(AsmParam param) {
        if(param.isAddress()) {
            data.add(STA_ADDRESS.copy());
            data.add(param.getAddress().getMSBs(4));
            data.add(param.getAddress().getSubField(4,7));
            data.add(param.getAddress().getLSBs(4));
        }
    }
}
