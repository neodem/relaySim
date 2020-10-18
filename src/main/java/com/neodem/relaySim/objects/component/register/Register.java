package com.neodem.relaySim.objects.component.register;

import com.neodem.relaySim.data.Bus;

/**
 * The Register will always have it's internal state sent to it's out bus.
 * <p>
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/17/16
 */
public interface Register {

    void chipSelect(boolean selected);

    void write(boolean write);

    void setInBus(Bus inBus);

    Bus getInBus();

    void setOutBus(Bus outBus);

    Bus getOutBus();
}
