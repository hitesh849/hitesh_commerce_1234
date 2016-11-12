package com.dwacommerce.pos.utility;

/**
 * Created by Admin on 10/18/2016.
 */
public class SpnModelsItem {
    private String mModelName = "";
    private int mModelConstant = 0;

    public SpnModelsItem(String modelName, int modelConstant) {
        mModelName = modelName;
        mModelConstant = modelConstant;
    }

    public int getModelConstant() {
        return mModelConstant;
    }

    @Override
    public String toString() {
        return mModelName;
    }
}
