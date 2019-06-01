package com.orion.stocks.core.enums;

import java.util.ArrayList;
import java.util.List;

public enum POSITION_STATUS {

    OPEN, CLOSED;

    private static List<String> statusList = new ArrayList<String>();

    static {
        for(POSITION_STATUS positionStatus : POSITION_STATUS.values()) {
            statusList.add(positionStatus.name());
        }
    }

    public static List<String> getStatusList() {
        return statusList;
    }

}
