package com.orion.stocks.core.model;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Position {

    private int id;

    private StringProperty status;
    private StringProperty broker;
    private StringProperty stockCode;
    private StringProperty transactionType;
    private IntegerProperty shares;

    private LocalDate dateBought;
    private DoubleProperty buyPrice;
    private DoubleProperty buyCommission;
    private DoubleProperty buyVat;
    private DoubleProperty buyTransFee;
    private DoubleProperty buySccp;
    private DoubleProperty totalExpenses;

    private LocalDate dateSold;
    private DoubleProperty sellPrice;
    private DoubleProperty sellCommission;
    private DoubleProperty sellVat;
    private DoubleProperty sellTransFee;
    private DoubleProperty sellSccp;
    private DoubleProperty salesTax;
    private DoubleProperty netSale;
    private DoubleProperty netGain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringProperty getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = new SimpleStringProperty(status);
    }

    public StringProperty getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = new SimpleStringProperty(broker);
    }

    public StringProperty getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = new SimpleStringProperty(stockCode);
    }

    public StringProperty getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = new SimpleStringProperty(transactionType);
    }

    public IntegerProperty getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = new SimpleIntegerProperty(shares);
    }

    public LocalDate getDateBought() {
        return dateBought;
    }

    public void setDateBought(LocalDate dateBought) {
        this.dateBought = dateBought;
    }

    public DoubleProperty getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = new SimpleDoubleProperty(buyPrice);
    }

    public DoubleProperty getBuyCommission() {
        if(buyCommission == null) {
            buyCommission = new SimpleDoubleProperty(0.0);
        }
        return buyCommission;
    }

    public void setBuyCommission(Double buyCommission) {
        this.buyCommission = new SimpleDoubleProperty(buyCommission);
    }

    public DoubleProperty getBuyVat() {
        if(buyVat == null) {
            buyVat = new SimpleDoubleProperty(0.0);
        }
        return buyVat;
    }

    public void setBuyVat(Double buyVat) {
        this.buyVat = new SimpleDoubleProperty(buyVat);
    }

    public DoubleProperty getBuyTransFee() {
        if(buyTransFee == null) {
            buyTransFee = new SimpleDoubleProperty(0.0);
        }
        return buyTransFee;
    }

    public void setBuyTransFee(Double buyTransFee) {
        this.buyTransFee = new SimpleDoubleProperty(buyTransFee);
    }

    public DoubleProperty getBuySccp() {
        if(buySccp == null) {
            buySccp = new SimpleDoubleProperty(0.0);
        }
        return buySccp;
    }

    public void setBuySccp(Double buySccp) {
        this.buySccp = new SimpleDoubleProperty(buySccp);
    }

    public DoubleProperty getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = new SimpleDoubleProperty(totalExpenses);
    }

    public LocalDate getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDate dateSold) {
        this.dateSold = dateSold;
    }

    public DoubleProperty getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = new SimpleDoubleProperty(sellPrice);
    }

    public DoubleProperty getSellCommission() {
        return sellCommission;
    }

    public void setSellCommission(Double sellCommission) {
        this.sellCommission = new SimpleDoubleProperty(sellCommission);
    }

    public DoubleProperty getSellVat() {
        return sellVat;
    }

    public void setSellVat(Double sellVat) {
        this.sellVat = new SimpleDoubleProperty(sellVat);
    }

    public DoubleProperty getSellTransFee() {
        return sellTransFee;
    }

    public void setSellTransFee(Double sellTransFee) {
        this.sellTransFee = new SimpleDoubleProperty(sellTransFee);
    }

    public DoubleProperty getSellSccp() {
        return sellSccp;
    }

    public void setSellSccp(Double sellSccp) {
        this.sellSccp = new SimpleDoubleProperty(sellSccp);
    }

    public DoubleProperty getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(Double salesTax) {
        this.salesTax = new SimpleDoubleProperty(salesTax);
    }

    public DoubleProperty getNetSale() {
        return netSale;
    }

    public void setNetSale(Double netSale) {
        this.netSale = new SimpleDoubleProperty(netSale);
    }

    public DoubleProperty getNetGain() {
        return netGain;
    }

    public void setNetGain(Double netGain) {
        this.netGain = new SimpleDoubleProperty(netGain);
    }
}
