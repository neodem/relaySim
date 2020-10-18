package com.neodem.relaySim.config;

import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.objects.component.register.Register;
import com.neodem.relaySim.objects.component.register.RegularRegister;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/18/20
 */
@Configuration
public class RegisterConfig {

    @Value("${sim.data.width}")
    private int dataWidth;

    @Bean
    public Register accumulator(Bus accumulatorInputBus, Bus aluABus) {
        RegularRegister register = new RegularRegister(dataWidth, "Accumulator", accumulatorInputBus);
        register.setOutBus(aluABus);

        register.init();
        return register;
    }

}
