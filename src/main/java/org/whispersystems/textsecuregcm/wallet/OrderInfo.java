
package org.whispersystems.textsecuregcm.wallet;

import java.security.Principal;

import javax.security.auth.Subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderInfo implements Principal {

    @JsonProperty
    private long date;//中国标准时间时间戳

    @JsonProperty
    private int type;//0:收款;1:付款;2:手续费;3:其他

    @JsonProperty
    private String amount;//交易金额

    @JsonProperty
    private String currency;//币种

    @JsonProperty
    private String otherAddr;//交易对方的钱包地址

    @JsonProperty
    private String hash;//交易hash
    

    public OrderInfo() {

    }

    // Principal implementation
    @Override
    @JsonIgnore
    public String getName() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean implies(Subject subject) {
        return false;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOtherAddr() {
        return otherAddr;
    }

    public void setOtherAddr(String otherAddr) {
        this.otherAddr = otherAddr;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }



}
