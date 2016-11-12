package com.dwacommerce.pos.viewControllers;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.KeyValueData;
import com.dwacommerce.pos.dao.StateData;
import com.dwacommerce.pos.model.AddNewCustomerModel;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragmentActivity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by admin on 8/24/2016.
 */
public class AddNewCustomerActivity extends AbstractFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private AddNewCustomerModel addNewCustomerModel = new AddNewCustomerModel();

    private ImageView imgBackNewCustomer;
    private ImageView imgSubmitAddNew;
    private EditText etxtFirstNameAddNew;
    private EditText etxtLastNameAddNew;
    private EditText etxtAddressAddNew;
    private EditText etxtPostalAddNew;
    private EditText etxtCityAddNew;
    private EditText etxtPhoneAddNew;
    private EditText etxtEmailAddNew;
    private AutoCompleteTextView autoCompleteCountryAddNew;
    private AutoCompleteTextView autoCompleteStateAddNew;
    private String countryGeoId;
    private String stateProvinceId;
    ArrayList<KeyValueData> countryList = new ArrayList<>();
    ArrayList<KeyValueData> stateList = new ArrayList<>();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.new_customer);
        init();
    }

    private void init() {
        imgBackNewCustomer = (ImageView) findViewById(R.id.imgBackNewCustomer);
        imgSubmitAddNew = (ImageView) findViewById(R.id.imgSubmitAddNew);
        etxtFirstNameAddNew = (EditText) findViewById(R.id.etxtFirstNameAddNew);
        etxtLastNameAddNew = (EditText) findViewById(R.id.etxtLastNameAddNew);
        etxtAddressAddNew = (EditText) findViewById(R.id.etxtAddressAddNew);
        etxtCityAddNew = (EditText) findViewById(R.id.etxtCityAddNew);
        etxtPostalAddNew = (EditText) findViewById(R.id.etxtPostalAddNew);
        etxtPhoneAddNew = (EditText) findViewById(R.id.etxtPhoneAddNew);
        etxtPhoneAddNew = (EditText) findViewById(R.id.etxtPhoneAddNew);
        etxtEmailAddNew = (EditText) findViewById(R.id.etxtEmailAddNew);
        autoCompleteCountryAddNew = (AutoCompleteTextView) findViewById(R.id.autoCompleteCountryAddNew);
        autoCompleteStateAddNew = (AutoCompleteTextView) findViewById(R.id.autoCompleteStateAddNew);
        imgBackNewCustomer.setOnClickListener(this);
        imgSubmitAddNew.setOnClickListener(this);
        prepareCountryList();
        setCountrySuggestionList();
        autoCompleteCountryAddNew.setOnItemClickListener(this);
        autoCompleteStateAddNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    stateProvinceId = ((KeyValueData) parent.getItemAtPosition(position)).key;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCountrySuggestionList() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, countryList);
        autoCompleteCountryAddNew.setAdapter(arrayAdapter);
    }

    private void prepareCountryList() {
        try {
            JSONObject jsonObject = new JSONObject(Config.getCountryList());
            countryList.clear();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                KeyValueData keyValueData = new KeyValueData();
                keyValueData.key = keys.next();
                keyValueData.value = jsonObject.getString(keyValueData.key);
                countryList.add(keyValueData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (countryList.isEmpty()) {
                Util.showProDialog(AddNewCustomerActivity.this);
                addNewCustomerModel.getCountryList();
            }
        }
    }

    private void prepareStateList(JSONObject jsonObject) {
        try {
            stateList.clear();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                KeyValueData keyValueData = new KeyValueData();
                keyValueData.key = keys.next();
                keyValueData.value = jsonObject.getString(keyValueData.key);
                stateList.add(keyValueData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stateList);
            autoCompleteStateAddNew.setAdapter(arrayAdapter);
        }
    }

    @Override
    protected BasicModel getModel() {
        return addNewCustomerModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        if (data instanceof StateData) {
            try {
                StateData stateData = ((StateData) data);
                if (Constants.RESPONSE_SUCCESS_MSG.equals(stateData.response)) {
                    prepareStateList(new JSONObject(stateData.geoList.toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data instanceof CommonResponseData) {
            CommonResponseData responseData = ((CommonResponseData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.responseMessage)) {
                this.finish();
            } else {
                Util.showAlertDialog(null, responseData.response);
            }
        } else if (data instanceof RetrofitError) {
            Util.showAlertDialog(null, Constants.DEFAULT_SERVER_ERROR);
        }

    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.imgBackNewCustomer) {
            super.onBackPressed();
        } else if (vid == R.id.imgSubmitAddNew) {
            String firstName = etxtFirstNameAddNew.getText().toString().trim();
            String lastName = etxtLastNameAddNew.getText().toString().trim();
            String email = etxtEmailAddNew.getText().toString().trim();
            String address = etxtAddressAddNew.getText().toString().trim();
            String postalCode = etxtPostalAddNew.getText().toString().trim();
            String city = etxtCityAddNew.getText().toString().trim();
            String state = autoCompleteStateAddNew.getText().toString().trim();
            String country = autoCompleteCountryAddNew.getText().toString().trim();
            String phone = etxtPhoneAddNew.getText().toString().trim();
            if (validDetails(firstName, lastName, email, address, country, state, city, postalCode, phone)) {
                if (Util.isDeviceOnline()) {
                    Util.showProDialog(this);
                    addNewCustomerModel.createNewParty(firstName, lastName, email, address, city, postalCode, countryGeoId, stateProvinceId);
                } else {
                    Util.showCenteredToast(AddNewCustomerActivity.this, Constants.INTERNET_ERROR_MSG);
                }

            }
        }

    }

    private boolean validDetails(String firstName, String lastName, String email, String address, String country, String state, String city, String postalCode, String phone) {
        if (TextUtils.isEmpty(firstName)) {
            etxtFirstNameAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(lastName)) {
            etxtLastNameAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            etxtEmailAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            etxtAddressAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(city)) {
            etxtCityAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(country)) {
            autoCompleteCountryAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(state)) {
            autoCompleteStateAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(postalCode)) {
            etxtPostalAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            etxtPhoneAddNew.setError("Can't be Empty");
            return false;
        }
        if (TextUtils.isEmpty(countryGeoId)) {
            autoCompleteCountryAddNew.setError("Invalid Country");
            return false;
        }
        if (TextUtils.isEmpty(stateProvinceId)) {
            autoCompleteStateAddNew.setError("Invalid State");
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        countryGeoId = ((KeyValueData) parent.getItemAtPosition(position)).key;
        autoCompleteStateAddNew.setText("");
        stateProvinceId = "";
        Util.showProDialog(AddNewCustomerActivity.this);
        addNewCustomerModel.getStateList(countryGeoId);
    }
}
