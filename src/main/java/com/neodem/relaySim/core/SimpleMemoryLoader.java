package com.neodem.relaySim.core;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.objects.component.memory.Memory;
import org.springframework.beans.factory.annotation.Required;

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

        List<BitField> buffer = block.getData();

        memory.chipSelect(true);
        for(int i = 0; i<buffer.size(); i++) {
            addressBus.updateData(BitField.createFromInt(address));
            dataBus.updateData(buffer.get(i));
            memory.write(true);
            address++;
        }
        memory.chipSelect(false);
    }

    @Required
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
}
