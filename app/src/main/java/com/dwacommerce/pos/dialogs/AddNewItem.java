package com.dwacommerce.pos.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.ProductData;
import com.dwacommerce.pos.model.AddNewItemModel;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.viewControllers.DashBordActivity;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractDialogFragment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by admin on 8/19/2016.
 */

public class AddNewItem extends AbstractDialogFragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {

    private AddNewItemModel addNewItemModel = new AddNewItemModel();
    private Dialog dialog;
    private Button btnMinusQuantityAddNew;
    private Button btnPlusQuantityAddNew;
    private EditText etQuantityAddNew;
    private TextView txtAddToCartAddNew;
    private TextView txtDismissAddNew;
    private AutoCompleteTextView txtItemSearchAddNew;
    private ProductData selectedProductData;
    private boolean quantityDisable;
    private String productName;

    @Override
    protected Dialog onCreateDialogPost(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_cart_item);
        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        init();
        return dialog;
    }

    public void quantityDisable(boolean flag) {
        quantityDisable = flag;
    }

    public void setProductData(ProductData productData) {
        selectedProductData = productData;
    }

    private void init() {
        btnMinusQuantityAddNew = (Button) dialog.findViewById(R.id.btnMinusQuantityAddNew);
        btnPlusQuantityAddNew = (Button) dialog.findViewById(R.id.btnPlusQuantityAddNew);
        etQuantityAddNew = (EditText) dialog.findViewById(R.id.etQuantityAddNew);
        txtAddToCartAddNew = (TextView) dialog.findViewById(R.id.txtAddToCartAddNew);
        txtDismissAddNew = (TextView) dialog.findViewById(R.id.txtDismissAddNew);
        txtItemSearchAddNew = (AutoCompleteTextView) dialog.findViewById(R.id.txtItemSearchAddNew);
        btnMinusQuantityAddNew.setOnClickListener(this);
        btnPlusQuantityAddNew.setOnClickListener(this);
        txtAddToCartAddNew.setOnClickListener(this);
        txtDismissAddNew.setOnClickListener(this);

        etQuantityAddNew.setFocusableInTouchMode(quantityDisable);
        etQuantityAddNew.setFocusable(quantityDisable);
        if (selectedProductData != null) {
            txtItemSearchAddNew.setText(selectedProductData.productName);
        } else {
            txtItemSearchAddNew.addTextChangedListener(this);
            txtItemSearchAddNew.setOnItemClickListener(this);
        }


        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(Constants.BARCODE_VALUE)) {
            String barcode = arguments.getString(Constants.BARCODE_VALUE);
            arguments.clear();
            txtItemSearchAddNew.setText(barcode);
        }
    }

    @Override
    protected BasicModel getModel() {
        return addNewItemModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        try {
            if (data instanceof ProductData) {
                ProductData productData = ((ProductData) data);
                setProductSuggestionList(productData.productsList);
            } else if (data instanceof CommonResponseData) {
                CommonResponseData responseData = (CommonResponseData) data;
                if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                    if (Env.currentActivity instanceof DashBordActivity) {
                        ((DashBordActivity) Env.currentActivity).fetchCartData();
                    }
                    dismiss();
                } else {
                    Util.showAlertDialog(null, responseData.response != null ? responseData.response : "error");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setProductSuggestionList(ArrayList<ProductData> productsList) {
        try {
            ArrayAdapter itemSuggestionsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productsList);
            txtItemSearchAddNew.setAdapter(itemSuggestionsAdapter);
            txtItemSearchAddNew.showDropDown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        try {
            if (vid == R.id.btnPlusQuantityAddNew) {
                int quantity = Integer.parseInt(etQuantityAddNew.getText().toString());
                quantity++;
                etQuantityAddNew.setText("" + quantity);

            } else if (vid == R.id.btnMinusQuantityAddNew) {
                int quantity = Integer.parseInt(etQuantityAddNew.getText().toString());
                quantity--;
                if (quantity < 1) {
                    quantity = 1;
                }
                etQuantityAddNew.setText("" + quantity);
            } else if (vid == R.id.txtAddToCartAddNew) {
                String productName = txtItemSearchAddNew.getText().toString();
                if (!TextUtils.isEmpty(productName)) {
                    if (Util.isDeviceOnline() && selectedProductData != null) {
                        Util.showProDialog(Env.currentActivity);
                        addNewItemModel.addItemToCart(selectedProductData.productId, etQuantityAddNew.getText().toString().trim());

                    }
                } else {
                    txtItemSearchAddNew.setError("Can't be blank!");
                }

            } else if (vid == R.id.txtDismissAddNew) {
                dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Util.showProDialog(Env.currentActivity);
        }


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!txtItemSearchAddNew.isPerformingCompletion()) {
            String searchText = editable.toString();
            if (searchText.length() >= 2) {
                addNewItemModel.findProducts(searchText);
            }
            selectedProductData = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            selectedProductData = ((ProductData) parent.getItemAtPosition(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
