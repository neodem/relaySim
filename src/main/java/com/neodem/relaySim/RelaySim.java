package com.neodem.relaySim;

import com.neodem.relaySim.core.MemoryBlock;
import com.neodem.relaySim.core.MemoryLoader;
import com.neodem.relaySim.core.SimpleMemoryLoader;
import com.neodem.relaySim.core.assembler.Assembler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/11/16
 */
@SpringBootApplication
public class RelaySim {

    public static void main(String[] args) {
        SpringApplication.run(RelaySim.class, args);
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws Exception {
        ApplicationContext context = event.getApplicationContext();

        Assembler assembler = (Assembler) context.getBean("assembler");
        URI fileLocation = ClassLoader.getSystemResource("testProgramA.asm").toURI();
        String program = new String(Files.readAllBytes(Paths.get(fileLocation)));

        // assemble the program into a block of memory
        MemoryBlock block = assembler.assemble(program);

        // load the data into ram
        MemoryLoader loader = (SimpleMemoryLoader) context.getBean("memoryLoader");
        loader.loadData(block);


    }
}
