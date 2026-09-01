package com.payable.sdk;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PayableTransactionHistoryResponse extends PayableResponse {

    @Expose
    public List<PayableTransaction> transactions;

    @Expose
    public int pageId;

    /**
     * True when there is at least one more page to ask for.
     */
    @Expose
    public boolean hasMore;

    /**
     * Start of the window the POS app actually used, always 3 days back. Informational only -
     * it cannot be set from the SDK.
     */
    @Expose
    public String fromDate;

    /**
     * End of the window the POS app actually used.
     */
    @Expose
    public String toDate;

    @Override
    public String toString() {
        return "PayableTransactionHistoryResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", pageId=" + pageId +
                ", hasMore=" + hasMore +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", transactions=" + transactions +
                '}';
    }

    public String toFormattedString() {

        StringBuilder builder = new StringBuilder("\n\n"
                + "Status: " + status + "\n"
                + "Error: " + error + "\n"
                + "Page: " + pageId + "\n"
                + "Has more: " + hasMore + "\n"
                + "From: " + fromDate + "\n"
                + "To: " + toDate + "\n"
                + "Transactions: " + (transactions == null ? 0 : transactions.size()) + "\n");

        if (transactions != null) {
            for (PayableTransaction transaction : transactions) {
                builder.append(transaction.toFormattedString());
            }
        }

        return builder.toString();
    }
}
