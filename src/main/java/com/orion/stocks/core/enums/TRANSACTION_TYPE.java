package com.orion.stocks.core.enums;

import java.util.ArrayList;
import java.util.List;

public enum TRANSACTION_TYPE {

    TRADE, DIVIDEND;

    private static List<String> transactionTypes = new ArrayList<String>();

    static {
        for(TRANSACTION_TYPE transactionType : TRANSACTION_TYPE.values()) {
            transactionTypes.add(transactionType.name());
        }
    }

    public static List<String> getTransactionTypes() {
        return transactionTypes;
    }

}
