package com.neodem.relaySim.objects.bus;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.Changer;
import com.neodem.relaySim.objects.Listener;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public class Bus implements Changer {
    private BitField data;
    private Collection<Listener> listeners;

    public Bus(int size) {
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
}
