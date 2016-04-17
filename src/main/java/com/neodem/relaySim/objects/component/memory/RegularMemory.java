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
public class RegularMemory implements Memory, BusListener {

    private Bus addressBus;
    private Bus dataBus;

    private int addressWidth = 0;
    private int dataWidth = 0;
    private int size = 0;

    private List<BitField> data;

    private boolean initCalled = false;
    private boolean selected = false;

    private int currentAddress;

    public RegularMemory() {
    }

    /**
     * @param addressWidth size of the addresssing (in bits)
     * @param dataWidth    size of a word (in bits)
     * @param size         number of words to store
     */
    public RegularMemory(int addressWidth, int dataWidth, int size) throws Exception {
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
    public void chipSelect(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void write(boolean write) {
        if (!initCalled)
            throw new RuntimeException("init has not been called! This component has not been initialized!");
        if (selected && write) {
            data.set(currentAddress, new BitField(dataBus.getData()));
        }
    }

    @Override
    public void read(boolean read) {
        if (!initCalled)
            throw new RuntimeException("init has not been called! This component has not been initialized!");
        if (selected && read) {
            dataBus.updateData(new BitField(data.get(currentAddress)));
        }
    }

    @Override
    public void dataChanged(Bus b) {
        BitField busData = b.getData();

        int desiredAddress = busData.intValue();

        if (desiredAddress >= size) {
            currentAddress = size - 1;
        } else {
            currentAddress = desiredAddress;
        }
    }

    public void setAddressBus(Bus addressBus) {
        this.addressBus = addressBus;
        this.addressBus.addListener(this);
    }

    public void setDataBus(Bus dataBus) {
        this.dataBus = dataBus;
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

}
