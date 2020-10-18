package com.neodem.relaySim.config;

import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.objects.component.alu.ALU;
import com.neodem.relaySim.objects.component.alu.BusBasedALUBridge;
import com.neodem.relaySim.objects.component.alu.SoftwareALU;
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
    public ALU alu() {
        return new SoftwareALU(dataWidth);
    }

    @Bean
    public BusBasedALUBridge aluComponent(ALU actualALU, Bus aluABus, Bus aluBBus, Bus aluOutBus, Bus aluControlBus) {
        BusBasedALUBridge alu = new BusBasedALUBridge(dataWidth, "ALU", actualALU);
        alu.setAluAin(aluABus);
        alu.setAluBin(aluBBus);
        alu.setAluControl(aluControlBus);
        alu.setAluOut(aluOutBus);
        alu.init();
        return alu;
    }

}
