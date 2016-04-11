package com.neodem.relaySim.objects;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/11/16
 */
public class ChangingComponent extends Component implements Changer {
    private BitField data;

    private Collection<Listener> listeners;

    public ChangingComponent() {
    }

    public ChangingComponent(int size, String name) {
        super(size, name);
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
}
