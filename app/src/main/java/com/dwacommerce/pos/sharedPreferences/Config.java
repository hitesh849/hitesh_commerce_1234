package com.dwacommerce.pos.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.utility.Constants;

import org.byteclues.lib.init.Env;

/**
 * Created by admin on 8/9/2016.
 */

public class Config {
    private static SharedPreferences preferences = null;
    private static SharedPreferences.Editor editor;
    public static String PREFERENCES_NAME = "dwaPreferences";
    public static String SHOW_FANCY_DASHBOARD = "showFancyDashboard";
    public static String BASE_URL = "base_url";

    public static void init(Context mContext) {
        Config.preferences = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Config.editor = preferences.edit();
    }

    public static void clearPreferences() {
        editor.clear();
        savePreferences();
    }


    public static void setDemo(boolean isDemo) {
        editor.putBoolean(Constants.ISDEMO, isDemo);
        savePreferences();
    }

    public static boolean isDemo() {
        return preferences.getBoolean(Constants.ISDEMO, false);
    }

    public static void savePreferences() {
        editor.commit();
    }

    public static void setServerUrl(String URL) {
        editor.putString(Constants.KEY_SERVER_URL, URL);
        savePreferences();
    }

    public static String getServerUrl() {
        return preferences.getString(Constants.KEY_SERVER_URL, "http://leisureapparel.dwacommerce.com/webpos/rest");
    }

    public static void setCustomerId(String customerID) {
        editor.putString(Constants.KEY_CUSTOMER_ID, customerID);
        savePreferences();
    }

    public static String getCustomerId() {
        return preferences.getString(Constants.KEY_CUSTOMER_ID, null);
    }

    public static void setStoreImageUrl(String URL) {
        editor.putString("store_image_url", URL).commit();
    }

    public static String getStoreImageUrl() {
        return preferences.getString("store_image_url", null);
    }

    public static void setStoreImageString(String string) {
        editor.putString("store_image_string", string).commit();
    }

    public static String getStoreImageString() {
        return preferences.getString("store_image_string", null);
    }

    public static void setStoreName(String storeName) {
        editor.putString("store_Name", storeName).commit();
    }

    public static String getStoreName() {
        return preferences.getString("store_Name", "");
    }

    public static void setStoreAddress(String storeName) {
        editor.putString("store_Address", storeName).commit();
    }

    public static String getStoreAddress() {
        return preferences.getString("store_Address", "");
    }

    public static void setSessionId(String sessionId) {
        editor.putString(Constants.KEY_SESSION_ID, sessionId);
        savePreferences();
    }

    public static void removeKey(String key) {
        editor.remove(key);
        editor.apply();
    }

    public static String getSessionId() {
        return preferences.getString(Constants.KEY_SESSION_ID, null);
    }

    public static void setProductStoreId(String productStoreId) {
        editor.putString(Constants.KEY_PRODUCT_STORE_ID, productStoreId);
        savePreferences();
    }

    public static String getProductStoreId() {
        return preferences.getString(Constants.KEY_PRODUCT_STORE_ID, null);
    }

    public static void setPosTerminalId(String posTerminalId) {
        editor.putString(Constants.KEY_POS_TERMINAL_ID, posTerminalId);
        savePreferences();
    }

    public static String getPosTerminalId() {
        return preferences.getString(Constants.KEY_POS_TERMINAL_ID, null);
    }

    public static void setCountryList(String countryListString) {
        editor.putString(Constants.KEY_COUNTRY_LIST, countryListString);
        savePreferences();
    }

    public static String getCountryList() {
        return preferences.getString(Constants.KEY_COUNTRY_LIST, null);
    }

    public static void setFancyDashboard(boolean isFancy) {
        editor.putBoolean(SHOW_FANCY_DASHBOARD, isFancy).commit();
    }

    public static boolean getFancyDashboard() {
        return preferences.getBoolean(SHOW_FANCY_DASHBOARD, true);
    }

    public static void setInternalScannerUse(boolean internalScannerUse) {
        editor.putBoolean("internal_scanner", internalScannerUse).commit();
    }

    public static boolean getInternalScannerUse() {
        return preferences.getBoolean("internal_scanner", true);
    }

    public static void setUserName(String userName) {
        editor.putString("user_name", userName).commit();
    }

    public static String getUserName() {
        return preferences.getString("user_name", "demo");
    }

    public static void setUserPassword(String userPassword) {
        editor.putString("user_password", userPassword).commit();
    }

    public static String getUserPassword() {
        return preferences.getString("user_password", "demo");
    }

    public static void setBaseUrl(String baseUrl) {
        editor.putString(BASE_URL, baseUrl);
    }

    public static String getBaseUrl() {
        return preferences.getString(BASE_URL, "");
    }

    public static void setPrinterIpAddress(String ipAddress) {
        editor.putString("ip_address", ipAddress).commit();
    }

    public static String getPrinterIpAddress() {
        return preferences.getString("ip_address", Env.currentActivity.getString(R.string.default_target));
    }

    public static void setPrinterSeriesConstant(int constant) {
        editor.putInt("series_constant", constant).commit();
    }

    public static int getPrinterSeriesConstant() {
        return preferences.getInt("series_constant", 0);
    }

    public static void setPrinterLanguageConstant(int constant) {
        editor.putInt("language_constant", constant).commit();
    }

    public static int getPrinterLanguageConstant() {
        return preferences.getInt("language_constant", 0);
    }

    public static void setPrinterWidth(int width) {
        editor.putInt("printer_width", width).commit();
    }

    public static int getPrinterWidth() {
        return preferences.getInt("printer_width", 2);
    }

    public static void setPrintWithoutUserConfirmation(boolean printWithoutUserConfirmation) {
        editor.putBoolean("printing_confirmation", printWithoutUserConfirmation).commit();
    }

    public static boolean getPrintWithoutUserConfirmation() {
        return preferences.getBoolean("printing_confirmation", false);
    }
}
