package com.payable.sdk;

import com.google.gson.annotations.Expose;

/**
 * Paging options for {@link Payable#requestTransactionHistory(PayableTransactionFilter)}.
 * <p>
 * There is deliberately no date range here. The POS app always answers with the last 3 days of
 * closed transactions and recomputes that window itself on every request, so the range cannot be
 * widened from the SDK.
 */
public class PayableTransactionFilter {

    @Expose
    public int pageId;

    @Expose
    public int pageSize;

    public PayableTransactionFilter() {
    }

    public PayableTransactionFilter(int pageId, int pageSize) {
        this.pageId = pageId;
        this.pageSize = pageSize;
    }

    public PayableTransactionFilter setPageId(int pageId) {
        this.pageId = pageId;
        return this;
    }

    /**
     * Rows per page. The POS app defaults this to 20 when it is not set and caps it at 100.
     */
    public PayableTransactionFilter setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
