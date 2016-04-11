package com.neodem.relaySim.objects.register;

import com.neodem.relaySim.objects.BitField;
import com.neodem.relaySim.objects.Changer;
import com.neodem.relaySim.objects.ChangingComponent;
import com.neodem.relaySim.objects.Listener;

/**
 * Created by : Vincent Fumo (neodem@gmail.com)
 * Created on : 3/25/16
 */
public class Register extends ChangingComponent implements Listener, Changer {
    private BitField input;

    @Override
    public void changed(Changer c) {
        input = c.getData();
    }

    public void init() {
        super.init();
        input = new BitField(size);
    }

    public void store() {
        super.updateData(new BitField(this.input));
    }
}
