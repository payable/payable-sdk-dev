package com.payable.sdk;

import com.google.gson.annotations.Expose;

public class PayableTxStatusResponse extends PayableResponse {

    @Expose
    public String cardHolder;

    @Expose
    public String ccLast4;

    @Expose
    public double amount;

    @Expose
    public int cardType;

    @Expose
    public String time;

    @Expose
    public String approvalCode;

    @Expose
    public int sigFlag;

    @Expose
    public int batchNo;

    @Expose
    public String merchantInvoiceId;

    @Expose
    public int txType;

    @Expose
    public int currencyType;

    @Expose
    public int installment;

    @Expose
    public String rrn;

    @Expose
    public int cvm;

    @Expose
    public String tid;

    @Expose
    public String mid;

    @Expose
    public int stn;

    @Expose
    public String aid;

    @Expose
    public String applable;

    @Expose
    public String bin;

    public String maskedCardNo() {
        return super.maskedCardNo(ccLast4, bin);
    }

    @Override
    public String toString() {
        return "PayableTxStatusResponse {" +
                "\nstatus=" + status +
                ", \ntxId='" + txId + '\'' +
                ", \nerror='" + error + '\'' +
                ", \ncardHolder='" + cardHolder + '\'' +
                ", \nccLast4='" + ccLast4 + '\'' +
                ", \namount=" + amount +
                ", \ncardType=" + cardType +
                ", \ntime='" + time + '\'' +
                ", \napprovalCode='" + approvalCode + '\'' +
                ", \nsigFlag=" + sigFlag +
                ", \nbatchNo=" + batchNo +
                ", \nmerchantInvoiceId='" + merchantInvoiceId + '\'' +
                ", \ntxType=" + txType +
                ", \ncurrencyType=" + currencyType +
                ", \ninstallment=" + installment +
                ", \nrrn='" + rrn + '\'' +
                ", \ncvm=" + cvm +
                ", \ntid='" + tid + '\'' +
                ", \nmid='" + mid + '\'' +
                ", \nstn=" + stn +
                ", \naid='" + aid + '\'' +
                ", \napplable='" + applable + '\'' +
                ", \nbin='" + bin + '\'' +
                ", \nmaskedCardNo='" + maskedCardNo() + '\'' +
                "\n}";
    }
}
