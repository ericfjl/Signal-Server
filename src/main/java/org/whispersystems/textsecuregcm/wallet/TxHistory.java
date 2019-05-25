
package org.whispersystems.textsecuregcm.wallet;

import java.security.Principal;
import java.util.List;

import javax.security.auth.Subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TxHistory implements Principal {

    @JsonProperty
    private long last_updated;

    @JsonProperty
    private Object marker;

    @JsonProperty
    private List<Object> transactions;

    @JsonProperty
    private String account;



    public long getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(long last_updated) {
        this.last_updated = last_updated;
    }

    public Object getMarker() {
        return marker;
    }

    public void setMarker(Object marker) {
        this.marker = marker;
    }

    public List<Object> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Object> transactions) {
        this.transactions = transactions;
    }
    
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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


}
