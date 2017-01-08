package com.dwacommerce.pos.viewControllers;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.AddToCartData;
import com.dwacommerce.pos.dao.CartItemsData;
import com.dwacommerce.pos.dao.CartLineData;
import com.dwacommerce.pos.dao.CartPaymentData;
import com.dwacommerce.pos.dao.CheckOutData;
import com.dwacommerce.pos.dao.ClearCartData;
import com.dwacommerce.pos.dao.CloseTerminalData;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.CustomerData;
import com.dwacommerce.pos.dao.LoyaltyPointsData;
import com.dwacommerce.pos.dao.OpenTerminalData;
import com.dwacommerce.pos.dao.OrderAdjustmentsData;
import com.dwacommerce.pos.dao.PaidInOutData;
import com.dwacommerce.pos.dao.PaidInOutResponse;
import com.dwacommerce.pos.dao.PaymentInfoData;
import com.dwacommerce.pos.dao.ProductAttributesData;
import com.dwacommerce.pos.dao.PromoCodeData;
import com.dwacommerce.pos.dao.ReceiptData;
import com.dwacommerce.pos.dao.ReceiptItemsData;
import com.dwacommerce.pos.dao.SaleData;
import com.dwacommerce.pos.dao.SaveSaleData;
import com.dwacommerce.pos.dao.SignOutData;
import com.dwacommerce.pos.database.DatabaseMgr;
import com.dwacommerce.pos.dialogs.AddNewItem;
import com.dwacommerce.pos.dialogs.DashBordDialogs;
import com.dwacommerce.pos.dialogs.UpdateCartItem;
import com.dwacommerce.pos.model.DashboardModel;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.utility.ShowMsg;
import com.epson.epos2.ConnectionListener;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.barcodescanner.BarcodeScanner;
import com.epson.epos2.barcodescanner.ScanListener;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.ArrayList;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by admin on 11-08-2016.
 */

