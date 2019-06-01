package com.orion.stocks.core;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public enum CONFIG_KEY {
        FEES_RATE
    }

    private double commissionRate;
    private double vatRate;
    private double transferFeeRate;
    private double sccpRate;

    private Map<String, Config> configurations = new HashMap<String, Config>();

    private Config() {
        // make this class singleton
    }

    public Config getConfig(String name) {
        if(!configurations.containsKey(name)) {
            init(name);
        }
        return configurations.get(name);
    }

    private void init(String name) {
        //@TODO - initialization of configurations
        Config config = new Config();
        config.setCommissionRate(0.0025);
        config.setVatRate(0.12);
        config.setTransferFeeRate(0.00005);
        config.setSccpRate(0.0001);
        configurations.put(name, config);
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getTransferFeeRate() {
        return transferFeeRate;
    }

    public void setTransferFeeRate(double transferFeeRate) {
        this.transferFeeRate = transferFeeRate;
    }

    public double getSccpRate() {
        return sccpRate;
    }

    public void setSccpRate(double sccpRate) {
        this.sccpRate = sccpRate;
    }

}
