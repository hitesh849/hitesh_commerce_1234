package com.dwacommerce.pos.viewControllers;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.PartyData;
import com.dwacommerce.pos.model.FindCustomerModel;
import com.dwacommerce.pos.utility.Constants;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.ArrayList;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by admin on 21-08-2016.
 */

public class FindCustomerActivity extends AbstractFragmentActivity implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    private FindCustomerModel findCustomerModel = new FindCustomerModel();
    private AutoCompleteTextView txtSearchParty;
    private ImageView imgBackFindCustomer;
    private LinearLayout llCustomerListContainer;
    private RadioGroup rdgrpFilterFindCustomer;
    private TextView txtAddNewCustomer;
    private RadioButton seletedButton;
    private PartyData selectedPartyData;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.find_customer_activity);
        init();
    }

    private void init() {
        txtSearchParty = (AutoCompleteTextView) findViewById(R.id.txtSearchParty);
        imgBackFindCustomer = (ImageView) findViewById(R.id.imgBackFindCustomer);
        llCustomerListContainer = (LinearLayout) findViewById(R.id.llCustomerListContainer);
        rdgrpFilterFindCustomer = (RadioGroup) findViewById(R.id.rdgrpFilterFindCustomer);
        txtAddNewCustomer = (TextView) findViewById(R.id.txtAddNewCustomer);
        txtSearchParty.addTextChangedListener(this);
        txtAddNewCustomer.setOnClickListener(this);
        imgBackFindCustomer.setOnClickListener(this);
        rdgrpFilterFindCustomer.check(R.id.rdByName);
    }

    @Override
    protected BasicModel getModel() {
        return findCustomerModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        Util.dimissProDialog();
        if (data instanceof PartyData) {
            PartyData partyData = ((PartyData) data);
            llCustomerListContainer.removeAllViews();
            setSuggestionList(partyData);
        } else if (data instanceof CommonResponseData) {
            CommonResponseData responseData = ((CommonResponseData) data);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                addNewContact(selectedPartyData);
                setResult(RESULT_OK);
                this.finish();
            } else {
                Util.showAlertDialog(null, responseData.response);
            }
        } else if (data instanceof RetrofitError) {
            Util.showAlertDialog(null, Constants.DEFAULT_SERVER_ERROR);
        }
    }

    private void setSuggestionList(PartyData partyData) {
        try {
            for (PartyData customerData : partyData.searchedParties) {
                TableRow tableRow = getCustomerRow(customerData);
                llCustomerListContainer.addView(tableRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TableRow getCustomerRow(PartyData customerData) {
        try {
            TableRow tableRow = ((TableRow) getLayoutInflater().inflate(R.layout.customer_data_row, null));
            TextView txtCustomerName = (TextView) tableRow.findViewById(R.id.txtCustomerName);
            TextView txtCustomerPhone = (TextView) tableRow.findViewById(R.id.txtCustomerPhone);
            TextView txtCustomerEmail = (TextView) tableRow.findViewById(R.id.txtCustomerEmail);
            TextView txtCustomerAddress = (TextView) tableRow.findViewById(R.id.txtCustomerAddress);
            RadioButton rdbtnSelectedCustomer = (RadioButton) tableRow.findViewById(R.id.rdbtnSelectedCustomer);
            txtCustomerName.setText(customerData.firstName);
            txtCustomerPhone.setText(customerData.contact);
            txtCustomerAddress.setText(customerData.address1);
            txtCustomerEmail.setText(customerData.email);
            tableRow.setTag(customerData);
            tableRow.setOnClickListener(this);
            rdbtnSelectedCustomer.setTag(customerData);
            rdbtnSelectedCustomer.setOnCheckedChangeListener(this);
            return tableRow;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.imgBackFindCustomer) {
            super.onBackPressed();
        } else if (vid == R.id.txtAddNewCustomer) {
            Intent intent = new Intent(FindCustomerActivity.this, AddNewCustomerActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_FOR_ADD_NEW_CUSTOMER);
        } else if (vid == R.id.customerDataContainer) {
            try {
                selectedPartyData = ((PartyData) v.getTag());
                if (Util.isDeviceOnline()) {
                    Util.showProDialog(FindCustomerActivity.this);
                    findCustomerModel.setPartyToCart(selectedPartyData.partyId, selectedPartyData.contactMechPurposeTypeId, selectedPartyData.contactMechId);
                } else {
                    Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!txtSearchParty.isPerformingCompletion()) {
            String searchText = editable.toString();
            if (searchText.length() >= 2) {
                int selectedFilterId = rdgrpFilterFindCustomer.getCheckedRadioButtonId();
                String searchType = "name";
                switch (selectedFilterId) {
                    case R.id.rdByName:
                        searchType = "name";
                        break;
                    case R.id.rdByContact:
                        searchType = "contact";
                        break;
                    case R.id.rdByEmail:
                        searchType = "email";
                        break;
                }
                findCustomerModel.searchParty(searchType, searchText);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if (seletedButton != null) {
                seletedButton.setChecked(false);
                seletedButton = null;
            }
            seletedButton = ((RadioButton) buttonView);
            selectedPartyData = ((PartyData) seletedButton.getTag());
            if (Util.isDeviceOnline()) {
                Util.showProDialog(FindCustomerActivity.this);
                findCustomerModel.setPartyToCart(selectedPartyData.partyId, selectedPartyData.contactMechPurposeTypeId, selectedPartyData.contactMechId);
            } else {
                Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean addNewContact(PartyData partInfo) {
        if (partInfo != null) {
            try {
                ArrayList<ContentProviderOperation> batch = new ArrayList<>();
                batch.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                // Adding insert operation to operations list
                // to insert display name in the table ContactsContract.Data
                batch.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, partInfo.firstName)
                        .build());

                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                batch.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, partInfo.contact)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, batch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
