package com.neodem.relaySim.core;

import com.neodem.relaySim.core.assembler.Assembler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/11/16
 */
public class System {

    private final ApplicationContext context;

    public System(ApplicationContext context) throws IOException, URISyntaxException {
        this.context = context;

        Assembler assembler = (Assembler)context.getBean("assembler");

        URI fileLocation = ClassLoader.getSystemResource("testProgramA.asm").toURI();

        String program = new String(Files.readAllBytes(Paths.get(fileLocation)));
        MemoryBlock block = assembler.assemble(program);

        MemoryLoader loader = (SimpleMemoryLoader)context.getBean("simpleMemoryLoader");
        loader.loadData(block);


    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ApplicationContext context = new ClassPathXmlApplicationContext("/system-context.xml");
        new System(context);
    }
}
