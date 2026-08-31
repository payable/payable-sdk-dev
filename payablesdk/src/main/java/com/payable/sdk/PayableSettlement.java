package com.payable.sdk;

import com.google.gson.annotations.Expose;

/**
 * A single settled batch returned by {@link Payable#requestSettlementHistory(PayableSettlementFilter)}.
 */
public class PayableSettlement {

    @Expose
    public String id;

    @Expose
    public long profileId;

    @Expose
    public int batchNo;

    @Expose
    public String tid;

    @Expose
    public String mid;

    @Expose
    public int currency;

    @Expose
    public int installment;

    @Expose
    public int isDcc;

    @Expose
    public int txCount;

    @Expose
    public double totalAmount;

    @Expose
    public String startDate;

    @Expose
    public String endDate;

    @Expose
    public String settledDate;

    @Expose
    public String printedDate;

    @Override
    public String toString() {
        return "PayableSettlement{" +
                "id='" + id + '\'' +
                ", profileId=" + profileId +
                ", batchNo=" + batchNo +
                ", tid='" + tid + '\'' +
                ", mid='" + mid + '\'' +
                ", currency=" + currency +
                ", installment=" + installment +
                ", isDcc=" + isDcc +
                ", txCount=" + txCount +
                ", totalAmount=" + totalAmount +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", settledDate='" + settledDate + '\'' +
                ", printedDate='" + printedDate + '\'' +
                '}';
    }

    public String toFormattedString() {
        return "\n"
                + "Batch no: " + batchNo + "\n"
                + "TID: " + tid + "\n"
                + "MID: " + mid + "\n"
                + "Transactions: " + txCount + "\n"
                + "Total amount: " + PayableStringUtils.currencyToString(currency) + " " + totalAmount + "\n"
                + "Installment: " + installment + "\n"
                + "DCC: " + isDcc + "\n"
                + "From: " + startDate + "\n"
                + "To: " + endDate + "\n"
                + "Settled date: " + settledDate + "\n"
                + "Printed date: " + printedDate + "\n";
    }
}
