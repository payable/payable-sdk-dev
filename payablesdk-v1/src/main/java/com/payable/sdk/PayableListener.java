package com.payable.sdk;

public interface PayableListener {
    boolean onPaymentStart(PayableSale payableSale);
    void onPaymentSuccess(PayableSale payableSale);
    void onPaymentFailure(PayableSale payableSale);
}
