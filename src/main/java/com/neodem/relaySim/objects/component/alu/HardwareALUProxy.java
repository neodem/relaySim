package com.neodem.relaySim.objects.component.alu;

import com.fazecast.jSerialComm.SerialPort;
import com.neodem.relaySim.data.BitField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * proxy to our 4 bit hardware ALU (connected over USB)
 * <p>
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/15/20
 */
public class HardwareALUProxy implements ALU {

    private static Logger logger = LoggerFactory.getLogger(HardwareALUProxy.class);

    @Override
    public ALUResult operate(boolean s0, boolean s1, boolean cIn, boolean bInv, BitField a, BitField b) {
        BitField input = BitField.combine(BitField.create(s0, s1, cIn, bInv), a, b);
        ALUResult result = sendToHardware(input);
        return result;
    }

    private ALUResult sendToHardware(BitField input) {
        SerialPort sp = getSerialPort();
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

        if (sp.openPort()) {
            logger.info("Port is open :)");
        } else {
            logger.warn("Failed to open port :(");
            return null;
        }

        // send data
        try {
            for (int i = 0; i < input.size(); i++) {
                sp.getOutputStream().write(input.getBit(i));
                sp.getOutputStream().flush();
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (sp.closePort()) {
            logger.info("Port is closed :)");
        } else {
            logger.warn("Failed to close port :(");
            return null;
        }


        return null;
    }

    private SerialPort getSerialPort() {
        return SerialPort.getCommPort("/dev/ttyACM1");
    }

}
