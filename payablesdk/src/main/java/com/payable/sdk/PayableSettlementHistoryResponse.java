package com.payable.sdk;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PayableSettlementHistoryResponse extends PayableResponse {

    @Expose
    public List<PayableSettlement> settlements;

    @Expose
    public int pageId;

    /**
     * True when the requested page came back full, meaning there is at least one more page to ask for.
     */
    @Expose
    public boolean hasMore;

    @Override
    public String toString() {
        return "PayableSettlementHistoryResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", pageId=" + pageId +
                ", hasMore=" + hasMore +
                ", settlements=" + settlements +
                '}';
    }

    public String toFormattedString() {

        StringBuilder builder = new StringBuilder("\n\n"
                + "Status: " + status + "\n"
                + "Error: " + error + "\n"
                + "Page: " + pageId + "\n"
                + "Has more: " + hasMore + "\n"
                + "Settlements: " + (settlements == null ? 0 : settlements.size()) + "\n");

        if (settlements != null) {
            for (PayableSettlement settlement : settlements) {
                builder.append(settlement.toFormattedString());
            }
        }

        return builder.toString();
    }
}
