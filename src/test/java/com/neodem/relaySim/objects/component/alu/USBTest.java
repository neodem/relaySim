package com.neodem.relaySim.objects.component.alu;

import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class USBTest {

    private static Logger logger = LoggerFactory.getLogger(USBTest.class);

    public USBTest() {
        SerialPort s = getSerialPort();
        s.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        openPort(s);

        byte b = 4;
        byte b2 = 5;
//        while(true) {
            try {
                s.getOutputStream().write(b);
                s.getOutputStream().write(b2);
                s.getOutputStream().flush();
            } catch(Exception e) {
                logger.error("issue", e);
            }
//        }
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

    public static void main(String[] args) {
        new USBTest();
    }

    private SerialPort getSerialPort() {
        SerialPort[] commPorts = SerialPort.getCommPorts();


        SerialPort sp = commPorts[2];

        // default connection settings for Arduino
        sp.setComPortParameters(9600, 8, 1, 0);
        return sp;
    }
}
