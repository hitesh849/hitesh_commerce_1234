package com.dwacommerce.pos.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.CartLineData;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.model.UpdateCartItemModel;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.viewControllers.DashBordActivity;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractDialogFragment;

import java.util.Observable;

/**
 * Created by admin on 21-08-2016.
 */

public class UpdateCartItem extends AbstractDialogFragment implements View.OnClickListener {
    private UpdateCartItemModel updateCartItemModel = new UpdateCartItemModel();
    private Dialog dialog;
    private CartLineData cartLineData;
    private TextView txtNameUpdateCartItem;
    private TextView txtQuantityUpdateItem;
    private TextView txtUpdateCartItem;
    private TextView txtDismissUpdateItem;
    private Button btnPlusUpdateItem;
    private Button btnMinusUpdateCartItem;

    @Override
    protected Dialog onCreateDialogPost(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_cart_item);
        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        init();
        return dialog;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Constants.SERIALIZABLE_DATA)) {
            cartLineData = ((CartLineData) bundle.getSerializable(Constants.SERIALIZABLE_DATA));
        }
        txtNameUpdateCartItem = (TextView) dialog.findViewById(R.id.txtNameUpdateCartItem);
        txtQuantityUpdateItem = (TextView) dialog.findViewById(R.id.txtQuantityUpdateItem);
        txtUpdateCartItem = (TextView) dialog.findViewById(R.id.txtUpdateCartItem);
        txtDismissUpdateItem = (TextView) dialog.findViewById(R.id.txtDismissUpdateItem);
        btnPlusUpdateItem = (Button) dialog.findViewById(R.id.btnPlusUpdateItem);
        btnMinusUpdateCartItem = (Button) dialog.findViewById(R.id.btnMinusUpdateCartItem);
        btnPlusUpdateItem.setOnClickListener(this);
        btnMinusUpdateCartItem.setOnClickListener(this);
        txtUpdateCartItem.setOnClickListener(this);
        txtDismissUpdateItem.setOnClickListener(this);
        txtNameUpdateCartItem.setText(cartLineData.productName);
        txtQuantityUpdateItem.setText(cartLineData.quantity + "");
    }

    @Override
    protected BasicModel getModel() {
        return updateCartItemModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        if (data instanceof CommonResponseData) {
            CommonResponseData responseData = ((CommonResponseData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                if (Env.currentActivity instanceof DashBordActivity) {
                    ((DashBordActivity) Env.currentActivity).fetchCartData();
                    dismiss();
                }
            } else {
                Util.showAlertDialog(null, TextUtils.isEmpty(responseData.response) ? "error" : responseData.response);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.txtDismissUpdateItem) {
            dismiss();
        } else if (vid == R.id.txtUpdateCartItem) {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(Env.currentActivity);
                updateCartItemModel.updateCartItem(txtQuantityUpdateItem.getText().toString().trim(), cartLineData.cartLineIndex + "");
            } else {
                Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
            }

        } else if (vid == R.id.btnPlusUpdateItem) {
            int quantity = Integer.parseInt(txtQuantityUpdateItem.getText().toString());
            quantity++;
            txtQuantityUpdateItem.setText("" + quantity);
        } else if (vid == R.id.btnMinusUpdateCartItem) {
            int quantity = Integer.parseInt(txtQuantityUpdateItem.getText().toString());
            quantity--;
            if (quantity < 1) {
                quantity = 1;
            }
            txtQuantityUpdateItem.setText("" + quantity);
        }
    }

}
