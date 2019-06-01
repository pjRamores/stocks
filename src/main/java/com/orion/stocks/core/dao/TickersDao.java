package com.orion.stocks.core.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.orion.stocks.core.model.Ticker;
import com.orion.stocks.util.DBUtil;
import com.orion.stocks.util.DateUtil;

public class TickersDao {

    private static Map<String, Ticker> tickers = null;

    public static Map<String, Ticker> getTickers() {
        if(tickers != null) {
            return tickers;
        }

        tickers = new LinkedHashMap<String, Ticker>();

        try {
            Connection conn = DBUtil.getConnection();
            String sql = "SELECT ID, STOCK_CODE, CURRENT_PRICE, DATE_FORMAT(LAST_MODIFIED_DATE, '%Y-%m-%d %H:%i:%S') LAST_MODIFIED_DATE FROM ticker ORDER BY STOCK_CODE";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs != null) {
                while(rs.next()) {
                    String stockCode = rs.getString("STOCK_CODE");

                    double currentPrice = rs.getString("CURRENT_PRICE") == null ? 0.0 : rs.getDouble("CURRENT_PRICE");
                    double averagePrice = PositionsDao.getOpenPositions(stockCode)
                            .stream()
                            .mapToDouble(p -> p.getBuyPrice().doubleValue())
                            .average()
                            .orElse(0.0);
                    int totalShares = PositionsDao.getOpenPositions(stockCode)
                            .stream()
                            .mapToInt(p -> p.getShares().intValue())
                            .sum();
                    double gainLoss = (currentPrice * totalShares) - (averagePrice * totalShares);

                    // if(totalShares == 0) continue; //exclude non-active stocks

                    Ticker ticker = new Ticker();
                    ticker.setId(rs.getInt("ID"));
                    ticker.setStockCode(stockCode);
                    ticker.setLastUpdatedDate(DateUtil.dateTimeFormatter.format(LocalDateTime.parse(rs.getString("LAST_MODIFIED_DATE"), DateUtil.dateTimeFormatter)));
                    ticker.setDailyChangeAmout(0.0);
                    ticker.setDailyChangePercent(0.0);
                    ticker.setGainLoss(gainLoss);
                    ticker.setTotalShares(totalShares);
                    ticker.setAveragePrice(averagePrice);
                    ticker.setCurrentPrice(currentPrice);

                    tickers.put(stockCode, ticker);
                }
            }

            conn.close();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return tickers;
    }

    public static void updateTicker(String stockCode, LocalDateTime now) {
        Ticker ticker = getTickers().get(stockCode);

        if(ticker == null) {
            System.err.println("Invalid stock code : " + stockCode);
            return;
        }

        try {
            Connection conn = DBUtil.getConnection();
            String sql = "UPDATE ticker SET CURRENT_PRICE = '" + ticker.getCurrentPrice().doubleValue() + "', LAST_MODIFIED_DATE = '"+now.toString()+"' WHERE ID = '" + ticker.getId() + "'";
            Statement stmt = conn.createStatement();
            int records = stmt.executeUpdate(sql);
            System.out.println("Updated " + records + " record(s) of ticker table.");
            conn.close();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Ticker> getTickersAsList() {
        List<Ticker> tickers = new ArrayList<Ticker>();

        for(Ticker ticker : getTickers().values()) {
            tickers.add(ticker);
        }

        return tickers;
    }

}
