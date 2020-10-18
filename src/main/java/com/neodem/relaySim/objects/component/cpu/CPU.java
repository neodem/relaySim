package com.neodem.relaySim.objects.component.cpu;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/18/20
 */
public interface CPU {

    /**
     * have the CPU execute operations against the connected RAM starting at the given address
     *
     * @param startAddress
     */
    void startExecution(int startAddress);
}
