package org.byteclues.lib.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.byteclues.lib.init.Env;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 19-07-2015.
 */
public class Util {
    public static final String VAL_OK = "OK";
    private static ProgressDialog progressDialog = null;
    private static AlertDialog alertDialog = null;
    public static String FLD_STATUS = "status";
    public static final String STATUS_CODE_USER_LOGOUT = "UserLogout";
    public static final String ACTION_USER_LOGOUT = "userLogout";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_LOGOUT_MESSAGE = "logout_message";


    public static void showProDialog(Context context) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void dimissProDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showCenteredToast(Context ctx, String msg) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isDeviceOnline() {
        boolean isDeviceOnLine = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) Env.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isDeviceOnLine = netInfo != null && netInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeviceOnLine;
    }

    public static boolean checkValueForKey(JSONObject jsonObject, String key) {
        boolean b = false;
        if (jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)) {
            b = true;
        }
        return b;
    }

    public static AlertDialog showAlertDialog(DialogInterface.OnClickListener okClicklistener, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(Env.currentActivity);
            DialogInterface.OnClickListener clickListener = okClicklistener != null ? okClicklistener : new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    alertDialog = null;
                }
            };
            builder.setPositiveButton("Ok", clickListener);
            builder.setMessage(msg);
            alertDialog = builder.create();
            alertDialog.show();
            return alertDialog;
        }
        return null;
    }

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void writeDBfileOnSdcard(Context context, String databaseFileName) {
        try {
            File f = new File(context.getDatabasePath(databaseFileName).getAbsolutePath());
            FileInputStream fis = null;
            FileOutputStream fos = null;

            try {
                fis = new FileInputStream(f);
                fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + databaseFileName + ".sqlite");
                while (true) {
                    int i = fis.read();
                    if (i != -1) {
                        fos.write(i);
                    } else {
                        break;
                    }
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, ImageView imgView, String url, int defaultResource) {
        try {
            Picasso.with(context).load(url).fit().centerCrop().error(defaultResource).fit().placeholder(defaultResource).fit().into(imgView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) Env.currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    public static Bitmap getDecode64ImageStringFromBitmap(String strBase64, int default_resource_id) {
        try {
            byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeResource(Env.currentActivity.getResources(), default_resource_id);
    }

}
