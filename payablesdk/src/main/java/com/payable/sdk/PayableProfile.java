package com.payable.sdk;

import com.google.gson.annotations.Expose;

public class PayableProfile {

    @Expose
    public String tid;

    @Expose
    public String name;

    @Expose
    public String currency;

    @Expose
    public Integer installment;
}
