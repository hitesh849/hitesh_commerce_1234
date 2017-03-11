package com.dwacommerce.pos.printers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.OrderAdjustmentsData;
import com.dwacommerce.pos.dao.ReceiptData;
import com.dwacommerce.pos.dao.ReceiptItemsData;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.utility.ShowMsg;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import org.byteclues.lib.utils.Util;

/**
 * Created by phoosaram on 09-03-2017.
 */

public class EpsonPrinter implements ReceiveListener {
    private static EpsonPrinter instance;
    private Printer mPrinter;
    private Context context;

    private EpsonPrinter() {
    }

    public static EpsonPrinter getInstance(Context context) {
        if (instance == null) {
            instance = new EpsonPrinter();
        }
        instance.context = context;
        return instance;
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(Config.getPrinterSeriesConstant(), Config.getPrinterLanguageConstant(), instance.context);
        } catch (Exception e) {
            ShowMsg.showException(e, "Printer", context);
            return false;
        }
        mPrinter.setReceiveEventListener(this);
        return true;
    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }
        mPrinter.clearCommandBuffer();
        mPrinter.setReceiveEventListener(null);
        mPrinter = null;
    }

    private boolean createStatementReceiptSequence(String statementReceipt) {
        String method = "Default";
        try {
            if (mPrinter == null) {
                return false;
            }
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addLineFeed";
            mPrinter.addFeedLine(2);
            method = "addText";
            mPrinter.addText(statementReceipt);
            method = "addLinefeed";
            mPrinter.addFeedLine(2);
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            method = "addText";
            mPrinter.addText("Powered by : DWA Commerce");
            method = "addLinefeed";
            mPrinter.addFeedLine(2);
            method = "addCutFeed";
            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Exception e) {
            ShowMsg.showException(e, method, instance.context);
            return false;
        }
        return true;
    }

    public boolean printStatementData(String statementData) {
        if (!initializeObject()) {
            return false;
        }
        if (!createStatementReceiptSequence(statementData)) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean createReceiptData(ReceiptData receiptData) {
        String method = "Default";
        Bitmap logoData = TextUtils.isEmpty(Config.getStoreImageString()) ? BitmapFactory.decodeResource(instance.context.getResources(), R.mipmap.ic_launcher) : Util.getDecode64ImageStringFromBitmap(Config.getStoreImageString(), R.mipmap.ic_launcher);
        StringBuilder textData = new StringBuilder();
        final int barcodeWidth = 2;
        final int barcodeHeight = 100;
        if (mPrinter == null) {
            return false;
        }
        try {
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addImage";
            mPrinter.addImage(logoData, 0, 0, logoData.getWidth(), logoData.getHeight(), Printer.COLOR_1, Printer.MODE_MONO, Printer.HALFTONE_DITHER, Printer.PARAM_DEFAULT, Printer.COMPRESS_AUTO);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            textData.append(Config.getStoreName() + "\n");
            textData.append(Config.getStoreAddress() + "\n");
            textData.append("\n");
            textData.append("Order No - " + receiptData.order_details.orderHeader.orderId + "\n");
            textData.append(receiptData.order_details.orderHeader.orderDate + "\n");
            textData.append("Customer Name - " + receiptData.order_details.customerName + "\n");
            textData.append("Order Details\n");
            for (int i = 0; i < (Config.getPrinterWidth() == 2 ? Constants.TWO_INCH_CHAR : Constants.THREE_INCH_CHAR); i++) {
                textData.append("-");
            }
            textData.append("\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            if (Config.getPrinterWidth() == 2) {
                textData.append("      Item Name        Qty  Amt   \n");
            } else {
                textData.append("            Item Name               Qty   Amt \n");
            }
            for (int i = 0; i < (Config.getPrinterWidth() == 2 ? Constants.TWO_INCH_CHAR : Constants.THREE_INCH_CHAR); i++) {
                textData.append("-");
            }
            textData.append("\n");
            for (ReceiptItemsData receiptItemsData : receiptData.order_details.orderItems) {
                String str = "";
                if (Config.getPrinterWidth() == 2) {
                    if (receiptItemsData.itemDescription.length() > 20) {
                        str = receiptItemsData.itemDescription.substring(0, 20) + "..";
                    } else {
                        str = receiptItemsData.itemDescription;
                        for (int i = 0; i <= 22 - receiptItemsData.itemDescription.length(); i++) {
                            str += " ";
                        }
                    }
                } else {
                    if (receiptItemsData.itemDescription.length() > 33) {
                        str = receiptItemsData.itemDescription.substring(0, 33) + "..";
                    } else {
                        str = receiptItemsData.itemDescription;
                        for (int i = 0; i <= 35 - receiptItemsData.itemDescription.length(); i++) {
                            str += " ";
                        }
                    }

                }

                textData.append(str + "  " + (int) receiptItemsData.quantity + "  " + (((int) receiptItemsData.quantity) * receiptItemsData.unitPrice) + "\n");
            }
            for (int i = 0; i < (Config.getPrinterWidth() == 2 ? Constants.TWO_INCH_CHAR : Constants.THREE_INCH_CHAR); i++) {
                textData.append("-");
            }
            textData.append("\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            method = "addTextSize";
            mPrinter.addTextSize(1, 1);
            method = "addFeedLine";
            for (OrderAdjustmentsData orderAdjustmentsData : receiptData.order_details.orderAdjustments) {
                switch (orderAdjustmentsData.orderAdjustmentTypeId) {
                    case "VAT_TAX": {
                        receiptData.order_details.VAT_TAX += orderAdjustmentsData.amountAlreadyIncluded;
                        break;
                    }
                    case "SALES_TAX": {
                        receiptData.order_details.SALES_TAX += orderAdjustmentsData.amount;
                        break;
                    }
                    case "PROMOTION_ADJUSTMENT": {
                        receiptData.order_details.PROMOTION_AMOUNT += orderAdjustmentsData.amount;
                        break;
                    }
                    case "LOYALITY_POINTS": {
                        receiptData.order_details.LOYALTY_POINTS_AMOUNT += orderAdjustmentsData.amount;
                        break;
                    }
                }
            }
            textData.append("Items SubTotal : " + receiptData.order_details.orderSubTotal + "\n");
            if (receiptData.order_details.VAT_TAX > 0)
                textData.append("       VAT Tax : " + String.format("%.2f", receiptData.order_details.VAT_TAX) + "\n");
            if (receiptData.order_details.SALES_TAX > 0)
                textData.append("     SALES Tax : " + String.format("%.2f", receiptData.order_details.SALES_TAX) + "\n");
            if (receiptData.order_details.PROMOTION_AMOUNT != 0)
                textData.append(" Promotion Amt : " + String.format("%.2f", receiptData.order_details.PROMOTION_AMOUNT) + "\n");
            if (receiptData.order_details.LOYALTY_POINTS_AMOUNT != 0)
                textData.append(" Loyalty Point : " + String.format("%.2f", receiptData.order_details.LOYALTY_POINTS_AMOUNT) + "\n");
            mPrinter.addFeedLine(1);
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            method = "addTextSize";
            mPrinter.addTextSize(2, 2);
            method = "addText";
            mPrinter.addText("TOTAL " + receiptData.order_details.orderHeader.grandTotal + "\n");
            mPrinter.addTextSize(1, 1);
            textData.append("Thanks for shopping with us\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            method = "addFeedLine";
            mPrinter.addFeedLine(2);
            method = "addBarcode";
            mPrinter.addBarcode(receiptData.order_details.orderHeader.orderId, Printer.BARCODE_CODE39, Printer.HRI_BELOW, Printer.FONT_A, barcodeWidth, barcodeHeight);
            method = "addText";
            mPrinter.addFeedLine(1);
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            textData.append("Powered by : DWA Commerce");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Exception e) {
            ShowMsg.showException(e, method, instance.context);
            return false;
        }
        textData = null;
        return true;
    }

    public boolean runPrintReceiptSequence(ReceiptData receiptData) {
        if (!initializeObject()) {
            return false;
        }
        if (!createReceiptData(receiptData)) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += instance.context.getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += instance.context.getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += instance.context.getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += instance.context.getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += instance.context.getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += instance.context.getString(R.string.handlingmsg_err_autocutter);
            msg += instance.context.getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += instance.context.getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += instance.context.getString(R.string.handlingmsg_err_overheat);
                msg += instance.context.getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += instance.context.getString(R.string.handlingmsg_err_overheat);
                msg += instance.context.getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += instance.context.getString(R.string.handlingmsg_err_overheat);
                msg += instance.context.getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += instance.context.getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += instance.context.getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;
        if (mPrinter == null) {
            return false;
        }
        try {
            mPrinter.connect(Config.getPrinterIpAddress(), Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "connect", instance.context);
            return false;
        }
        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", instance.context);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();
        dispPrinterWarnings(status);
        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status), instance.context);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "sendData", instance.context);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }
        return true;
    }

    @Override
    public void onPtrReceive(Printer printer, int i, final PrinterStatusInfo printerStatusInfo, String s) {
        if (instance.context instanceof Activity) {
            ((Activity) instance.context).runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    dispPrinterWarnings(printerStatusInfo);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            disconnectPrinter();
                        }
                    }).start();
                }
            });
        }
    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        ShowMsg.showException(e, "endTransaction", instance.context);
                    }
                });
            }

        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            if (instance.context instanceof Activity) {
                ((Activity) instance.context).runOnUiThread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        ShowMsg.showException(e, "disconnect", instance.context);
                    }
                });
            }

        }

        finalizeObject();
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += instance.context.getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += instance.context.getString(R.string.handlingmsg_warn_battery_near_end);
        }
        Util.showAlertDialog(null, warningsMsg);
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;//print available
        }

        return true;
    }
}
