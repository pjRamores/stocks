package com.orion.stocks.util;

import java.text.ParseException;

import com.orion.stocks.core.model.Dividend;
import com.orion.stocks.core.model.Position;

/*
 * @TODO - to implement static methods
 */
public class MoneyUtil {

    private static final double COMMISSION_RATE = 0.0025;
    private static final double VAT_RATE = 0.12;
    private static final double TRANS_FEE_RATE = 0.00005;
    private static final double SCCP_RATE = 0.0001;
    private static final double SALES_TAX_RATE = 0.005;

    public static Position calculateBuyFees(Position position, double buyPrice, int shares) {
        if(position == null) {
            System.err.println("Position object is null!");
            return position;
        }

        position.setBuyPrice(buyPrice);
        position.setShares(shares);

        double commission = buyPrice * shares * COMMISSION_RATE;
        commission = commission > 20 ? commission : 20.0;

        position.setBuyCommission(commission);
        position.setBuyVat(commission * VAT_RATE);
        position.setBuyTransFee(buyPrice * shares * TRANS_FEE_RATE);
        position.setBuySccp(buyPrice * shares * SCCP_RATE);
        position.setTotalExpenses((buyPrice * shares) 
                + position.getBuyCommission().doubleValue()
                + position.getBuyVat().doubleValue()
                + position.getBuyTransFee().doubleValue()
                + position.getBuySccp().doubleValue());

        return position;
    }

    public static Position calculateBuyFees(Position position, String buyPrice, String shares) {
        try {
            return calculateBuyFees(position, Util.moneyDisplayFormat.parse(buyPrice).doubleValue(), Util.moneyDisplayFormat.parse(shares).intValue());
        } catch(NumberFormatException | ParseException e) {
            e.printStackTrace();
            return position;
        }
    }

    public static Position calculateSellFees(Position position, double sellPrice, int shares) {
        if(position == null) {
            System.err.println("Position object is null!");
            return position;
        }

        position.setSellPrice(sellPrice);
        position.setShares(shares);

        double commission = sellPrice * shares * COMMISSION_RATE;
        commission = commission > 20 ? commission : 20.0;

        position.setSellCommission(commission);
        position.setSellVat(commission * VAT_RATE);
        position.setSellTransFee(sellPrice * shares * TRANS_FEE_RATE);
        position.setSellSccp(sellPrice * shares * SCCP_RATE);
        position.setSalesTax(sellPrice * shares * SALES_TAX_RATE);
        position.setNetSale((sellPrice * shares) 
                - position.getSellCommission().doubleValue()
                - position.getSellVat().doubleValue()
                - position.getSellTransFee().doubleValue()
                - position.getSellSccp().doubleValue()
                - position.getSalesTax().doubleValue());
        position.setNetGain(position.getNetSale().doubleValue() - position.getTotalExpenses().doubleValue());

        return position;
    }

    public static Position calculateSellFees(Position position, String sellPrice, String shares) {
        try {
            return calculateSellFees(position, Util.moneyDisplayFormat.parse(sellPrice).doubleValue(), Util.moneyDisplayFormat.parse(shares).intValue());
        } catch(NumberFormatException | ParseException e) {
            e.printStackTrace();
            return position;
        }
    }

    public static Dividend calculateCashDividend(Dividend dividend, int shares, double dividendPerShare) {
        if(dividend == null) {
            System.err.println("Dividend object is null!");
            return dividend;
        }

        dividend.setShares(shares);
        dividend.setCashDividend(dividendPerShare);
        dividend.setGrossAmount(dividendPerShare * shares);
        dividend.setWithholdingTax(dividendPerShare * shares * 0.1);
        dividend.setNetAmount(dividend.getGrossAmount().doubleValue() - dividend.getWithholdingTax().doubleValue());

        return dividend;
    }

    public static Dividend calculateCashDividend(Dividend dividend, String shares, String dividendPerShare) {
        try {
            return calculateCashDividend(dividend, Util.moneyDisplayFormat.parse(shares).intValue(), Util.moneyDisplayFormat.parse(dividendPerShare).doubleValue());
        } catch(NumberFormatException | ParseException e) {
            e.printStackTrace();
            return dividend;
        }
    }
}
