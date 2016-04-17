package com.neodem.relaySim.objects.component.memory;

import com.neodem.relaySim.data.BitField;
import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.data.BusListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class Memory implements BusListener {

    private Bus addressBus;
    private Bus dataBus;
    private Bus controlBus;

    private int addressWidth = 0;
    private int dataWidth = 0;
    private int size = 0;

    private List<BitField> data;

    private boolean initCalled = false;

    public Memory() {
    }

    /**
     * @param addressWidth size of the addresssing (in bits)
     * @param dataWidth    size of a word (in bits)
     * @param size         number of words to store
     */
    public Memory(int addressWidth, int dataWidth, int size) throws Exception {
        this.addressWidth = addressWidth;
        this.dataWidth = dataWidth;
        this.size = size;

        init();
    }

    public void init() throws Exception {
        if (size <= 0 || addressWidth <= 0 || dataWidth <= 0)
            throw new IllegalAccessException("init() needs to be called with non zero addressWidth, dataWidth and size");

        //todo if size > addressWidth we should warn

        data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            BitField toStore = BitField.createFromInt(0, dataWidth);
            data.add(toStore);
        }

        initCalled = true;
    }

    @Override
    public void dataChanged(Bus b) {
        if (!initCalled) throw new RuntimeException("init has not been called!");

        BitField control = b.getData();
        int address = addressBus.getData().intValue();
        if (address >= size) address = size - 1;
        if (control.intValue() == 1) {
            // read
            dataBus.updateData(data.get(address));
        } else if (control.intValue() == 2) {
            // write
            data.set(address, new BitField(dataBus.getData()));
        }
    }

    public void setAddressBus(Bus addressBus) {
        this.addressBus = addressBus;
    }

    public void setDataBus(Bus dataBus) {
        this.dataBus = dataBus;
    }

    public void setControlBus(Bus controlBus) {
        this.controlBus = controlBus;
        this.controlBus.addListener(this);
    }

    public void setAddressWidth(int addressWidth) {
        this.addressWidth = addressWidth;
    }

    public void setDataWidth(int dataWidth) {
        this.dataWidth = dataWidth;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Bus getAddressBus() {
        return addressBus;
    }

    public Bus getDataBus() {
        return dataBus;
    }

    public Bus getControlBus() {
        return controlBus;
    }
}
