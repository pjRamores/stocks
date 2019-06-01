package com.orion.stocks.core.enums;

import java.util.ArrayList;
import java.util.List;

public enum BROKER {

    COL, FM;

    private static List<String> brokers = new ArrayList<String>();

    static {
        for(BROKER broker : BROKER.values()) {
            brokers.add(broker.name());
        }
    }

    public static List<String> getBrokers() {
        return brokers;
    }

}
