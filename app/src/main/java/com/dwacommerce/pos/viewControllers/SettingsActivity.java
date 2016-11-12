package com.dwacommerce.pos.viewControllers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by admin on 08-08-2016.
 */

public class SettingsActivity extends AbstractFragmentActivity implements View.OnClickListener {
    private SettingsModel settingsModel = new SettingsModel();
    private EditText etxtUrlSettings;
    private EditText etxtUserNameSettings;
    private EditText etxtPasswordSettigs;
    private Button btnSubmitSettings;
    private ProductStores selectedProductStore;


    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.setting);
        init();
    }

    private void init() {
        etxtUrlSettings = (EditText) findViewById(R.id.etxtUrlSettings);
        etxtUserNameSettings = (EditText) findViewById(R.id.etxtUserNameSettings);
        etxtPasswordSettigs = (EditText) findViewById(R.id.etxtPasswordSettigs);
        btnSubmitSettings = (Button) findViewById(R.id.btnSubmitSettings);
        btnSubmitSettings.setOnClickListener(this);

        etxtUrlSettings.setText("http://leisureapparel.dwacommerce.com/webpos/rest");
        etxtUserNameSettings.setText("dwaadmin");
        etxtPasswordSettigs.setText("changeit");
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
                Config.setServerUrl(etxtUrlSettings.getText().toString().trim());
                if (userData.productStores != null && !userData.productStores.isEmpty()) {
                    ProductStores[] productStores = new ProductStores[userData.productStores.size()];
                    productStores = userData.productStores.toArray(productStores);
                    radioDialog(productStores);
                } else {
                    Util.showAlertDialog(null, "No product Store found!");
                }
                setBaseUrl();
            } else {
                Util.showAlertDialog(null, "Oops! something went wrong!");
            }

        } else if (data instanceof RetrofitError) {
            Util.showCenteredToast(SettingsActivity.this, Constants.DEFAULT_SERVER_ERROR);
        }
    }

    private void setBaseUrl() {
        try {
            //URL url = new URL(Config.getServerUrl());
            // String baseUrl = url.getProtocol() + "://" + url.getHost();
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
        int id = v.getId();
        if (id == R.id.btnSubmitSettings) {
            String url = etxtUrlSettings.getText().toString();
            String userName = etxtUserNameSettings.getText().toString();
            String password = etxtPasswordSettigs.getText().toString();
            if (validateCredentials(url.trim(), userName.trim(), password.trim())) {
                if (Util.isDeviceOnline()) {
                    Util.showProDialog(SettingsActivity.this);
                    settingsModel.fetchSettings(url, userName, password);
                } else {
                    Util.showCenteredToast(SettingsActivity.this, Constants.INTERNET_ERROR_MSG);
                }
            }
        }
    }

    private boolean validateCredentials(String url, String userName, String password) {
        if (TextUtils.isEmpty(url)) {
            etxtUrlSettings.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(userName)) {
            etxtUserNameSettings.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etxtPasswordSettigs.setError("Can't be Empty");
            return false;
        }
        return true;
    }

    private void radioDialog(ProductStores[] stores) {
        AlertDialog.Builder storeDialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.product_store, null);
        storeDialogBuilder.setView(dialogView);
        final RadioGroup rdgrpStore = ((RadioGroup) dialogView.findViewById(R.id.rdgrpStore));
        TextView txtOkStore = ((TextView) dialogView.findViewById(R.id.txtOkStore));
        TextView txtCancelStore = ((TextView) dialogView.findViewById(R.id.txtCancelStore));
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
                    Util.showCenteredToast(SettingsActivity.this, "No Store Selected!");
                    return;
                }
                if (selectedProductStore != null) {
                    Config.setProductStoreId(selectedProductStore.productStoreId);
                    Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
                    startActivity(intent);
                    alertDialog.dismiss();
                    SettingsActivity.this.finish();
                    return;
                }
                alertDialog.dismiss();
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
