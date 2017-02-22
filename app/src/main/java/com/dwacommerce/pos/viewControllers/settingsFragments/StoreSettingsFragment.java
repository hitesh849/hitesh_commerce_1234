package com.dwacommerce.pos.viewControllers.settingsFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.ProductStores;
import com.dwacommerce.pos.dao.UserData;
import com.dwacommerce.pos.model.SettingsModel;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.viewControllers.LogInActivity;
import com.dwacommerce.pos.viewControllers.SettingActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Admin on 10/23/2016.
 */
public class StoreSettingsFragment extends AbstractFragment implements View.OnClickListener {
    private SettingsModel settingsModel = new SettingsModel();
    private EditText etxtUrlStoreSetting;
    private EditText etxtUserNameStoreSettings;
    private EditText etxtPasswordStoreSettigs;
    private EditText etxtCustomerIdStoreSettigs;
    private TextInputLayout etxtInputStoreNameStoreSetting;
    private EditText etxtStoreNameStoreSetting;
    private TextView txtSaveStoreSetting;
    private TextView txtDemoStoreSetting;
    private ProductStores selectedProductStore;
    private View view;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_store_layout, null);
        init();
        return view;
    }

    private void init() {
        etxtUrlStoreSetting = (EditText) view.findViewById(R.id.etxtUrlStoreSetting);
        etxtUserNameStoreSettings = (EditText) view.findViewById(R.id.etxtUserNameStoreSettings);
        etxtPasswordStoreSettigs = (EditText) view.findViewById(R.id.etxtPasswordStoreSettigs);
        txtSaveStoreSetting = (TextView) view.findViewById(R.id.txtSaveStoreSetting);
        txtDemoStoreSetting = (TextView) view.findViewById(R.id.txtDemoStoreSetting);
        etxtCustomerIdStoreSettigs = (EditText) view.findViewById(R.id.etxtCustomerIdStoreSettigs);
        etxtStoreNameStoreSetting = (EditText) view.findViewById(R.id.etxtStoreNameStoreSetting);
        etxtInputStoreNameStoreSetting = (TextInputLayout) view.findViewById(R.id.etxtInputStoreNameStoreSetting);
        txtSaveStoreSetting.setOnClickListener(this);
        txtDemoStoreSetting.setOnClickListener(this);

        if (!TextUtils.isEmpty(Config.getStoreName()))
            etxtStoreNameStoreSetting.setText(Config.getStoreName());
        else
            etxtStoreNameStoreSetting.setVisibility(View.GONE);

      /*  etxtUrlStoreSetting.setText("http://leisureapparel.dwacommerce.com/webpos/rest");
        etxtUserNameStoreSettings.setText("dwaadmin");
        etxtPasswordStoreSettigs.setText("changeit");*/
    }

    @Override
    protected BasicModel getModel() {
        return settingsModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        if (data instanceof UserData) {
            UserData userData = ((UserData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(userData.response.responseMessage)) {
                Config.setServerUrl(etxtUrlStoreSetting.getText().toString().trim()+"/webpos/rest");
                Config.setCustomerId(etxtCustomerIdStoreSettigs.getText().toString().trim());
                if (userData.productStores != null && !userData.productStores.isEmpty()) {
                    ProductStores[] productStores = new ProductStores[userData.productStores.size()];
                    productStores = userData.productStores.toArray(productStores);
                    radioDialog(productStores);
                } else {
                    Util.showAlertDialog(null, "No product Store found!");
                }
                setBaseUrl();
            } else {
                String msg="Oops! something went wrong!";
                if(userData.response!=null && userData.response.errorMessage!=null)
                {
                    msg=userData.response.errorMessage;
                }
                else
                Util.showAlertDialog(null, msg);
            }

        } else if (data instanceof RetrofitError) {
            Util.showCenteredToast(getActivity(), Constants.DEFAULT_SERVER_ERROR);
        }
    }

    private void setBaseUrl() {
        try {
            Config.setBaseUrl(getDomainUrl(Config.getServerUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDomainUrl(String url) {
        url = url + "/";
        String protocol = url.substring(0, url.indexOf("://"));
        String domain = url.substring(protocol.length() + 3);
        domain = domain.substring(0, domain.indexOf("/"));
        return protocol + "://" + domain;
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.txtSaveStoreSetting) {
            String url = etxtUrlStoreSetting.getText().toString()+"/webpos/rest";
            String userName = etxtUserNameStoreSettings.getText().toString();
            String password = etxtPasswordStoreSettigs.getText().toString();
            String customerId = etxtCustomerIdStoreSettigs.getText().toString();
            if (TextUtils.isEmpty(url)) {
                etxtUrlStoreSetting.setError("Can't be Empty");
                return;
            }
            if (TextUtils.isEmpty(userName)) {
                etxtUserNameStoreSettings.setError("Can't be Empty");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etxtPasswordStoreSettigs.setError("Can't be Empty");
                return;
            }
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                settingsModel.fetchSettings(url, userName, password);
                Config.setDemo(false);
            } else {
                Util.showCenteredToast(getActivity(), Constants.INTERNET_ERROR_MSG);
            }
        } else if (vid == R.id.txtDemoStoreSetting) {
            Config.setProductStoreId("retailStore2");
            Config.setPosTerminalId(null);
            Config.setBaseUrl(getDomainUrl(Config.getServerUrl()));
            ((SettingActivity) getActivity()).setResult = true;
            Intent intent = new Intent(getActivity(), LogInActivity.class);
            Config.setDemo(true);
            startActivity(intent);
        } else {
            getActivity().finish();
        }
    }

    private void radioDialog(ProductStores[] stores) {
        AlertDialog.Builder storeDialogBuilder = new AlertDialog.Builder(getActivity());
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.product_store, null);
        storeDialogBuilder.setView(dialogView);
        final RadioGroup rdgrpStore = ((RadioGroup) dialogView.findViewById(R.id.rdgrpStore));
        TextView txtOkStore = ((TextView) dialogView.findViewById(R.id.txtOkStore));
        TextView txtCancelStore = ((TextView) dialogView.findViewById(R.id.txtCancelStore));

        if (!TextUtils.isEmpty(Config.getStoreName()))
            txtOkStore.setText(getString(R.string.change_store));
        final AlertDialog alertDialog = storeDialogBuilder.create();
        if (stores != null) {
            for (int i = 0; i < stores.length; i++) {
                RadioButton radioButton = new RadioButton(dialogView.getContext());
                radioButton.setText(stores[i].storeName);
                radioButton.setTag(stores[i]);
                rdgrpStore.addView(radioButton);
            }
        }
        txtOkStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) alertDialog.findViewById(rdgrpStore.getCheckedRadioButtonId());
                if (radioButton != null) {
                    selectedProductStore = ((ProductStores) radioButton.getTag());
                } else {
                    Util.showCenteredToast(getActivity(), "No Store Selected!");
                    return;
                }
                alertDialog.dismiss();
                if (selectedProductStore != null) {
                    Config.setProductStoreId(selectedProductStore.productStoreId);
                    Config.setPosTerminalId(null);
                    ((SettingActivity) getActivity()).setResult = true;
                    Intent intent = new Intent(getActivity(), LogInActivity.class);
                    startActivity(intent);
                    return;
                }
            }
        });
        txtCancelStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
