package com.neodem.relaySim.config;

import com.neodem.relaySim.data.Bus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/18/20
 */
@Configuration
public class BusConfig {

    @Value("${sim.data.width}")
    private int dataWidth;

    @Bean
    public Bus aluABus() {
        return new Bus(dataWidth, "aluABus");
    }

    @Bean
    public Bus aluBBus() {
        return new Bus(dataWidth, "aluBBus");
    }

    @Bean
    public Bus aluOutBus() {
        return new Bus(dataWidth, "aluOutBus");
    }

    @Bean
    public Bus aluControlBus() {
        return new Bus(dataWidth, "aluControlBus");
    }

    @Bean
    public Bus accumulatorInputBus() {
        return new Bus(dataWidth, "accumulatorInputBus");
    }
}
