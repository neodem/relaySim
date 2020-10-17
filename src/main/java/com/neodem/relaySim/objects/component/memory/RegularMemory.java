package com.neodem.relaySim.objects.component.memory;

import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.Bus;
import com.neodem.relaySim.data.BusListener;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import org.springframework.beans.factory.annotation.Required;

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

    private String name = "RAM";

    private int currentAddress;

    public RegularMemory() {
    }

    /**
     * @param addressWidth size of the addresssing (in bits)
     * @param dataWidth    size of a word (in bits)
     * @param size         number of words to store
     */
    public RegularMemory(String name, int addressWidth, int dataWidth, int size) throws Exception {
        this.addressWidth = addressWidth;
        this.dataWidth = dataWidth;
        this.size = size;
        this.name = name;

        init();
    }

    public void init() throws Exception {
        if (size <= 0 || addressWidth <= 0 || dataWidth <= 0)
            throw new IllegalAccessException("init() needs to be called with non zero addressWidth, dataWidth and size");

        //todo if size > addressWidth we should warn

        data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            BitField toStore = BitFieldBuilder.createFromInt(0, dataWidth);
            data.add(toStore);
        }

        if(addressBus == null) {
            addressBus = new Bus(addressWidth, name + "-Bus-Address");
            addressBus.addListener(this);
        }
        if(dataBus == null) dataBus = new Bus(dataWidth, name + "-Bus-Data");

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
            data.set(currentAddress, dataBus.getData().copy());
        }
    }

    @Override
    public void read(boolean read) {
        if (!initCalled)
            throw new RuntimeException("init has not been called! This component has not been initialized!");
        if (selected && read) {
            dataBus.updateData(data.get(currentAddress).copy());
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

    @Required
    public void setAddressWidth(int addressWidth) {
        this.addressWidth = addressWidth;
    }

    @Required
    public void setDataWidth(int dataWidth) {
        this.dataWidth = dataWidth;
    }

    @Required
    public void setSize(int size) {
        this.size = size;
    }

    public Bus getAddressBus() {
        return addressBus;
    }

    public Bus getDataBus() {
        return dataBus;
    }

    public String getName() {
        return name;
    }

    @Required
    public void setName(String name) {
        this.name = name;
    }
}
