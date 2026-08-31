package com.payable.sdk;

import com.google.gson.annotations.Expose;

/**
 * Optional filters for {@link Payable#requestSettlementHistory(PayableSettlementFilter)}.
 * Every boxed field is optional - leave it null and the POS app ignores it.
 */
public class PayableSettlementFilter {

    @Expose
    public int pageId;

    @Expose
    public int pageSize;

    @Expose
    public Long profileId;

    @Expose
    public String startDate;

    @Expose
    public String endDate;

    @Expose
    public String tid;

    @Expose
    public Integer currency;

    @Expose
    public Integer installment;

    @Expose
    public Integer isDcc;

    @Expose
    public Integer batchNo;

    public PayableSettlementFilter() {
    }

    public PayableSettlementFilter(int pageId, int pageSize) {
        this.pageId = pageId;
        this.pageSize = pageSize;
    }

    public PayableSettlementFilter setPageId(int pageId) {
        this.pageId = pageId;
        return this;
    }

    public PayableSettlementFilter setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PayableSettlementFilter setProfileId(Long profileId) {
        this.profileId = profileId;
        return this;
    }

    public PayableSettlementFilter setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public PayableSettlementFilter setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public PayableSettlementFilter setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public PayableSettlementFilter setCurrency(Integer currency) {
        this.currency = currency;
        return this;
    }

    public PayableSettlementFilter setInstallment(Integer installment) {
        this.installment = installment;
        return this;
    }

    public PayableSettlementFilter setIsDcc(Integer isDcc) {
        this.isDcc = isDcc;
        return this;
    }

    public PayableSettlementFilter setBatchNo(Integer batchNo) {
        this.batchNo = batchNo;
        return this;
    }
}
