package com.neodem.relaySim.objects;

/**
 * Created by Vincent Fumo (neodem@gmail.com)
 * Created on 3/27/16
 */
public interface Changer {
    void addListener(Listener c);
    BitField getData();
}
