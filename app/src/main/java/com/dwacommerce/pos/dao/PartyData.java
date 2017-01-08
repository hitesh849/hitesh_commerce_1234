package com.dwacommerce.pos.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 8/12/2016.
 */

public class PartyData implements Serializable, CharSequence {
    public String responseMessage;
    public ArrayList<PartyData> searchedParties;
    public String lastName;
    public String contactMechId;
    public String stateProvinceGeoId;
    public String address1;
    public String contactMechPurposeTypeId;
    public String contactMechTypeId;
    public String partyTypeId;
    public String city;
    public String postalCode;
    public String countryGeoId;
    public String fromDate;
    public String partyId;
    public String firstName;
    public String contact;
    public String email;

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    @Override
    public String toString() {
        return firstName;
    }
}
