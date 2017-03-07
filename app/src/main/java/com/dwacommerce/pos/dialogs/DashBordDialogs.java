package com.dwacommerce.pos.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.CustomerData;
import com.dwacommerce.pos.dao.PaidInOutData;
import com.dwacommerce.pos.dao.ProductAttributesData;
import com.dwacommerce.pos.dao.SaleData;
import com.dwacommerce.pos.viewControllers.DashBordActivity;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by phoosaram on 26-08-2016.
 */

public class DashBordDialogs {
    private Context context;
    private String productId;
    private String paidReasonEnumId;
    private View lastSelectedView;

    public DashBordDialogs(Context context) {
        this.context = context;
    }

    public void cashPaymentDialog(String grandTotalAmount, String currency) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.payment, null);
        dialogBuilder.setView(dialogView);
        TextView txtCashAmountPayment = (TextView) dialogView.findViewById(R.id.txtCashAmountPayment);
        txtCashAmountPayment.setText(grandTotalAmount + " " + currency);
        TextView txtCancelPayment = (TextView) dialogView.findViewById(R.id.txtCancelPayment);
        TextView txtSavePayment = (TextView) dialogView.findViewById(R.id.txtSavePayment);
        final EditText etxtCashPaymentDialog = (EditText) dialogView.findViewById(R.id.etxtCashPaymentDialog);
        etxtCashPaymentDialog.setText(grandTotalAmount);
        final AlertDialog alertDialog = dialogBuilder.create();
        txtSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etxtCashPaymentDialog.getText().toString())) {
                    etxtCashPaymentDialog.setError("Can't be empty");
                    return;
                }
                ((DashBordActivity) context).cashPayment(etxtCashPaymentDialog.getText().toString().trim());
                Util.hideKeyboard(etxtCashPaymentDialog);
                alertDialog.dismiss();
            }
        });
        txtCancelPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(v);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void paymentDialog(final String paymentMode, String headerText, String grandTotalAmount, String currency) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.creditcard_payment, null);
        dialogBuilder.setView(dialogView);
        TextView txtAmountCreditCard = (TextView) dialogView.findViewById(R.id.txtAmountCreditCard);
        TextView txtHeaderPayment = (TextView) dialogView.findViewById(R.id.txtHeaderPayment);
        txtHeaderPayment.setText(headerText);
        txtAmountCreditCard.setText(grandTotalAmount + " " + currency);
        TextView txtCancelCreditCard = (TextView) dialogView.findViewById(R.id.txtCancelCreditCard);
        TextView txtConfirmCreditCard = (TextView) dialogView.findViewById(R.id.txtConfirmCreditCard);
        final EditText etxtAmountCreditCard = (EditText) dialogView.findViewById(R.id.etxtAmountCreditCard);
        etxtAmountCreditCard.setText(grandTotalAmount);
        final AlertDialog alertDialog = dialogBuilder.create();
        txtConfirmCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etxtRefNoCreditCard = (EditText) dialogView.findViewById(R.id.etxtRefNoCreditCard);
                if (TextUtils.isEmpty(etxtAmountCreditCard.getText().toString())) {
                    etxtAmountCreditCard.setError("Can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(etxtRefNoCreditCard.getText().toString())) {
                    etxtRefNoCreditCard.setError("Can't be empty");
                    return;
                }
                ((DashBordActivity) context).cartPayment(paymentMode, etxtRefNoCreditCard.getText().toString().trim(), etxtAmountCreditCard.getText().toString().trim());
                Util.hideKeyboard(etxtRefNoCreditCard);
                alertDialog.dismiss();
            }
        });
        txtCancelCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(v);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void paymentWithAllCategoriesDialog(String headerText, final String grandTotalAmount, String currency, final CustomerData customerData) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.debit_credit_card_payment, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        TextView txtConfirmCatPayment = (TextView) dialogView.findViewById(R.id.txtConfirmCatPayment);
        TextView txtCancelCreditCard = (TextView) dialogView.findViewById(R.id.txtCancelCreditCard);
        TextView txtAmountCreditCard = (TextView) dialogView.findViewById(R.id.txtAmountCreditCard);
        final LinearLayout llCreditMain = (LinearLayout) dialogView.findViewById(R.id.llCreditMain);
        final LinearLayout llAllPaymentMain = (LinearLayout) dialogView.findViewById(R.id.llAllPaymentMain);
        final CheckBox chkByCash = (CheckBox) dialogView.findViewById(R.id.chkByCash);
        final CheckBox chkByDebitCredit = (CheckBox) dialogView.findViewById(R.id.chkByDebitCredit);
        final CheckBox chkByCheque = (CheckBox) dialogView.findViewById(R.id.chkByCheque);
        final CheckBox chkByCredit = (CheckBox) dialogView.findViewById(R.id.chkByCredit);

        final EditText etxtAmountCash = (EditText) dialogView.findViewById(R.id.etxtAmountCash);
        setEditTextMode(etxtAmountCash, false);
        final EditText etxtDebitCreditCardAmount = (EditText) dialogView.findViewById(R.id.etxtDebitCreditCardAmount);
        setEditTextMode(etxtDebitCreditCardAmount, false);
        final EditText etxtByDebitCreditRefNo = (EditText) dialogView.findViewById(R.id.etxtByDebitCreditRefNo);
        setEditTextMode(etxtByDebitCreditRefNo, false);
        final EditText etxtBankName = (EditText) dialogView.findViewById(R.id.etxtBankName);
        setEditTextMode(etxtBankName, false);
        final EditText etxtBankAmount = (EditText) dialogView.findViewById(R.id.etxtBankAmount);
        setEditTextMode(etxtBankAmount, false);
        final EditText etxtBankRefNo = (EditText) dialogView.findViewById(R.id.etxtBankRefNo);
        setEditTextMode(etxtBankRefNo, false);
        final EditText etxtCreditAmount = (EditText) dialogView.findViewById(R.id.etxtCreditAmount);
        setEditTextMode(etxtCreditAmount, false);
        final TextView txtAvlCreditAmount = (TextView) dialogView.findViewById(R.id.txtAvlCreditAmount);
        final TextView txtPaymentError = (TextView) dialogView.findViewById(R.id.txtPaymentError);
        final CheckBox chkbxCreditAmount = ((CheckBox) dialogView.findViewById(R.id.chkbxCreditAmount));
        ;
        final LinearLayout llBankAccountContainer = ((LinearLayout) dialogView.findViewById(R.id.llBankAccountContainer));
        txtAmountCreditCard.setText(grandTotalAmount);

        if (customerData != null && customerData.billingAccountInfo != null) {
            chkbxCreditAmount.setVisibility(View.VISIBLE);
            txtAvlCreditAmount.setText("Available Credit amount: " + customerData.billingAccountInfo.accountBalance);
        } else {
            chkbxCreditAmount.setVisibility(View.GONE);
        }
        chkbxCreditAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llCreditMain.setVisibility(View.VISIBLE);
                    llBankAccountContainer.setVisibility(View.GONE);
                } else {
                    llCreditMain.setVisibility(View.GONE);
                    llBankAccountContainer.setVisibility(View.VISIBLE);
                }
            }
        });
        txtCancelCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(dialogView.findFocus());
                alertDialog.dismiss();
            }
        });

        chkByCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPaymentError.setVisibility(View.GONE);
                setEditTextMode(etxtAmountCash, chkByCash.isChecked());
                if (!chkByCash.isChecked()) {
                    etxtAmountCash.setError(null);
                    etxtAmountCash.setText(null);
                } else {
                    etxtAmountCash.setText(grandTotalAmount);
                }
            }
        });
        chkByDebitCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPaymentError.setVisibility(View.GONE);
                setEditTextMode(etxtDebitCreditCardAmount, chkByDebitCredit.isChecked());
                setEditTextMode(etxtByDebitCreditRefNo, chkByDebitCredit.isChecked());
                if (!chkByDebitCredit.isChecked()) {
                    etxtDebitCreditCardAmount.setError(null);
                    etxtByDebitCreditRefNo.setError(null);
                }
            }
        });
        chkByCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPaymentError.setVisibility(View.GONE);
                setEditTextMode(etxtBankAmount, chkByCheque.isChecked());
                setEditTextMode(etxtBankRefNo, chkByCheque.isChecked());
                if (!chkByCheque.isChecked()) {
                    etxtBankAmount.setError(null);
                    etxtBankRefNo.setError(null);

                }
            }
        });

        chkByCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPaymentError.setVisibility(View.GONE);
                setEditTextMode(etxtCreditAmount, chkByCredit.isChecked());
                if (!chkByCredit.isChecked()) {
                    etxtCreditAmount.setError(null);
                }
            }
        });


        txtConfirmCatPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cashAmt = etxtAmountCash.getText().toString();
                String ccAmt = etxtDebitCreditCardAmount.getText().toString();
                String ccRefNum = etxtByDebitCreditRefNo.getText().toString();
                String bankName = etxtBankName.getText().toString();
                String checkAmt = etxtBankAmount.getText().toString();
                String checkRefNum = etxtBankRefNo.getText().toString();
                String billAccAmt = etxtCreditAmount.getText().toString();

                String billAccId = null;
                if (customerData != null && customerData.billingAccountInfo != null && customerData.billingAccountInfo.billingAccountId != null)
                    billAccId = customerData.billingAccountInfo.billingAccountId;

                if (!chkByCash.isChecked() && !chkByDebitCredit.isChecked() && !chkByCheque.isChecked() && !chkByCredit.isChecked()) {
                    txtPaymentError.setVisibility(View.VISIBLE);
                    return;
                }

                if (!chkbxCreditAmount.isChecked()) {
                    if (chkByCash.isChecked()) {
                        if (TextUtils.isEmpty(cashAmt)) {
                            etxtAmountCash.setError("Can't be empty");
                            return;
                        }
                    }

                    if (chkByDebitCredit.isChecked()) {
                        if (TextUtils.isEmpty(ccAmt)) {
                            etxtDebitCreditCardAmount.setError("Can't be empty");
                            return;
                        }
                        if (TextUtils.isEmpty(ccRefNum)) {
                            etxtByDebitCreditRefNo.setError("Can't be empty");
                            return;
                        }
                    }
                    if (chkByCheque.isChecked()) {
                        if (TextUtils.isEmpty(checkAmt)) {
                            etxtBankAmount.setError("Can't be empty");
                            return;
                        }
                        if (TextUtils.isEmpty(checkRefNum)) {
                            etxtBankRefNo.setError("Can't be empty");
                            return;
                        }
                    }
                    ((DashBordActivity) context).cartPaymentForAllCategories(cashAmt, ccAmt, ccRefNum, bankName, checkAmt, checkRefNum, billAccId, "");
                } else {
                    if (chkByCredit.isChecked()) {
                        if (TextUtils.isEmpty(billAccAmt)) {
                            etxtCreditAmount.setError("Can't be empty");
                            return;
                        }
                    }
                    ((DashBordActivity) context).cartPaymentForAllCategories("", "", "", "", "", "", "", billAccAmt);
                }
                Util.hideKeyboard(dialogView.findFocus());
                alertDialog.dismiss();
            }
        });
    }

    private void setEditTextMode(EditText editText, boolean unableOrDisable) {
        editText.setFocusableInTouchMode(unableOrDisable);
        editText.setFocusable(unableOrDisable);
    }

    public void promotion() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.promotion, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelPromotion = (TextView) dialogView.findViewById(R.id.txtCancelPromotion);
        TextView txtApplyPromotion = (TextView) dialogView.findViewById(R.id.txtApplyPromotion);
        final AlertDialog alertDialog = dialogBuilder.create();
        txtApplyPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etextPromotion = (EditText) dialogView.findViewById(R.id.etextPromotion);
                if (TextUtils.isEmpty(etextPromotion.getText().toString().trim())) {
                    return;
                }
                ((DashBordActivity) context).applyPromotion(etextPromotion.getText().toString().trim());
                Util.hideKeyboard(v);
                alertDialog.dismiss();
            }
        });
        txtCancelPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(v);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void loyaltyPoint(long availableLoyaltyPoints) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.loyalty_points, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelLoyaltyPoints = ((TextView) dialogView.findViewById(R.id.txtCancelLoyaltyPoints));
        TextView txtApplyLoyaltyPoints = ((TextView) dialogView.findViewById(R.id.txtApplyLoyaltyPoints));
        TextView txtLoyaltyPointsDialog = ((TextView) dialogView.findViewById(R.id.txtLoyaltyPointsDialog));
        txtLoyaltyPointsDialog.setText("Available Loyalty Points:" + availableLoyaltyPoints);
        final AlertDialog alertDialog = dialogBuilder.create();
        txtApplyLoyaltyPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) dialogView.findViewById(R.id.etxtLoyaltyPoint);
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    return;
                }
                ((DashBordActivity) context).redeemLoyaltyPoints(editText.getText().toString().trim());
                Util.hideKeyboard(v);
                alertDialog.dismiss();
            }
        });
        txtCancelLoyaltyPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(v);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void logoutConfirmation() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.logout_confirmation, null);
        dialogBuilder.setView(dialogView);
        TextView txtNoLogout = ((TextView) dialogView.findViewById(R.id.txtNoLogout));
        TextView txtYesLogout = ((TextView) dialogView.findViewById(R.id.txtYesLogout));
        final AlertDialog alertDialog = dialogBuilder.create();
        txtYesLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashBordActivity) context).doSignOut();
                alertDialog.dismiss();
            }
        });
        txtNoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void productVariantDialog(final ProductAttributesData productAttributesData) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.product_variants, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelProductVariants = ((TextView) dialogView.findViewById(R.id.txtCancelProductVariants));
        TextView txtAddToCartProductVariants = ((TextView) dialogView.findViewById(R.id.txtAddToCartProductVariants));
        final LinearLayout variantContainer = ((LinearLayout) dialogView.findViewById(R.id.variantContainer));
        try {
            variantContainer.removeAllViews();
            Object object = productAttributesData.variantTree;
            for (int i = 0; i < productAttributesData.featureOrder.size(); i++) {
                String variantName = ((String) productAttributesData.featureTypes.get(productAttributesData.featureOrder.get(i)));
                if (object instanceof Map) {
                    Set<String> variantOptions = ((Map) object).keySet();
                    ArrayList<String> variantOptionsList = new ArrayList<String>(variantOptions);
                    if (variantOptionsList.size() > 0) {
                        object = ((Map) object).get(variantOptionsList.get(0));
                    }
                    View subVariantView = addSubVariant(inflater, variantOptionsList, variantName);
                    variantContainer.addView(subVariantView);
                    LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) subVariantView.getLayoutParams());
                    int margin = ((int) Env.currentActivity.getResources().getDimension(R.dimen.dim_5));
                    layoutParams.setMargins(margin, margin, margin, margin);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final AlertDialog alertDialog = dialogBuilder.create();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vid = v.getId();
                if (vid == R.id.txtAddToCartProductVariants) {
                    Object productTree = productAttributesData.variantTree;
                    for (int i = 0; i < variantContainer.getChildCount(); i++) {
                        Spinner dropdownList = ((Spinner) variantContainer.getChildAt(i).getTag());
                        String key = dropdownList.getSelectedItem().toString();
                        if (productTree instanceof Map) {
                            productTree = ((Map) productTree).get(key);
                        }
                    }
                    if (productTree instanceof ArrayList) {
                        productId = ((String) ((ArrayList) productTree).get(0));
                    }
                    if (TextUtils.isEmpty(productId)) {
                        Util.showCenteredToast(Env.currentActivity, "Invalid details");
                        return;
                    }
                    ((DashBordActivity) Env.currentActivity).addNewItemToCart(productId, "1");
                }
                alertDialog.dismiss();
            }
        };
        txtCancelProductVariants.setOnClickListener(onClickListener);
        txtAddToCartProductVariants.setOnClickListener(onClickListener);
        alertDialog.show();
    }

    private View addSubVariant(LayoutInflater inflater, List<String> list, String subVariantName) throws Exception {
        LinearLayout variant_row = (LinearLayout) inflater.inflate(R.layout.variant_row, null);
        TextView txtVariantName = ((TextView) variant_row.findViewById(R.id.txtVariantName));
        Spinner drpDwnVariant = ((Spinner) variant_row.findViewById(R.id.drpDwnVariant));
        txtVariantName.setText(subVariantName);
        variant_row.setTag(drpDwnVariant);
        ArrayAdapter adapter = new ArrayAdapter(drpDwnVariant.getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        drpDwnVariant.setAdapter(adapter);
        return variant_row;
    }


    public void paidInDialog(final ArrayList<PaidInOutData> reasons_list) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.paid_in, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelPaidInDialog = ((TextView) dialogView.findViewById(R.id.txtCancelPaidInDialog));
        TextView txtConfirmPaidInDialog = ((TextView) dialogView.findViewById(R.id.txtConfirmPaidInDialog));
        Spinner drpdwnPaidInDialog = ((Spinner) dialogView.findViewById(R.id.drpdwnPaidInDialog));
        ArrayAdapter arrayAdapter = new ArrayAdapter(drpdwnPaidInDialog.getContext(), android.R.layout.simple_spinner_dropdown_item, reasons_list);
        drpdwnPaidInDialog.setAdapter(arrayAdapter);
        drpdwnPaidInDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paidReasonEnumId = reasons_list.get(position).enumId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final AlertDialog alertDialog = dialogBuilder.create();
        txtConfirmPaidInDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etxtCommentPaidInDialog = (EditText) dialogView.findViewById(R.id.etxtCommentPaidInDialog);
                EditText etxtAmountPaidInDialog = (EditText) dialogView.findViewById(R.id.etxtAmountPaidInDialog);
                String amount = etxtAmountPaidInDialog.getText().toString();
                if (amount.isEmpty()) {
                    etxtAmountPaidInDialog.setError("Can't be empty");
                    return;
                }
                String comment = etxtCommentPaidInDialog.getText().toString().trim();
                ((DashBordActivity) context).paidOutAndIn(amount, comment, paidReasonEnumId, "IN");
                alertDialog.dismiss();
            }
        });
        txtCancelPaidInDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void paidOutDialog(final ArrayList<PaidInOutData> reasons_list) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.paid_out, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelPaidOutDialog = ((TextView) dialogView.findViewById(R.id.txtCancelPaidOutDialog));
        TextView txtConfirmPaidOutDialog = ((TextView) dialogView.findViewById(R.id.txtConfirmPaidOutDialog));
        Spinner drpDwnPaidOutDialog = ((Spinner) dialogView.findViewById(R.id.drpDwnPaidOutDialog));
        ArrayAdapter arrayAdapter = new ArrayAdapter(drpDwnPaidOutDialog.getContext(), android.R.layout.simple_spinner_dropdown_item, reasons_list);
        drpDwnPaidOutDialog.setAdapter(arrayAdapter);
        drpDwnPaidOutDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paidReasonEnumId = reasons_list.get(position).enumId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final AlertDialog alertDialog = dialogBuilder.create();
        txtConfirmPaidOutDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etxtCommentPaidOutDialog = (EditText) dialogView.findViewById(R.id.etxtCommentPaidOutDialog);
                EditText etxtAmountPaidOutDialog = (EditText) dialogView.findViewById(R.id.etxtAmountPaidOutDialog);
                String amount = etxtAmountPaidOutDialog.getText().toString();
                if (amount.isEmpty()) {
                    etxtAmountPaidOutDialog.setError("Can't be empty");
                    return;
                }
                String comment = etxtCommentPaidOutDialog.getText().toString().trim();
                ((DashBordActivity) context).paidOutAndIn(amount, comment, paidReasonEnumId, "OUT");
                alertDialog.dismiss();
            }
        });
        txtCancelPaidOutDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void saveSaleDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.save_sale, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelSaveSaleDialog = ((TextView) dialogView.findViewById(R.id.txtCancelSaveSaleDialog));
        TextView txtSaveClearSaleDialog = ((TextView) dialogView.findViewById(R.id.txtSaveClearSaleDialog));
        TextView txtSaveSaleDialog = ((TextView) dialogView.findViewById(R.id.txtSaveSaleDialog));
        final AlertDialog alertDialog = dialogBuilder.create();
        txtSaveClearSaleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etxtListNameSaveSaleDialog = ((EditText) dialogView.findViewById(R.id.etxtListNameSaveSaleDialog));
                String shoppingListName = etxtListNameSaveSaleDialog.getText().toString().trim();
                if (TextUtils.isEmpty(shoppingListName)) {
                    etxtListNameSaveSaleDialog.setError("Can't be empty");
                    return;
                }
                ((DashBordActivity) Env.currentActivity).saveSale(shoppingListName, true);
                Util.hideKeyboard(etxtListNameSaveSaleDialog);
                alertDialog.dismiss();
            }
        });
        txtSaveSaleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etxtListNameSaveSaleDialog = ((EditText) dialogView.findViewById(R.id.etxtListNameSaveSaleDialog));
                String shoppingListName = etxtListNameSaveSaleDialog.getText().toString().trim();
                if (TextUtils.isEmpty(shoppingListName)) {
                    etxtListNameSaveSaleDialog.setError("Can't be empty");
                    return;
                }
                ((DashBordActivity) Env.currentActivity).saveSale(shoppingListName, false);
                Util.hideKeyboard(etxtListNameSaveSaleDialog);
                alertDialog.dismiss();
            }
        });
        txtCancelSaveSaleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void loadSaleDialog(SaleData saleData) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.load_sale, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelLoadSaleDialog = ((TextView) dialogView.findViewById(R.id.txtCancelLoadSaleDialog));
        TextView txtAddLoadSaleDialog = ((TextView) dialogView.findViewById(R.id.txtAddLoadSaleDialog));
        TextView txtReplaceLoadSaleDialog = ((TextView) dialogView.findViewById(R.id.txtReplaceLoadSaleDialog));
        TextView txtDeleteLoadSaleDialog = ((TextView) dialogView.findViewById(R.id.txtDeleteLoadSaleDialog));
        TextView txtReplaceDeleteLoadSaleDialog = ((TextView) dialogView.findViewById(R.id.txtReplaceDeleteLoadSaleDialog));
        LinearLayout llSaleListContainer = ((LinearLayout) dialogView.findViewById(R.id.llSaleListContainer));
        final AlertDialog alertDialog = dialogBuilder.create();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vid = v.getId();
                if (vid == R.id.txtCancelLoadSaleDialog) {
                    alertDialog.dismiss();
                    return;
                } else if (vid == R.id.txtSaveSaleItem) {
                    if (lastSelectedView != null) {
                        lastSelectedView.setSelected(false);
                    }
                    v.setSelected(true);
                    lastSelectedView = (TextView) v;
                    return;
                } else {
                    if (lastSelectedView == null) {
                        Util.showCenteredToast(Env.currentActivity, "Please select at least one Item");
                        return;
                    }
                    String selectedShoppingListId = ((String) lastSelectedView.getTag());
                    String action = "";
                    if (vid == R.id.txtAddLoadSaleDialog) {
                        action = "add";
                    } else if (vid == R.id.txtReplaceLoadSaleDialog) {
                        action = "replace";
                    } else if (vid == R.id.txtDeleteLoadSaleDialog) {
                        action = "delete";
                    } else if (vid == R.id.txtReplaceDeleteLoadSaleDialog) {
                        action = "replaceAndDelete";
                    }
                    ((DashBordActivity) Env.currentActivity).loadSale(selectedShoppingListId, action);
                    alertDialog.dismiss();
                }

            }
        };
        llSaleListContainer.removeAllViews();
        Set<String> set = saleData.saved_sale.keySet();
        for (String key : set) {
            TextView textView = (TextView) inflater.inflate(R.layout.saved_sale_list_row, null);
            textView.setText(saleData.saved_sale.get(key));
            textView.setTag(key);
            textView.setOnClickListener(onClickListener);
            llSaleListContainer.addView(textView);
        }
        txtCancelLoadSaleDialog.setOnClickListener(onClickListener);
        txtAddLoadSaleDialog.setOnClickListener(onClickListener);
        txtReplaceLoadSaleDialog.setOnClickListener(onClickListener);
        txtDeleteLoadSaleDialog.setOnClickListener(onClickListener);
        txtReplaceDeleteLoadSaleDialog.setOnClickListener(onClickListener);
        alertDialog.show();
    }

    public void openTerminal() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.open_terminal, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelOpenTerminal = ((TextView) dialogView.findViewById(R.id.txtCancelOpenTerminal));
        TextView txtConfirmOpenTerminal = ((TextView) dialogView.findViewById(R.id.txtConfirmOpenTerminal));
        final AlertDialog alertDialog = dialogBuilder.create();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vid = v.getId();
                if (vid == R.id.txtConfirmOpenTerminal) {
                    EditText editText = (EditText) dialogView.findViewById(R.id.etxtStartingDrawerAmount);
                    String startingDrawerAmount = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(startingDrawerAmount)) {
                        startingDrawerAmount = "0";
                    }
                    ((DashBordActivity) Env.currentActivity).openTerminal(startingDrawerAmount);
                    Util.hideKeyboard(editText);
                }
                alertDialog.dismiss();
            }
        };
        txtCancelOpenTerminal.setOnClickListener(onClickListener);
        txtConfirmOpenTerminal.setOnClickListener(onClickListener);
        alertDialog.show();
    }

    public void closeTerminal() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((DashBordActivity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.close_terminal, null);
        dialogBuilder.setView(dialogView);
        TextView txtCancelCloseTerminal = ((TextView) dialogView.findViewById(R.id.txtCancelCloseTerminal));
        TextView txtConfirmCloseTerminal = ((TextView) dialogView.findViewById(R.id.txtConfirmCloseTerminal));
        final AlertDialog alertDialog = dialogBuilder.create();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vid = v.getId();
                if (vid == R.id.txtConfirmCloseTerminal) {
                    String cashAmount = ((EditText) dialogView.findViewById(R.id.etxtCashAmountCloseTerminal)).getText().toString().trim();
                    String chequeAmount = ((EditText) dialogView.findViewById(R.id.etxtChequeAmountCloseTerminal)).getText().toString().trim();
                    String creditCardAmount = ((EditText) dialogView.findViewById(R.id.etxtCCAmountCloseTerminal)).getText().toString().trim();
                    String giftCardAmount = ((EditText) dialogView.findViewById(R.id.etxtGCAmountCloseTerminal)).getText().toString().trim();
                    String otherPaymentAmount = ((EditText) dialogView.findViewById(R.id.etxtOPTAmountCloseTerminal)).getText().toString().trim();
                    cashAmount = TextUtils.isEmpty(cashAmount) ? "0" : cashAmount;
                    chequeAmount = TextUtils.isEmpty(chequeAmount) ? "0" : chequeAmount;
                    creditCardAmount = TextUtils.isEmpty(creditCardAmount) ? "0" : creditCardAmount;
                    giftCardAmount = TextUtils.isEmpty(giftCardAmount) ? "0" : giftCardAmount;
                    otherPaymentAmount = TextUtils.isEmpty(otherPaymentAmount) ? "0" : otherPaymentAmount;
                    ((DashBordActivity) Env.currentActivity).closeTerminal(cashAmount, chequeAmount, creditCardAmount, giftCardAmount, otherPaymentAmount);
                    Util.hideKeyboard(alertDialog.getCurrentFocus());
                }
                alertDialog.dismiss();
            }
        };
        txtConfirmCloseTerminal.setOnClickListener(onClickListener);
        txtCancelCloseTerminal.setOnClickListener(onClickListener);
        alertDialog.show();
    }
}
