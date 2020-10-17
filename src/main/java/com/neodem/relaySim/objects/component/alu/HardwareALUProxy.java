package com.neodem.relaySim.objects.component.alu;

import com.fazecast.jSerialComm.SerialPort;
import com.neodem.relaySim.data.bitfield.BitField;
import com.neodem.relaySim.data.bitfield.BitFieldBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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
        BitField input = BitFieldBuilder.combineShiftLeft(BitFieldBuilder.create(s0, s1, cIn, bInv), a, b);
        ALUResult result = communeWithHardware(input);
        return result;
    }

    private ALUResult communeWithHardware(BitField input) {
        ALUResult result = null;

        // send the 12 bit signal
        sendToHardware(input);

        // get back the 5 bit result
        BitField received = receiveFromHardware();

        if (received != null) {
            result = new ALUResult(received.getSubField(0, 3), received.getBit(4));
        }

        return result;
    }

    private BitField receiveFromHardware() {
        SerialPort sp = getSerialPort();

        // block until bytes can be written
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        openPort(sp);

        // get data
        byte[] readBuffer = new byte[1];
        try {
            // this should block until it gets its one byte
            int numRead = sp.readBytes(readBuffer, readBuffer.length);
            logger.info("Read " + numRead + " bytes.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        closePort(sp);

        BitField bitField = BitFieldBuilder.createFromBytes(readBuffer);

        return bitField;
    }

    private void sendToHardware(BitField input) {
        SerialPort sp = getSerialPort();

        // block until bytes can be written
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        openPort(sp);

        List<Byte> bytesToWrite = input.getAsBytes();

        // send data
        try {
            for (Byte b : bytesToWrite) {
                sp.getOutputStream().write(b);
                sp.getOutputStream().flush();
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        closePort(sp);
    }

    private void openPort(SerialPort sp) {
        if (sp.openPort()) {
            logger.info("Port is open :)");
        } else {
            logger.warn("Failed to open port :(");
            return;
        }
    }

    private void closePort(SerialPort sp) {
        if (sp.closePort()) {
            logger.info("Port is closed :)");
        } else {
            logger.warn("Failed to close port :(");
            return;
        }
    }

    private SerialPort getSerialPort() {
        SerialPort sp = SerialPort.getCommPort("/dev/ttyACM1");

        // default connection settings for Arduino
        sp.setComPortParameters(9600, 8, 1, 0);
        return sp;
    }

}
