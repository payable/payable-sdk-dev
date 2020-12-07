package com.payable.sdk;

public interface PayableProgressListener {
    void onCardInteraction(int action);
    void onPaymentAccepted(PayableSale payableSale);
}