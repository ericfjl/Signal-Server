
package org.whispersystems.textsecuregcm.wallet;

import java.security.Principal;

import javax.security.auth.Subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommResp implements Principal {

    @JsonProperty
    private String status;

    @JsonProperty
    private Object data;

    public CommResp() {

    }

    public CommResp(String status,Object data)
    {
        this.status = status;
        this.data = data;
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


}
