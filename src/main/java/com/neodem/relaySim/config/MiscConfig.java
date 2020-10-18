package com.neodem.relaySim.config;

import com.neodem.relaySim.core.assembler.Assembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/18/20
 */
@Configuration
public class MiscConfig {

    @Bean
    public Assembler assembler() {
        return new Assembler();
    }
}
