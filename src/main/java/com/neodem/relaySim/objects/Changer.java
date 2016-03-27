package com.neodem.relaySim.objects;

/**
 * Created by vfumo on 3/27/16.
 */
public interface Changer {
    void addListener(Listener c);
    BitField getData();
}
