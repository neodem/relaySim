package com.neodem.relaySim.core;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.objects.component.memory.Memory;

import java.util.List;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class SimpleMemoryLoader implements MemoryLoader {

    private Memory memory;

    public void loadData(MemoryBlock block) {
        int address = block.getStartLocation().intValue();

        Bus addressBus = memory.getAddressBus();
        Bus dataBus = memory.getDataBus();
        Bus controlBus = memory.getControlBus();

        List<BitField> buffer = block.getData();

        for(int i = 0; i<buffer.size(); i++) {
            addressBus.updateData(BitField.createFromInt(address));
            dataBus.updateData(buffer.get(i));
            controlBus.updateData(BitField.create(1,0));
            address++;
        }
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }
}
