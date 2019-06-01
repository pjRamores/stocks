package com.orion.stocks.util;

import org.json.JSONObject;

public class StocksUtil {

    public static double [] getCurrentPriceFromPhisix(String stockCode) {
        if(Util.isBlankOrNull(stockCode)) {
            throw new RuntimeException("Invalid stock code! " + stockCode);
        }

        JSONObject jsonObject = Util.readJsonFromUrl(Constants.PHISIX_URL + "/" + stockCode.toLowerCase() + ".json");
        if(jsonObject == null) {
            throw new RuntimeException("Unable to fetch json data! " + stockCode);
        }

        System.out.println("jsonObject : " + jsonObject);
        double amount = jsonObject.getJSONArray("stock").getJSONObject(0).getJSONObject("price").getDouble("amount");
        double percentChange = jsonObject.getJSONArray("stock").getJSONObject(0).getDouble("percent_change");
        double [] currentPrice = new double[]{amount, percentChange};

        return currentPrice;
    }

}
