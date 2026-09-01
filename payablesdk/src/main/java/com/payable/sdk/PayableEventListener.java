package com.payable.sdk;

import java.util.List;

public interface PayableEventListener {

    void onProfileList(List<PayableProfile> payableProfiles);

    void onVoid(PayableResponse payableResponse);

    void onTransactionStatus(PayableTxStatusResponse payableResponse);

    void onTransactionStatusV2(PayableTxStatusResponseV2 payableResponse);

    default void onSettlementHistory(PayableSettlementHistoryResponse payableResponse) {
    }

    /** Optional - the last 3 days of closed transactions. */
    default void onTransactionHistory(PayableTransactionHistoryResponse payableResponse) {
    }

    default void onLatestReversalRecord(PayableReversalRecordResponse payableResponse) {
    }

    default void onForceReversal(PayableForceReversalResponse payableResponse) {
    }
}
