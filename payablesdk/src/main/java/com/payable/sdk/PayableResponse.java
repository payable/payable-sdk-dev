package com.payable.sdk;

import com.google.gson.annotations.Expose;

public class PayableResponse {

    @Expose
    public int status;

    @Expose
    public String txId;

    @Expose
    public String error;
}
