package com.payable.sdk;

import com.google.gson.annotations.Expose;

public class PayableForceReversalResponse extends PayableResponse {

    public static final String REVERSAL_COMPLETED = "REVERSAL_COMPLETED";

    @Expose
    public String reversalHistoryId;

    @Expose
    public String reversalStatus;

    @Expose
    public String nacResponseCode;

    @Expose
    public String retrievalRefNo;

    @Expose
    public String authIdResponseCode;

    @Expose
    public String ccLast4;

    @Expose
    public String amount;

    @Expose
    public int currency;

    @Expose
    public String invoiceNo;

    public boolean isCompleted() {
        return REVERSAL_COMPLETED.equalsIgnoreCase(reversalStatus);
    }

    @Override
    public String toString() {
        return "PayableForceReversalResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", reversalHistoryId='" + reversalHistoryId + '\'' +
                ", reversalStatus='" + reversalStatus + '\'' +
                ", nacResponseCode='" + nacResponseCode + '\'' +
                ", retrievalRefNo='" + retrievalRefNo + '\'' +
                ", authIdResponseCode='" + authIdResponseCode + '\'' +
                ", ccLast4='" + ccLast4 + '\'' +
                ", amount='" + amount + '\'' +
                ", currency=" + currency +
                ", invoiceNo='" + invoiceNo + '\'' +
                '}';
    }

    public String toFormattedString() {
        return "\n\n"
                + "Status: " + status + "\n"
                + "Error: " + error + "\n"
                + "Reversal ID: " + reversalHistoryId + "\n"
                + "Reversal status: " + reversalStatus + "\n"
                + "Completed: " + isCompleted() + "\n"
                + "Amount: " + PayableStringUtils.currencyToString(currency) + " " + amount + "\n"
                + "Card last 4 digits: " + ccLast4 + "\n"
                + "Invoice no: " + invoiceNo + "\n"
                + "RRN: " + retrievalRefNo + "\n"
                + "Auth ID: " + authIdResponseCode + "\n"
                + "NAC response code: " + nacResponseCode + "\n";
    }
}
