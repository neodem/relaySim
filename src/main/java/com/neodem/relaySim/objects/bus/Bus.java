package com.neodem.relaySim.objects.bus;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.Changer;
import com.neodem.relaySim.objects.Listener;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public class Bus implements Changer {
    private BitField data;
    private String name;

    private Collection<Listener> listeners;

    private int size;
    public Bus() {
    }

    public Bus(String name, int size) {
        this.size = size;
        this.name = name;
        init();
    }

    public void init() {
        data = new BitField(size);
    }

    public void addListener(Listener c) {
        if (listeners == null) listeners = new HashSet<>();
        listeners.add(c);
    }

    public void updateData(BitField bitField) {
        this.data = bitField;
        alertListeners();
    }

    public BitField getData() {
        return data;
    }

    private void alertListeners() {
        for (Listener c : listeners) {
            c.changed(this);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Required
    public void setSize(int size) {
        this.size = size;
    }
}