public class DashBordActivity extends AbstractFragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ReceiveListener, TextView.OnEditorActionListener {
    private DashboardModel dashboardModel = new DashboardModel();
    private int TWO_INCH_CHAR = 35;
    private int THREE_INCH_CHAR = 48;
    private ImageView imgAddToCartDashboard;
    private TextView txtCustomerNameDashboard;
    private TextView txtSubTotalDashboard;
    private TextView txtVatTaxDashboard;
    private TextView txtDiscountDashboard;
    private TextView txtLoyaltyPointsDashboard;
    private TextView txtTotalDueDashboard;
    private TextView txtGrandTotalDashboard;
    private TextView txtCashDashBoard;
    private TextView txtCCDashboard;
    private TextView txtChequeDashboard;
    private TextView txtSalesTaxDashboard;
    private CustomerData customerData;
    private LinearLayout llItemContainerDashboard;
    private String currency;
    private ImageView imgLoyaltyPointsDashboard;
    private ImageView imgScanDashboard;
    private ImageView imgCashDashboard;
    private ImageView imgChequeDashboard;
    private ImageView imgCCDashboard;
    private ImageView imgPromotionDashboard;
    private ImageView imgClearResetDashboard;
    private ImageView imgCompleteDashboard;
    private ImageView imgPrintInvoiceDashboard;
    private ImageView imgOptionsMenuDashboard;
    private ImageView imgRefreshCartDashboard;
    private TableRow tblSalesTaxRow;
    private TableRow tblVatTaxRow;
    private DashBordDialogs dashBordDialogs = new DashBordDialogs(DashBordActivity.this);
    private String orderId;
    private Printer mPrinter;
    private BarcodeScanner mBarCodeScanner;
    private EditText etxtBarcode;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        int contentViewId = Config.getFancyDashboard() ? R.layout.dashbord_activity_with_category : R.layout.dashbord_activity;
        setContentView(contentViewId);
        init();
    }

    private void init() {
        imgAddToCartDashboard = (ImageView) findViewById(R.id.imgAddToCartDashboard);
        txtCustomerNameDashboard = (TextView) findViewById(R.id.txtCustomerNameDashboard);
        txtSubTotalDashboard = (TextView) findViewById(R.id.txtSubTotalDashboard);
        txtVatTaxDashboard = (TextView) findViewById(R.id.txtVatTaxDashboard);
        txtSalesTaxDashboard = (TextView) findViewById(R.id.txtSalesTaxDashboard);
        txtDiscountDashboard = (TextView) findViewById(R.id.txtDiscountDashboard);
        txtLoyaltyPointsDashboard = (TextView) findViewById(R.id.txtLoyaltyPointsDashboard);
        txtGrandTotalDashboard = (TextView) findViewById(R.id.txtGrandTotalDashboard);
        txtTotalDueDashboard = (TextView) findViewById(R.id.txtTotalDueDashboard);
        txtCashDashBoard = (TextView) findViewById(R.id.txtCashDashBoard);
        txtCCDashboard = (TextView) findViewById(R.id.txtCCDashboard);
        txtChequeDashboard = (TextView) findViewById(R.id.txtChequeDashboard);
        llItemContainerDashboard = (LinearLayout) findViewById(R.id.llItemContainerDashboard);
        imgLoyaltyPointsDashboard = (ImageView) findViewById(R.id.imgLoyaltyPointsDashboard);
        imgScanDashboard = (ImageView) findViewById(R.id.imgScanDashboard);
        imgCashDashboard = (ImageView) findViewById(R.id.imgCashDashboard);
        imgChequeDashboard = (ImageView) findViewById(R.id.imgChequeDashboard);
        imgCCDashboard = (ImageView) findViewById(R.id.imgCCDashboard);
        imgPromotionDashboard = (ImageView) findViewById(R.id.imgPromotionDashboard);
        imgClearResetDashboard = (ImageView) findViewById(R.id.imgClearResetDashboard);
        imgCompleteDashboard = (ImageView) findViewById(R.id.imgCompleteDashboard);
        imgPrintInvoiceDashboard = (ImageView) findViewById(R.id.imgPrintInvoiceDashboard);
        imgOptionsMenuDashboard = (ImageView) findViewById(R.id.imgOptionsMenuDashboard);
        imgRefreshCartDashboard = (ImageView) findViewById(R.id.imgRefreshCartDashboard);
        tblSalesTaxRow = (TableRow) findViewById(R.id.tblSalesTaxRow);
        tblVatTaxRow = (TableRow) findViewById(R.id.tblVatTaxRow);
        etxtBarcode = (EditText) findViewById(R.id.etxtBarcode);
        imgOptionsMenuDashboard.setOnClickListener(this);
        imgRefreshCartDashboard.setOnClickListener(this);
        imgPrintInvoiceDashboard.setOnClickListener(this);
        imgCashDashboard.setOnClickListener(this);
        imgClearResetDashboard.setOnClickListener(this);
        imgCompleteDashboard.setOnClickListener(this);
        imgChequeDashboard.setOnClickListener(this);
        imgCCDashboard.setOnClickListener(this);
        imgPromotionDashboard.setOnClickListener(this);
        imgScanDashboard.setOnClickListener(this);
        imgLoyaltyPointsDashboard.setOnClickListener(this);
        imgAddToCartDashboard.setOnClickListener(this);
        txtCustomerNameDashboard.setOnClickListener(this);
        etxtBarcode.setOnEditorActionListener(this);
        if (TextUtils.isEmpty(Config.getCountryList())) {
            dashboardModel.getCountryList();
        }
        if (!Config.getInternalScannerUse())
            etxtBarcode.setVisibility(View.VISIBLE);
        else
            etxtBarcode.setVisibility(View.GONE);
        fetchCartData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        etxtBarcode.requestFocus();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Util.dimissProDialog();
    }

    public void fetchCartData() {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(DashBordActivity.this);
            dashboardModel.showCart(Config.getSessionId(), Config.getPosTerminalId());
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQEST_CODE_FIND_CUSTOMER) {
                Util.showProDialog(DashBordActivity.this);
                dashboardModel.getPartyInfo();
            } else if (requestCode == Constants.REQUEST_CODE_FOR_READ_BARCODE) {
                if (data != null && data.hasExtra(Constants.BARCODE_VALUE)) {
                    String barcode = data.getStringExtra(Constants.BARCODE_VALUE);
                    addNewItemToCartWithBarCode(barcode, "1");
                }
            } else if (requestCode == Constants.REQUEST_CODE_FOR_SETTINGS) {
                try {
                    Intent intent = new Intent(DashBordActivity.this, DashBordActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.imgScanDashboard) {
            if (Config.getInternalScannerUse()) {
                Intent intent = new Intent(DashBordActivity.this, BarcodeActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_FOR_READ_BARCODE);
            } else {
                connectBarcodeScanner();
            }
        } else if (vid == R.id.imgAddToCartDashboard) {
            AddNewItem addNewItem = new AddNewItem();
            addNewItem.show(getSupportFragmentManager(), null);
        } else if (vid == R.id.imgDeleteItemDashboard) {
            CartLineData cartLineData = ((CartLineData) v.getTag());
            if (Util.isDeviceOnline()) {
                Util.showProDialog(DashBordActivity.this);
                dashboardModel.deleteCartItem("" + cartLineData.cartLineIndex);
            } else {
                Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
            }
        } else if (vid == R.id.itemRowDashboard) {
            CartLineData cartLineData = ((CartLineData) v.getTag());
            if (Util.isDeviceOnline()) {
                UpdateCartItem updateCartItem = new UpdateCartItem();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.SERIALIZABLE_DATA, cartLineData);
                updateCartItem.setArguments(bundle);
                updateCartItem.show(getSupportFragmentManager(), null);
            } else {
                Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
            }
        } else if (vid == R.id.txtCustomerNameDashboard) {
            Intent intent = new Intent(DashBordActivity.this, FindCustomerActivity.class);
            startActivityForResult(intent, Constants.REQEST_CODE_FIND_CUSTOMER);
        } else if (vid == R.id.imgLoyaltyPointsDashboard) {
            if (customerData != null) {
                dashBordDialogs.loyaltyPoint(customerData.availableLoyaltyPoints);
            } else {
                Util.showAlertDialog(null, Constants.NO_PARTY_FOUND_ALERT);
            }
        } else if (vid == R.id.imgPromotionDashboard) {
            dashBordDialogs.promotion();
        } else if (vid == R.id.imgCashDashboard) {
            dashBordDialogs.cashPaymentDialog(txtTotalDueDashboard.getText().toString(), currency);
        } else if (vid == R.id.imgChequeDashboard) {
            dashBordDialogs.paymentDialog("PayCheque", "Cheque Payment", txtTotalDueDashboard.getText().toString(), currency);
        } else if (vid == R.id.imgCCDashboard) {
            dashBordDialogs.paymentDialog("PayCC", "Credit Card Payment", txtTotalDueDashboard.getText().toString(), currency);
        } else if (vid == R.id.imgClearResetDashboard) {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(DashBordActivity.this);
                dashboardModel.clearCart();
            } else {
                Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
            }
        } else if (vid == R.id.imgCompleteDashboard) {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(DashBordActivity.this);
                dashboardModel.checkout();
            } else {
                Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
            }
        } else if (vid == R.id.imgOptionsMenuDashboard) {
            showPopup(v);
        } else if (vid == R.id.imgRefreshCartDashboard) {
            fetchCartData();
        } else if (vid == R.id.imgPrintInvoiceDashboard) {
            if (TextUtils.isEmpty(orderId)) {
                Util.showAlertDialog(null, "First checkout and then print");
                return;
            }
            getReceiptData(orderId);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    protected BasicModel getModel() {
        return dashboardModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        if (data instanceof CartItemsData) {
            etxtBarcode.requestFocus();
            setCartData(((CartItemsData) data));
        } else if (data instanceof SignOutData) {
            SignOutData signOutData = ((SignOutData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(signOutData.response)) {
                doLogout();
            } else {
                Util.showAlertDialog(null, Constants.DEFAULT_SERVER_ERROR);
            }
        } else if (data instanceof CustomerData) {
            CustomerData customerData = ((CustomerData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(customerData.response)) {
                setPartyToCart(customerData);
            } else {
                txtCustomerNameDashboard.setText("");
            }
        } else if (data instanceof CartPaymentData) {
            CartPaymentData cartPaymentData = ((CartPaymentData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(cartPaymentData.response)) {
                Util.showCenteredToast(DashBordActivity.this, cartPaymentData.responseMessage);
                fetchCartData();
            }
        } else if (data instanceof PromoCodeData) {
            PromoCodeData promoCodeData = ((PromoCodeData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(promoCodeData.response)) {
                fetchCartData();
            }
            Util.showAlertDialog(null, TextUtils.isEmpty(promoCodeData.responseMessage) ? "Invalid Promo Code" : promoCodeData.responseMessage);
        } else if (data instanceof LoyaltyPointsData) {
            LoyaltyPointsData responseData = ((LoyaltyPointsData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                fetchCartData();
            } else {
                Util.showAlertDialog(null, responseData.responseMessage);
            }
        } else if (data instanceof CheckOutData) {
            CheckOutData responseData = ((CheckOutData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                orderId = responseData.orderId;
                customerData = null;
                fetchCartData();
                if (Config.getPrintWithoutUserConfirmation()) {
                    onClick(imgPrintInvoiceDashboard);
                } else {
                    Util.showCenteredToast(this, responseData.responseMessage);
                }
            } else if (Constants.RESPONSE_ERROR_MSG.equals(responseData.response)) {
                Util.showAlertDialog(null, responseData.responseMessage);
            }
        } else if (data instanceof AddToCartData) {
            AddToCartData responseData = ((AddToCartData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                fetchCartData();
            } else if (Constants.RESPONSE_ERROR_MSG.equals(responseData.response)) {
                Util.showAlertDialog(null, responseData.responseMessage);
            }
        } else if (data instanceof PaidInOutData) {
            PaidInOutData inOutData = ((PaidInOutData) data);
            if ("IN".equals(inOutData.paidType)) {
                dashBordDialogs.paidInDialog(inOutData.reason_list);
            } else {
                dashBordDialogs.paidOutDialog(inOutData.reason_list);
            }
        } else if (data instanceof PaidInOutResponse) {
            PaidInOutResponse response = ((PaidInOutResponse) data);
            Util.showAlertDialog(null, response.responseMessage);
        } else if (data instanceof SaveSaleData) {
            SaveSaleData saveSaleData = ((SaveSaleData) data);
            if (saveSaleData.clearSale) {
                onClick(imgClearResetDashboard);
            } else {
                Util.showAlertDialog(null, saveSaleData.responseMessage);
            }
        } else if (data instanceof SaleData) {
            SaleData saleData = ((SaleData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(saleData.response)) {
                dashBordDialogs.loadSaleDialog(saleData);
            } else {
                Util.showAlertDialog(null, saleData.responseMessage);
            }
        } else if (data instanceof OpenTerminalData) {
            Util.showAlertDialog(null, ((OpenTerminalData) data).responseMessage);
        } else if (data instanceof CloseTerminalData) {
            Util.showAlertDialog(null, ((CloseTerminalData) data).responseMessage);
        } else if (data instanceof ReceiptData) {
            ReceiptData receiptData = ((ReceiptData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(receiptData.response)) {
                printReceipt(receiptData);
            } else {
                Util.showAlertDialog(null, "Order details not found!");
            }
        } else if (data instanceof ClearCartData) {
            ClearCartData responseData = ((ClearCartData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                customerData = null;
                fetchCartData();
            } else if (Constants.RESPONSE_ERROR_MSG.equals(responseData.response)) {
                Util.showAlertDialog(null, responseData.responseMessage);
            }
        } else if (data instanceof CommonResponseData) {
            CommonResponseData responseData = ((CommonResponseData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                fetchCartData();
            } else if (Constants.RESPONSE_ERROR_MSG.equals(responseData.response)) {
                Util.showAlertDialog(null, responseData.responseMessage);
            }
        } else if (data instanceof RetrofitError) {
            Util.showAlertDialog(null, Constants.DEFAULT_SERVER_ERROR);
        }

    }

    private void printReceipt(ReceiptData receiptData) {
        String receiptText = getReceiptText(receiptData);
        updateButtonState(false);
        if (!runPrintReceiptSequence(receiptData)) {
            updateButtonState(true);
        }
        if (Config.getReceiptSharing()) {
            openWhatsappContact(receiptText);
        }
    }

    private void openWhatsappContact(String text) {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(sendIntent, "Share with.."));
        } catch (PackageManager.NameNotFoundException e) {
            Util.showCenteredToast(DashBordActivity.this, "WhatsApp not Installed");
        }

    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }
        mPrinter.clearCommandBuffer();
        mPrinter.setReceiveEventListener(null);
        mPrinter = null;
    }

    private boolean runPrintReceiptSequence(ReceiptData receiptData) {
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

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;
        if (mPrinter == null) {
            return false;
        }
        try {
            mPrinter.connect(Config.getPrinterIpAddress(), Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "connect", DashBordActivity.this);
            return false;
        }
        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", DashBordActivity.this);
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
            ShowMsg.showMsg(makeErrorMessage(status), DashBordActivity.this);
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
            ShowMsg.showException(e, "sendData", DashBordActivity.this);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    private String getReceiptText(ReceiptData receiptData) {
        StringBuilder sharedMessageText = new StringBuilder();
        try {
            sharedMessageText.append(Config.getStoreName() + "\n");
            sharedMessageText.append(Config.getStoreAddress() + "\n");
            sharedMessageText.append("\n");
            sharedMessageText.append("Order No - " + receiptData.order_details.orderHeader.orderId + "\n");
            sharedMessageText.append(receiptData.order_details.orderHeader.orderDate + "\n");
            sharedMessageText.append("Customer Name - " + receiptData.order_details.customerName + "\n");
            sharedMessageText.append("Order Details\n");
            for (int i = 0; i < (Config.getPrinterWidth() == 2 ? TWO_INCH_CHAR : THREE_INCH_CHAR); i++) {
                sharedMessageText.append("-");
            }
            sharedMessageText.append("\n");
            sharedMessageText.append("      Item Name        Qty  Amt   \n");
            for (int i = 0; i < TWO_INCH_CHAR; i++) {
                sharedMessageText.append("-");
            }
            sharedMessageText.append("\n");
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

                sharedMessageText.append(str + "  " + (int) receiptItemsData.quantity + "  " + (((int) receiptItemsData.quantity) * receiptItemsData.unitPrice) + "\n");
            }
            for (int i = 0; i < TWO_INCH_CHAR; i++) {
                sharedMessageText.append("-");
            }
            sharedMessageText.append("\n");
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
            sharedMessageText.append("Items SubTotal : " + receiptData.order_details.orderSubTotal + "\n");
            if (receiptData.order_details.VAT_TAX > 0)
                sharedMessageText.append("       VAT Tax : " + String.format("%.2f", receiptData.order_details.VAT_TAX) + "\n");
            if (receiptData.order_details.SALES_TAX > 0)
                sharedMessageText.append("     SALES Tax : " + String.format("%.2f", receiptData.order_details.SALES_TAX) + "\n");
            if (receiptData.order_details.PROMOTION_AMOUNT != 0)
                sharedMessageText.append(" Promotion Amt : " + String.format("%.2f", receiptData.order_details.PROMOTION_AMOUNT) + "\n");
            if (receiptData.order_details.LOYALTY_POINTS_AMOUNT != 0)
                sharedMessageText.append(" Loyalty Point : " + String.format("%.2f", receiptData.order_details.LOYALTY_POINTS_AMOUNT) + "\n");
            sharedMessageText.append("TOTAL " + receiptData.order_details.orderHeader.grandTotal + "\n");
            sharedMessageText.append("Thanks for shopping with us\n");
            sharedMessageText.append("Powered by : DWA Commerce");
            return sharedMessageText.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean createReceiptData(ReceiptData receiptData) {
        StringBuilder sharedMessageText = new StringBuilder();
        String method = "Default";
        Bitmap logoData = TextUtils.isEmpty(Config.getStoreImageString()) ? BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher) : Util.getDecode64ImageStringFromBitmap(Config.getStoreImageString(), R.mipmap.ic_launcher);
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
            for (int i = 0; i < (Config.getPrinterWidth() == 2 ? TWO_INCH_CHAR : THREE_INCH_CHAR); i++) {
                textData.append("-");
            }
            textData.append("\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            sharedMessageText.append(textData.toString());
            textData.delete(0, textData.length());
            if (Config.getPrinterWidth() == 2) {
                textData.append("      Item Name        Qty  Amt   \n");
            } else {
                textData.append("            Item Name               Qty   Amt \n");
            }
            for (int i = 0; i < (Config.getPrinterWidth() == 2 ? TWO_INCH_CHAR : THREE_INCH_CHAR); i++) {
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
            for (int i = 0; i < (Config.getPrinterWidth() == 2 ? TWO_INCH_CHAR : THREE_INCH_CHAR); i++) {
                textData.append("-");
            }
            textData.append("\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            sharedMessageText.append(textData.toString());
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
            sharedMessageText.append(textData.toString());
            textData.delete(0, textData.length());
            method = "addTextSize";
            mPrinter.addTextSize(2, 2);
            method = "addText";
            mPrinter.addText("TOTAL " + receiptData.order_details.orderHeader.grandTotal + "\n");
            mPrinter.addTextSize(1, 1);
            textData.append("Thanks for shopping with us\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            sharedMessageText.append(textData.toString());
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
            sharedMessageText.append(textData.toString());
            textData.delete(0, textData.length());
            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Exception e) {
            ShowMsg.showException(e, method, DashBordActivity.this);
            return false;
        }
        textData = null;
        return true;
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(Config.getPrinterSeriesConstant(), Config.getPrinterLanguageConstant(), this);
        } catch (Exception e) {
            ShowMsg.showException(e, "Printer", this);
            return false;
        }
        mPrinter.setReceiveEventListener(this);
        return true;
    }

    private void updateButtonState(boolean state) {
        imgPrintInvoiceDashboard.setEnabled(state);
    }

    private void setPartyToCart(CustomerData customerData) {
        try {
            if (!TextUtils.isEmpty(customerData.partyInfo.partyName)) {
                this.customerData = customerData.partyInfo;
                txtCustomerNameDashboard.setText(this.customerData.partyName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doSignOut() {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(DashBordActivity.this);
            dashboardModel.doLogout();
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void doLogout() {
        try {
            DatabaseMgr.getInstance(Env.currentActivity).clearDB();
            Config.removeKey(Constants.KEY_SESSION_ID);
            Config.removeKey(Constants.KEY_POS_TERMINAL_ID);

            goToLoginScreen();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Config.removeKey(Constants.KEY_SESSION_ID);
            Config.removeKey(Constants.KEY_POS_TERMINAL_ID);
        }
    }

    private void setCartData(CartItemsData cartItemsData) {
        try {
            if (Constants.RESPONSE_SUCCESS_MSG.equals(cartItemsData.response)) {
                currency = TextUtils.isEmpty(cartItemsData.shoppingCart.currencyUom) ? "INR" : cartItemsData.shoppingCart.currencyUom;
                if (cartItemsData.shoppingCart.cartLines != null) {
                    llItemContainerDashboard.removeAllViews();
                    for (int i = 0; i < cartItemsData.shoppingCart.cartLines.size(); i++) {
                        CartLineData cartLineData = cartItemsData.shoppingCart.cartLines.get(i);
                        TableRow tableRow = getItemRow(cartLineData, i);
                        if (tableRow != null) {
                            llItemContainerDashboard.addView(tableRow);
                        }
                    }
                    setAmountData(cartItemsData);
                }
                if (customerData == null) {
                    onActivityResult(Constants.REQEST_CODE_FIND_CUSTOMER, RESULT_OK, null);
                }
            } else {
                Util.showCenteredToast(DashBordActivity.this, "Session Expired");
                doLogout();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAmountData(CartItemsData cartItemsData) {
        try {
            txtSubTotalDashboard.setText("" + cartItemsData.totalItemSubTotal);
            txtLoyaltyPointsDashboard.setText("" + cartItemsData.loyaltyPointsRedeemed);
            txtGrandTotalDashboard.setText("" + cartItemsData.posTotal);
            txtTotalDueDashboard.setText("" + cartItemsData.totalDue);
            txtDiscountDashboard.setText("" + String.format("%.2f", cartItemsData.promotionAmount));
            txtCashDashBoard.setText("");
            txtCCDashboard.setText("");
            txtChequeDashboard.setText("");
            if (cartItemsData.totalVatTaxIncluded > 0) {
                txtVatTaxDashboard.setText("" + String.format("%.2f", cartItemsData.totalVatTaxIncluded));
                tblVatTaxRow.setVisibility(View.VISIBLE);
            } else
                tblVatTaxRow.setVisibility(View.GONE);

            if (cartItemsData.totalSalesTax > 0) {
                txtSalesTaxDashboard.setText("" + String.format("%.2f", cartItemsData.totalSalesTax));
                tblSalesTaxRow.setVisibility(View.VISIBLE);
            } else
                tblSalesTaxRow.setVisibility(View.GONE);

            for (PaymentInfoData paymentInfo : cartItemsData.shoppingCart.paymentInfo) {
                switch (paymentInfo.paymentMethodTypeId) {
                    case "CASH":
                        txtCashDashBoard.setText(paymentInfo.amount + "");
                        break;
                    case "CREDIT_CARD":
                        txtCCDashboard.setText(paymentInfo.amount + "");
                        break;
                    case "PERSONAL_CHECK":
                        txtChequeDashboard.setText(paymentInfo.amount + "");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TableRow getItemRow(CartLineData cartLineData, int cartLineIndex) {
        try {
            TableRow tableRow = ((TableRow) getLayoutInflater().inflate(R.layout.cart_item_row, null));
            TextView txtProductNameDashboard = (TextView) tableRow.findViewById(R.id.txtProductNameDashboard);
            TextView txtQuantityDahsboard = (TextView) tableRow.findViewById(R.id.txtQuantityDahsboard);
            TextView txtRateDashboard = (TextView) tableRow.findViewById(R.id.txtRateDashboard);
            TextView txtAmountDashboard = (TextView) tableRow.findViewById(R.id.txtAmountDashboard);
            ImageView imgDeleteItemDashboard = (ImageView) tableRow.findViewById(R.id.imgDeleteItemDashboard);
            TableRow itemRowDashboard = (TableRow) tableRow.findViewById(R.id.itemRowDashboard);
            txtProductNameDashboard.setText(cartLineData.productName);
            txtQuantityDahsboard.setText("" + cartLineData.quantity);
            txtRateDashboard.setText("" + cartLineData.basePrice);
            String amount = "" + (cartLineData.basePrice * cartLineData.quantity);
            txtAmountDashboard.setText(amount);
            cartLineData.cartLineIndex = cartLineIndex;
            imgDeleteItemDashboard.setTag(cartLineData);
            itemRowDashboard.setTag(cartLineData);
            imgDeleteItemDashboard.setOnClickListener(this);
            itemRowDashboard.setOnClickListener(this);
            return tableRow;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cashPayment(String amountToBePaid) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(DashBordActivity.this);
            dashboardModel.cashPayment("PayCash", amountToBePaid);
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void cartPayment(String paymentMode, String refNo, String amountToBePaid) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(DashBordActivity.this);
            dashboardModel.cartPayment(paymentMode, refNo, amountToBePaid);
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void redeemLoyaltyPoints(String loyaltyPoints) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(DashBordActivity.this);
            dashboardModel.addLoyaltyPoints(loyaltyPoints);
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void applyPromotion(String promoCode) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(DashBordActivity.this);
            dashboardModel.applyPromoCode(promoCode);
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuSignOutDashboard) {
            dashBordDialogs.logoutConfirmation();
        } else if (itemId == R.id.settingsMenuDashboard) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_FOR_SETTINGS);
        } else if (itemId == R.id.menuPaidInDashboard) {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                dashboardModel.getPaidInOutReasons("IN");
            } else {
                Util.showCenteredToast(this, Constants.INTERNET_ERROR_MSG);
            }
        } else if (itemId == R.id.menuPaidOutDashboard) {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                dashboardModel.getPaidInOutReasons("OUT");
            } else {
                Util.showCenteredToast(this, Constants.INTERNET_ERROR_MSG);
            }
        } else if (itemId == R.id.menuSaveSaleDashboard) {
            dashBordDialogs.saveSaleDialog();
        } else if (itemId == R.id.menuLoadSaleDashboard) {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(this);
                dashboardModel.fetchSavedSaleList();
            } else {
                Util.showCenteredToast(Env.currentActivity, Constants.INTERNET_ERROR_MSG);
            }
        } else if (itemId == R.id.menuOpenTerminalDashboard) {
            dashBordDialogs.openTerminal();
        } else if (itemId == R.id.menuCloseTerminalDashboard) {
            dashBordDialogs.closeTerminal();
        }
        return false;
    }

    private void getReceiptData(String orderId) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(DashBordActivity.this);
            dashboardModel.getOrderDetails(orderId);
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void openTerminal(String startingDrawerAmount) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            dashboardModel.openTerminal(startingDrawerAmount);
        } else {
            Util.showCenteredToast(Env.currentActivity, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void closeTerminal(String cashAmount, String chequeAmount, String CCAmount, String GCAmount, String otherAmount) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            dashboardModel.closeTerminal(cashAmount, chequeAmount, CCAmount, GCAmount, otherAmount);
        } else {
            Util.showCenteredToast(Env.currentActivity, Constants.INTERNET_ERROR_MSG);
        }
    }

    private void goToLoginScreen() {
        try {
            Intent intent = new Intent(DashBordActivity.this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProductVariantDialog(ProductAttributesData productAttributesData) {
        dashBordDialogs.productVariantDialog(productAttributesData);
    }

    public void addNewItemToCart(String productId, String quantity) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(Env.currentActivity);
            dashboardModel.addItemToCart(productId, quantity);
        } else {
            Util.showCenteredToast(Env.currentActivity, Constants.INTERNET_ERROR_MSG);
        }
    }

    public synchronized void addNewItemToCartWithBarCode(String barcode, String quantity) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(Env.currentActivity);
            dashboardModel.addItemToCartWithBarcode(barcode, quantity);
        } else {
            Util.showCenteredToast(Env.currentActivity, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void paidOutAndIn(String amountInOut, String reasonCommentInOut, String reasonEnumId, String paidType) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            dashboardModel.paidOutAndIn(amountInOut, reasonCommentInOut, reasonEnumId, paidType);
        } else {
            Util.showCenteredToast(this, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void saveSale(String shoppingListName, boolean clearSale) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            dashboardModel.saveSale(shoppingListName, clearSale);
        } else {
            Util.showCenteredToast(this, Constants.INTERNET_ERROR_MSG);
        }
    }

    public void loadSale(String shoppingListid, String action) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(this);
            dashboardModel.loadSaleFromList(shoppingListid, action);
        } else {
            Util.showCenteredToast(this, Constants.INTERNET_ERROR_MSG);
        }
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                //ShowMsg.showResult(code, makeErrorMessage(status), DashBordActivity.this);
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
                    ShowMsg.showException(e, "endTransaction", DashBordActivity.this);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "disconnect", DashBordActivity.this);
                }
            });
        }

        finalizeObject();
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
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
        Util.showAlertDialog(null, warningsMsg);
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

    private void connectBarcodeScanner() {
        try {
            Util.showCenteredToast(this, "Connecting scanner");
            if (!initializeScanner()) {
                return;
            }

            if (!connectScanner()) {
                return;
            }
            Util.showCenteredToast(this, "scanner connected");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean connectScanner() {
        if (mBarCodeScanner == null) {
            return false;
        }
        try {
            mBarCodeScanner.connect(getString(R.string.default_scanner_target), BarcodeScanner.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "connect", DashBordActivity.this);

            destroyScanner();

            return false;
        }
        return true;
    }

    private void destroyScanner() {
        if (mBarCodeScanner == null) {
            return;
        }

        mBarCodeScanner.setScanEventListener(null);
        mBarCodeScanner.setConnectionEventListener(null);
        mBarCodeScanner = null;
    }

    private boolean initializeScanner() {
        if (mBarCodeScanner != null) {
            destroyScanner();
        }
        try {
            mBarCodeScanner = new BarcodeScanner(DashBordActivity.this);
        } catch (Exception e) {
            ShowMsg.showException(e, "BarcodeScanner", DashBordActivity.this);

            return false;
        }
        mBarCodeScanner.setScanEventListener(mScanEvent);
        mBarCodeScanner.setConnectionEventListener(mConnectionChangedEvent);

        return true;
    }

    private ConnectionListener mConnectionChangedEvent = new ConnectionListener() {
        @Override
        public void onConnection(Object deviceObj, int eventType) {
            if (eventType == EVENT_DISCONNECT) {
                if (!Config.getInternalScannerUse()) {
                    Util.showAlertDialog(null, "Barcode scanner disconnected please try again");
                }
            } else

            {
                //Do each process.
            }
        }
    };
    private ScanListener mScanEvent = new ScanListener() {
        @Override
        public void onScanData(BarcodeScanner scannerObj, final String input) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    addNewItemToCartWithBarCode(input, "1");
                }
            });
        }
    };

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        try {
            String barcode = etxtBarcode.getText().toString().trim();
            if (!TextUtils.isEmpty(barcode)) {
                etxtBarcode.setText("");
                addNewItemToCartWithBarCode(barcode, "1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
