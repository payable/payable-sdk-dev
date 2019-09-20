package com.payable.demo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.payable.demo.R;
import com.payable.sdk.Payable;
import com.payable.sdk.PayableListener;
import com.payable.sdk.PayableSale;

public class MainActivity extends AppCompatActivity implements PayableListener {

    EditText edtAmount;
    Button btnPayCard, btnPayWallet, btnPay;
    TextView txtResponse, actTitle;

    double saleAmount = 0;

    // 1. Declare Payable Client
    Payable payableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnPayCard = findViewById(R.id.btnPayCard);
        btnPayWallet = findViewById(R.id.btnPayWallet);
        btnPay = findViewById(R.id.btnPay);
        txtResponse = findViewById(R.id.txtResponse);
        actTitle = findViewById(R.id.actTitle);
        actTitle.setText("Main Activity");

        // 2. Set Payable Client
        payableClient = Payable.createPayableClient(this, "1452", "FOOD_COURT", "C6DFA0B215B2CF24EF04794F718A3FC8");

        btnPayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard(edtAmount);

                // 3. Call your method
                payableSale(Payable.METHOD_CARD);
            }
        });

        btnPayWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard(edtAmount);

                // 3. Call your method
                payableSale(Payable.METHOD_WALLET);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payableSale(Payable.METHOD_ANY);
            }
        });
    }

    private void payableSale(int paymentMethod) {

        // 4. Convert sale amount to double from EditText
        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        // 5. start the payment request to PAYable app with the callback listener { "TRACKING_NO" : "123455" }
        payableClient.startPayment(saleAmount, paymentMethod, "{ \"ORDER_TRACKING\" : \"123455\" }", this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 7. onActivityResult set the callback listener to handle the response
        payableClient.handleResponse(requestCode, data);

    }

    // 8. onPaymentSuccess method
    @Override
    public boolean onPaymentStart(PayableSale payableSale) {
        return true;
    }

    // 8. onPaymentSuccess method
    @Override
    public void onPaymentSuccess(PayableSale payableSale) {
        updateMyUI(payableSale);
    }

    // 9. onPaymentFailure method
    @Override
    public void onPaymentFailure(PayableSale payableSale) {
        updateMyUI(payableSale);
    }

    // 10. Update..
    private void updateMyUI(PayableSale payableSale) {

        String responseText = "statusCode: " + payableSale.getStatusCode() + "\n";
        responseText += "responseAmount: " + payableSale.getSaleAmount() + "\n";
        responseText += "ccLast4: " + payableSale.getCcLast4() + "\n";
        responseText += "cardType: " + payableSale.getCardType() + "\n";
        responseText += "txId: " + payableSale.getTxId() + "\n";
        responseText += "terminalId: " + payableSale.getTerminalId() + "\n";
        responseText += "mid: " + payableSale.getMid() + "\n";
        responseText += "txnType: " + payableSale.getTxnType() + "\n";
        responseText += "txnStatus: " + payableSale.getTxnStatus() + "\n";
        responseText += "receiptSMS: " + payableSale.getReceiptSMS() + "\n";
        responseText += "receiptEmail: " + payableSale.getReceiptEmail() + "\n";
        responseText += "paymentMethod: " + payableSale.getPaymentMethod() + "\n";
        responseText += "message: " + payableSale.getMessage() + "\n";

        txtResponse.setText(responseText);
    }

    protected void hideSoftKeyboard(EditText input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
}
