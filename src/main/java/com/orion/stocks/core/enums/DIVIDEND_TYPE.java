package com.orion.stocks.core.enums;

import java.util.ArrayList;
import java.util.List;

public enum DIVIDEND_TYPE {

    CASH, SHARE;

    private static List<String> types = new ArrayList<String>();

    static {
        for(DIVIDEND_TYPE dividendType : DIVIDEND_TYPE.values()) {
            types.add(dividendType.name());
        }
    }

    public static List<String> getTypes() {
        return types;
    }

}
