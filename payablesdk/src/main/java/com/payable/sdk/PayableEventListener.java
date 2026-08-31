package com.payable.sdk;

import java.util.List;

public interface PayableEventListener {

    void onProfileList(List<PayableProfile> payableProfiles);

    void onVoid(PayableResponse payableResponse);

    void onTransactionStatus(PayableTxStatusResponse payableResponse);

    void onTransactionStatusV2(PayableTxStatusResponseV2 payableResponse);

    default void onSettlementHistory(PayableSettlementHistoryResponse payableResponse) {
    }

    default void onLatestReversalRecord(PayableReversalRecordResponse payableResponse) {
    }

    default void onForceReversal(PayableForceReversalResponse payableResponse) {
    }
}
