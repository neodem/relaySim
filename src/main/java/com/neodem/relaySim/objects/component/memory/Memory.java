package com.neodem.relaySim.objects.component.memory;

import com.neodem.relaySim.data.Bus;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public interface Memory {

    void chipSelect(boolean selected);

    void write(boolean write);

    void read(boolean read);

    void setAddressBus(Bus addressBus);

    Bus getAddressBus();

    void setDataBus(Bus dataBus);

    Bus getDataBus();
}
