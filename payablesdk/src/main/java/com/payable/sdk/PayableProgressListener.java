package com.payable.sdk;

public interface PayableProgressListener {
    void onCardInteraction(int action, PayableSale payableSale);
    void onPaymentAccepted(PayableSale payableSale);
    void onPaymentRejected(PayableSale payableSale);
}