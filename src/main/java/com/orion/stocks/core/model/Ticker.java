package com.orion.stocks.core.model;

import java.time.LocalDateTime;

import com.orion.stocks.core.dao.TickersDao;
import com.orion.stocks.util.DateUtil;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ticker {

    private int id;
    private StringProperty stockCode;
    private DoubleProperty currentPrice;
    private StringProperty lastUpdatedDate;
    private DoubleProperty averagePrice;
    private IntegerProperty totalShares;
    private DoubleProperty gainLoss;
    private DoubleProperty dailyChangeAmout;
    private DoubleProperty dailyChangePercent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringProperty getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = new SimpleStringProperty(stockCode);
    }

    public DoubleProperty getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = new SimpleDoubleProperty(currentPrice);
        this.currentPrice.addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(oldValue)) {
                this.getLastUpdatedDate().set(DateUtil.dateTimeFormatter.format(LocalDateTime.now()));
                this.getGainLoss().set((newValue.doubleValue() * this.totalShares.doubleValue()) - (this.averagePrice.doubleValue() * this.totalShares.doubleValue()));
                TickersDao.updateTicker(this.stockCode.getValue(), LocalDateTime.now());
            }
        });
    }

    public StringProperty getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = new SimpleStringProperty(lastUpdatedDate);
    }

    public DoubleProperty getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = new SimpleDoubleProperty(averagePrice);
    }

    public IntegerProperty getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(int totalShares) {
        this.totalShares = new SimpleIntegerProperty(totalShares);
    }

    public DoubleProperty getGainLoss() {
        return gainLoss;
    }

    public void setGainLoss(double gainLoss) {
        this.gainLoss = new SimpleDoubleProperty(gainLoss);
    }

    public DoubleProperty getDailyChangeAmout() {
        return dailyChangeAmout;
    }

    public void setDailyChangeAmout(double dailyChangeAmout) {
        this.dailyChangeAmout = new SimpleDoubleProperty(dailyChangeAmout);
    }

    public DoubleProperty getDailyChangePercent() {
        return dailyChangePercent;
    }

    public void setDailyChangePercent(double dailyChangePercent) {
        this.dailyChangePercent = new SimpleDoubleProperty(dailyChangePercent);
    }

}
