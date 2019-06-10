package org.whispersystems.textsecuregcm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoinInfo {

    @JsonProperty
    private String currency;

    @JsonProperty
    private String issuer;


    @JsonProperty
    private String targetCurrency;

    @JsonProperty
    private String targetIssuer;

    @JsonProperty
    private Float targetRate = 1f;

    @JsonProperty
    private String icon;

    @JsonProperty
    private Float rate = 0.0f;

    @JsonProperty
    private Boolean depositAddress; // 是否支持充值地址

    public CoinInfo() {
    }

    public CoinInfo(String currency,String issuer,String icon,String targetCurrency,String targetIssuer,Float targetRate,Boolean depositAddress){
        this.currency = currency;
        this.issuer = issuer;
        this.icon = icon;
        this.targetCurrency = targetCurrency;
        this.targetIssuer = targetIssuer;
        this.targetRate = targetRate;
        this.depositAddress = depositAddress;
    }


    @Override
    public String toString() {
        return "CoinInfo [currency=" + currency + ", icon=" + icon + ", issuer=" + issuer + "]";
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getTargetIssuer() {
        return targetIssuer;
    }

    public void setTargetIssuer(String targetIssuer) {
        this.targetIssuer = targetIssuer;
    }

    public float getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(float targetRate) {
        this.targetRate = targetRate;
    }

    public Boolean getDepositAddress() {
        return depositAddress;
    }

    public void setDepositAddress(Boolean depositAddress) {
        this.depositAddress = depositAddress;
    }

    
    
}