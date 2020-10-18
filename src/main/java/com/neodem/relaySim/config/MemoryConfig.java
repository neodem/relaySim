package com.neodem.relaySim.config;

import com.neodem.relaySim.core.MemoryLoader;
import com.neodem.relaySim.core.SimpleMemoryLoader;
import com.neodem.relaySim.objects.component.memory.Memory;
import com.neodem.relaySim.objects.component.memory.RegularMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/18/20
 */
@Configuration
public class MemoryConfig {

    @Value("${sim.data.width}")
    private int dataWidth;

    @Value("${sim.address.width}")
    private int addressWidth;

    @Value("${sim.ram.bytes}")
    private int ramSize;

    @Bean
    public Memory mainRam() throws Exception {
        RegularMemory memory = new RegularMemory("RAM", addressWidth, dataWidth, ramSize);
        return memory;
    }

    @Bean
    public MemoryLoader memoryLoader(Memory mainRam) {
        return new SimpleMemoryLoader(mainRam);
    }
}
