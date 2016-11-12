package com.dwacommerce.pos.dao;

/**
 * Created by admin on 8/11/2016.
 */

public class Terminals implements CharSequence {
    public String terminalName;
    public String posTerminalId;

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
        return terminalName;
    }
}
