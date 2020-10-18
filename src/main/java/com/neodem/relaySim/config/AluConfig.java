package com.neodem.relaySim.config;

import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.objects.component.Component;
import com.neodem.relaySim.objects.component.alu.BusBasedALU;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/18/20
 */
@Configuration
public class AluConfig {

    @Value("${sim.data.width}")
    private int dataWidth;

    @Bean
    public Component alu(Bus aluABus, Bus aluBBus, Bus aluOutBus, Bus aluControlBus) {
        BusBasedALU alu = new BusBasedALU(dataWidth, "ALU");
        alu.setAluAin(aluABus);
        alu.setAluBin(aluBBus);
        alu.setAluControl(aluControlBus);
        alu.setAluOut(aluOutBus);
        alu.init();
        return alu;
    }

}
