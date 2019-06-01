package com.orion.stocks.core.model;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Dividend {

    private int id;

    private StringProperty type;
    private StringProperty broker;
    private StringProperty stockCode;
    private IntegerProperty shares;
    private LocalDate paymentDate;
    private DoubleProperty openingPrice;
    private DoubleProperty cashDividend;
    private DoubleProperty grossAmount;
    private DoubleProperty withholdingTax;
    private DoubleProperty netAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringProperty getType() {
        return type;
    }

    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
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

    public IntegerProperty getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = new SimpleIntegerProperty(shares);
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public DoubleProperty getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(Double openingPrice) {
        this.openingPrice = new SimpleDoubleProperty(openingPrice);
    }

    public DoubleProperty getCashDividend() {
        return cashDividend;
    }

    public void setCashDividend(Double cashDividend) {
        this.cashDividend = new SimpleDoubleProperty(cashDividend);
    }

    public DoubleProperty getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Double grossAmount) {
        this.grossAmount = new SimpleDoubleProperty(grossAmount);
    }

    public DoubleProperty getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(Double withholdingTax) {
        this.withholdingTax = new SimpleDoubleProperty(withholdingTax);
    }

    public DoubleProperty getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = new SimpleDoubleProperty(netAmount);
    }

}
