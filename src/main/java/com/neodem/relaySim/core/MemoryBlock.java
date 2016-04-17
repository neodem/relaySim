package com.neodem.relaySim.core;

import com.neodem.relaySim.data.BitField;

import java.util.List;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 4/16/16
 */
public class MemoryBlock {
    private BitField startLocation;
    private List<BitField> data;

    public MemoryBlock(BitField startLocation, List<BitField> data) {
        this.startLocation = startLocation;
        this.data = data;
    }

    public BitField getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(BitField startLocation) {
        this.startLocation = startLocation;
    }

    public List<BitField> getData() {
        return data;
    }

    public void setData(List<BitField> data) {
        this.data = data;
    }

    public int size() {
        return data.size();
    }
}
