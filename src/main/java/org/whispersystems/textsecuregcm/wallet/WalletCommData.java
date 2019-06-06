
package org.whispersystems.textsecuregcm.wallet;

import java.security.Principal;

import javax.security.auth.Subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletCommData implements Principal {

    @JsonProperty
    private String status;
    
    @JsonProperty
    private Object result;

    @JsonProperty
    private Object data;

    public WalletCommData() {

    }
    public WalletCommData(String status,Object result,Object data){
        this.status = status;
        this.result = result;
        this.data   = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


}
