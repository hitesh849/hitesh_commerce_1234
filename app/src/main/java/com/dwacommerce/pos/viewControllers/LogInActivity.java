package com.dwacommerce.pos.viewControllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.ConfigurationsData;
import com.dwacommerce.pos.dao.LoginData;
import com.dwacommerce.pos.dao.Terminals;
import com.dwacommerce.pos.model.LogInModel;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by admin on 07-08-2016.
 */

public class LogInActivity extends AbstractFragmentActivity implements View.OnClickListener {
    private LogInModel loginModel = new LogInModel();

    private EditText etxtUserNameLogin;
    private EditText etxtPasswordLogin;
    private Button btnSubmitLogin;
    private RelativeLayout rlSelectTerminalLogin;
    private TextView txtTerminalNameLogin;
    private Terminals[] terminals;
    private Terminals selectedTerminal;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.login_activity);
        if (TextUtils.isEmpty(Config.getProductStoreId())) {
            goToSettings();
        } else {
            init();
        }
    }

    private void init() {
        etxtUserNameLogin = (EditText) findViewById(R.id.etxtUserNameLogin);
        etxtPasswordLogin = (EditText) findViewById(R.id.etxtPasswordLogin);
        btnSubmitLogin = (Button) findViewById(R.id.btnSubmitLogin);
        txtTerminalNameLogin = (TextView) findViewById(R.id.txtTerminalNameLogin);
        rlSelectTerminalLogin = (RelativeLayout) findViewById(R.id.rlSelectTerminalLogin);
        btnSubmitLogin.setOnClickListener(this);
        rlSelectTerminalLogin.setOnClickListener(this);
        Util.showProDialog(LogInActivity.this);
        loginModel.saveSettings(Config.getProductStoreId());
        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(permissions)) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    private boolean hasPermissions(String[] permissions) {
        try {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(LogInActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void goToSettings() {
        Intent intent = new Intent(LogInActivity.this, SettingActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void gotoDashboard() {
        startActivity(new Intent(this, DashBordActivity.class));
        this.finish();
    }

    @Override
    protected BasicModel getModel() {
        return loginModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        if (data instanceof ConfigurationsData) {
            ConfigurationsData configurationsData = ((ConfigurationsData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(configurationsData.responseMessage)) {
                if (configurationsData.productStoreInfo != null) {
                    Config.setStoreImageUrl(configurationsData.productStoreInfo.storeImageUrl);
                    Config.setStoreAddress(configurationsData.productStoreInfo.storeAddress);
                    Config.setStoreName(configurationsData.productStoreInfo.storeName);
                    saveStoreImage();
                }
                if (configurationsData.posTerminals != null && !configurationsData.posTerminals.isEmpty()) {
                    terminals = new Terminals[configurationsData.posTerminals.size()];
                    terminals = configurationsData.posTerminals.toArray(terminals);
                    if (Config.isDemo()) {
                        etxtUserNameLogin.setText("demo");
                        etxtPasswordLogin.setText("demo");
                        selectedTerminal = terminals[0];
                        txtTerminalNameLogin.setText(selectedTerminal.terminalName);
                    }
                } else {
                    Util.showCenteredToast(LogInActivity.this, "No Terminals found!");
                    goToSettings();
                }
            } else {
                Util.showAlertDialog(null, Constants.DEFAULT_SERVER_ERROR);
            }
        } else if (data instanceof LoginData) {
            LoginData loginData = ((LoginData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(loginData.response)) {
                Config.setPosTerminalId(selectedTerminal.posTerminalId);
                Config.setSessionId(loginData.jsessionid);
                Config.setUserName(etxtUserNameLogin.getText().toString().trim());
                Config.setUserPassword(etxtPasswordLogin.getText().toString().trim());
                gotoDashboard();
            } else {
                Util.showAlertDialog(null, "User Not found!");
            }

        } else if (data instanceof RetrofitError) {
            Util.showAlertDialog(null, Constants.DEFAULT_SERVER_ERROR);
        }

    }

    private void saveStoreImage() {
        try {
            Picasso.with(LogInActivity.this).load(Config.getBaseUrl() + Config.getStoreImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try {
                        String string = Util.getEncoded64ImageStringFromBitmap(bitmap);
                        Config.setStoreImageString(string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSubmitLogin) {
            String userName = etxtUserNameLogin.getText().toString();
            String password = etxtPasswordLogin.getText().toString();
            if (validateCredentials(userName, password)) {
                if (Util.isDeviceOnline()) {
                    Util.showProDialog(LogInActivity.this);
                    loginModel.doLogin(userName, password, selectedTerminal.posTerminalId, Config.getProductStoreId());
                } else {
                    Util.showCenteredToast(LogInActivity.this, Constants.INTERNET_ERROR_MSG);
                }

            }
        } else if (id == R.id.rlSelectTerminalLogin) {
            terminalDialog(terminals);
        }
    }

    private boolean validateCredentials(String userName, String password) {
        if (TextUtils.isEmpty(userName)) {
            etxtUserNameLogin.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 4 || password.length() > 10) {
            etxtPasswordLogin.setError("between 4 and 10 alphanumeric characters");
            return false;
        }
        if (TextUtils.isEmpty(txtTerminalNameLogin.getText().toString())) {
            txtTerminalNameLogin.setError("please select terminal");
            return false;
        }
        return true;
    }

    private void terminalDialog(final Terminals[] terminals) {
        AlertDialog.Builder terminalDialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.terminals_dialog, null);
        terminalDialogBuilder.setView(dialogView);
        final RadioGroup rdgrpTerminal = ((RadioGroup) dialogView.findViewById(R.id.rdgrpTerminal));
        TextView txtOkTerminals = ((TextView) dialogView.findViewById(R.id.txtOkTerminals));
        TextView txtCancelTerminals = ((TextView) dialogView.findViewById(R.id.txtCancelTerminals));
        final AlertDialog alertDialog = terminalDialogBuilder.create();
        if (terminals != null) {
            for (int i = 0; i < terminals.length; i++) {
                RadioButton radioButton = new RadioButton(dialogView.getContext());
                radioButton.setText(terminals[i].terminalName);
                radioButton.setTag(terminals[i]);
                rdgrpTerminal.addView(radioButton);
            }
        }
        txtOkTerminals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) alertDialog.findViewById(rdgrpTerminal.getCheckedRadioButtonId());
                if (radioButton != null)
                    selectedTerminal = ((Terminals) radioButton.getTag());
                if (selectedTerminal != null)
                    txtTerminalNameLogin.setText(selectedTerminal.terminalName);
                alertDialog.dismiss();
            }
        });
        txtCancelTerminals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}