package com.neodem.relaySim.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/11/16
 */
public class System {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("system-context.xml");
    }
}
