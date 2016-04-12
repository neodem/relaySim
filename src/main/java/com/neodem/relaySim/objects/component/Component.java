package com.neodem.relaySim.objects.component;

import org.springframework.beans.factory.annotation.Required;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/11/16
 */
public class Component {

    protected int size;
    protected String name;

    public Component() {
    }

    public Component(int size, String name) {
        this.size = size;
        this.name = name;
    }

    @Required
    public void setSize(int size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }
}
