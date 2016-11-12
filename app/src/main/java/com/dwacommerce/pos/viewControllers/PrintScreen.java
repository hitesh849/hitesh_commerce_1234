package com.dwacommerce.pos.viewControllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.OrderAdjustmentsData;
import com.dwacommerce.pos.dao.ReceiptData;
import com.dwacommerce.pos.dao.ReceiptItemsData;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.utility.ShowMsg;
import com.dwacommerce.pos.utility.SpnModelsItem;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

/**
 * Created by Admin on 11-09-2016.
 */
public class PrintScreen extends Activity implements View.OnClickListener, ReceiveListener {
    private Context mContext = null;
    private EditText mEditTarget = null;
    private Spinner mSpnSeries = null;
    private Spinner mSpnLang = null;
    private Printer mPrinter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer_screen);
        mContext = this;
        int[] target = {R.id.btnDiscovery, R.id.btnSampleReceipt};
        for (int i = 0; i < target.length; i++) {
            Button button = (Button) findViewById(target[i]);
            button.setOnClickListener(this);
        }
        mSpnSeries = (Spinner) findViewById(R.id.spnModel);
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(this, android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_m10), Printer.TM_M10));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_m30), Printer.TM_M30));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p20), Printer.TM_P20));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p60), Printer.TM_P60));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p60ii), Printer.TM_P60II));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p80), Printer.TM_P80));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t20), Printer.TM_T20));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t60), Printer.TM_T60));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t70), Printer.TM_T70));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t81), Printer.TM_T81));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t82), Printer.TM_T82));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t83), Printer.TM_T83));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t88), Printer.TM_T88));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t90), Printer.TM_T90));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t90kp), Printer.TM_T90KP));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_u220), Printer.TM_U220));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_u330), Printer.TM_U330));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_l90), Printer.TM_L90));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_h6000), Printer.TM_H6000));
        mSpnSeries.setAdapter(seriesAdapter);
        mSpnSeries.setSelection(0);
        mSpnLang = (Spinner) findViewById(R.id.spnLang);
        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(this, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_ank), Printer.MODEL_ANK));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_japanese), Printer.MODEL_JAPANESE));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_chinese), Printer.MODEL_CHINESE));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_taiwan), Printer.MODEL_TAIWAN));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_korean), Printer.MODEL_KOREAN));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_thai), Printer.MODEL_THAI));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_southasia), Printer.MODEL_SOUTHASIA));
        mSpnLang.setAdapter(langAdapter);
        mSpnLang.setSelection(0);
        mEditTarget = (EditText) findViewById(R.id.edtTarget);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            String target = data.getStringExtra(getString(R.string.title_target));
            if (target != null) {
                EditText mEdtTarget = (EditText) findViewById(R.id.edtTarget);
                mEdtTarget.setText(target);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnDiscovery:
                intent = new Intent(this, DiscoverActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.btnSampleReceipt:
                updateButtonState(false);
                if (!runPrintReceiptSequence()) {
                    updateButtonState(true);
                }
                break;
            default:
                // Do nothing
                break;
        }
    }

    private boolean runPrintReceiptSequence() {
        if (!initializeObject()) {
            return false;
        }

        if (!createReceiptData()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean createReceiptData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SERIALIZABLE_DATA)) {
            ReceiptData receiptData = ((ReceiptData) intent.getSerializableExtra(Constants.SERIALIZABLE_DATA));
            String method = "";
            Bitmap logoData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
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
                textData.append("The Store - " + Config.getStoreName() + "\n");
                textData.append("Store Address – " + Config.getStoreAddress() + "\n");
                textData.append("\n");
                textData.append(receiptData.order_details.orderHeader.orderDate + "\n");
                textData.append("Order No - " + receiptData.order_details.orderHeader.orderId + "\n");
                textData.append("Customer Name - " + receiptData.order_details.customerName + "\n");
                textData.append("Order Details\n");
                textData.append("----------------------------------\n");
                method = "addText";
                mPrinter.addText(textData.toString());
                textData.delete(0, textData.length());
                textData.append("Item Name   Qty   Amt  List Price\n");
                for (ReceiptItemsData receiptItemsData : receiptData.order_details.orderItems) {
                    textData.append(receiptItemsData.itemDescription + "  " + (int) receiptItemsData.quantity + "  " + receiptItemsData.unitPrice + "  " + receiptItemsData.unitListPrice + "\n");
                }
                textData.append("---------------------------------\n");
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

                textData.append("Items SubTotal :" + receiptData.order_details.orderSubTotal + "\n");
                if (receiptData.order_details.VAT_TAX > 0)
                    textData.append("      VAT Tax  :" + receiptData.order_details.VAT_TAX + "\n");
                if (receiptData.order_details.SALES_TAX > 0)
                    textData.append("     SALES Tax :" + receiptData.order_details.SALES_TAX + "\n");
                if (receiptData.order_details.PROMOTION_AMOUNT > 0)
                    textData.append(" Promotion Amt :" + receiptData.order_details.PROMOTION_AMOUNT + "\n");
                if (receiptData.order_details.LOYALTY_POINTS_AMOUNT > 0)
                    textData.append(" Loyalty Point :" + receiptData.order_details.LOYALTY_POINTS_AMOUNT + "\n");
                mPrinter.addFeedLine(1);
                method = "addText";
                mPrinter.addText(textData.toString());
                textData.delete(0, textData.length());
                method = "addTextSize";
                mPrinter.addTextSize(2, 2);
                method = "addText";
                mPrinter.addText("TOTAL  " + receiptData.orderHeader.grandTotal + "\n");
                mPrinter.addTextSize(1, 1);
                textData.append("Thanks for shopping with us\n");
                textData.append("Powered by: DWA Commerce");
                method = "addText";
                mPrinter.addText(textData.toString());
                textData.delete(0, textData.length());
                method = "addFeedLine";
                mPrinter.addFeedLine(2);
                method = "addBarcode";
                mPrinter.addBarcode(receiptData.order_details.orderHeader.orderId, Printer.BARCODE_CODE39, Printer.HRI_BELOW, Printer.FONT_A, barcodeWidth, barcodeHeight);
                method = "addCut";
                mPrinter.addCut(Printer.CUT_FEED);
            } catch (Exception e) {
                ShowMsg.showException(e, method, mContext);
                return false;
            }
            textData = null;
            return true;
        } else {
            return false;
        }
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
            ShowMsg.showMsg(makeErrorMessage(status), mContext);
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
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(((SpnModelsItem) mSpnSeries.getSelectedItem()).getModelConstant(), ((SpnModelsItem) mSpnLang.getSelectedItem()).getModelConstant(), mContext);
        } catch (Exception e) {
            ShowMsg.showException(e, "Printer", mContext);
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

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;
        if (mPrinter == null) {
            return false;
        }
        try {
            mPrinter.connect(mEditTarget.getText().toString(), Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }
        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", mContext);
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

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "disconnect", mContext);
                }
            });
        }

        finalizeObject();
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

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        EditText edtWarnings = (EditText) findViewById(R.id.edtWarnings);
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
        }

        edtWarnings.setText(warningsMsg);
    }

    private void updateButtonState(boolean state) {
        Button btnReceipt = (Button) findViewById(R.id.btnSampleReceipt);
        btnReceipt.setEnabled(state);
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                dispPrinterWarnings(status);

                updateButtonState(true);

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
