package com.neodem.relaySim.objects.component;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/11/16
 */
public abstract class Component {

    protected int size;
    protected String name;
    protected boolean initCalled = false;

    public Component() {
    }

    public Component(int size, String name) {
        this.size = size;
        this.name = name;
    }

    public void init() {
        initCalled = true;
    }

    public String toString() {
        return name;
    }

    @Required
    public void setSize(int size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Component component = (Component) o;

        return new EqualsBuilder()
                .append(name, component.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
