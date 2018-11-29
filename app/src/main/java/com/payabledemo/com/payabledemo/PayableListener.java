package com.payabledemo.com.payabledemo;

public interface PayableListener {
    void onPaymentSuccess(Payable payable);
    void onPaymentFailure(Payable payable);
}
