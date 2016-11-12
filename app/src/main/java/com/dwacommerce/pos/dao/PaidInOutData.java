package com.dwacommerce.pos.dao;

import java.util.ArrayList;

/**
 * Created by Admin on 9/30/2016.
 */
public class PaidInOutData extends CommonResponseData {
    public String enumId;
    public String description;
    public String paidType;
    public ArrayList<PaidInOutData> reason_list;

    @Override
    public String toString() {
        return description;
    }
}
