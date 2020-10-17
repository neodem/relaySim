package com.neodem.relaySim.data;

import com.neodem.relaySim.objects.component.Component;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public class Bus extends Component {

    private BitField data;

    private Collection<BusListener> listeners;

    public Bus() {
    }

    public Bus(int size, String name) {
        super(size, name);
        init();
    }

    public void init() {
        data = BitFieldBuilder.createWithSize(size);
    }

    public void addListener(BusListener c) {
        if (listeners == null) listeners = new HashSet<>();
        listeners.add(c);
    }

    public BitField getData() {
        return data;
    }

    public void updateData(BitField bitField) {
        this.data = bitField;
        alertListeners();
    }

    private void alertListeners() {
        if (listeners != null) {
            for (BusListener c : listeners) {
                c.dataChanged(this);
            }
        }
    }
}
