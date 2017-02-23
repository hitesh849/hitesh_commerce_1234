package com.dwacommerce.pos.printers;

import com.aem.api.AEMPrinter;
import com.aem.api.AEMScrybeDevice;
import com.aem.api.IAemScrybe;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.utils.Util;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Admin on 22-02-2017.
 */
public class AemPrinter implements IAemScrybe {
    private static AemPrinter instance;
    private AEMScrybeDevice m_AemScrybeDevice;
    private AEMPrinter m_AemPrinter = null;


    public static AemPrinter getInstance() {
        if (instance == null) {
            instance = new AemPrinter();
        }
        return instance;
    }

    private AemPrinter() {
    }

    public boolean print(String printerName, String text) {
        try {
            if (m_AemPrinter == null) {
                m_AemScrybeDevice = (m_AemScrybeDevice == null) ? new AEMScrybeDevice(this) : m_AemScrybeDevice;
                m_AemScrybeDevice.connectToPrinter(printerName);
                m_AemPrinter = m_AemScrybeDevice.getAemPrinter();
            }

            m_AemPrinter.print(text);
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();
            m_AemPrinter.setCarriageReturn();

        } catch (IOException e) {
            if (e.getMessage().contains("Service discovery failed")) {
                Util.showAlertDialog(null, "Not Connected\n"
                        + Config.getAemPrinterName()
                        + " is unreachable or off otherwise it is connected with other device");
            } else if (e.getMessage().contains("Device or resource busy")) {
                Util.showAlertDialog(null, "the device is already connected");
            } else {
                Util.showAlertDialog(null, "Unable to connect");
            }
            return false;
        }
        return true;
    }

    private void disconnectAemPrinter() {
        try {
            m_AemScrybeDevice.disConnectPrinter();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDiscoveryComplete(ArrayList<String> arrayList) {

    }

    private void disconnectPrinter() {
        try {
            if (m_AemScrybeDevice != null && m_AemPrinter != null) {
                m_AemScrybeDevice.disConnectPrinter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        disconnectPrinter();
    }
}
