package com.payabledemo.com.payabledemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Payable {

    EditText edtAmount;
    Button btnPay;
    TextView txtResponse;

    double saleAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnPay = findViewById(R.id.btnPay);
        txtResponse = findViewById(R.id.txtResponse);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payableSale();
            }
        });
    }

    private void payableSale() {

        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        if(saleAmount == 789) {
            Intent in = new Intent(this, InsideActivity.class);
            startActivity(in);
            return;
        }

        setClientId("566");
        setClientName("Daraz");

        startPayment(saleAmount, new PayableListener() {
            @Override
            public void onPaymentSuccess(Payable payable) {
                updateMyUI(payable);
            }

            @Override
            public void onPaymentFailure(Payable payable) {

                if(payable.getStatusCode() == PAYABLE_APP_NOT_INSTALLED) {
                    Toast.makeText(getApplicationContext(), "PAYABLE_APP_NOT_INSTALLED", Toast.LENGTH_LONG).show();
                }
                else {
                    updateMyUI(payable);
                }
            }
        });
    }

    private void updateMyUI(Payable payable) {

        String responseText = "statusCode: " + payable.getStatusCode() + "\n";
        responseText += "responseAmount: " + payable.getSaleAmount() + "\n";
        responseText += "ccLast4: " + payable.getCcLast4() + "\n";
        responseText += "cardType: " + payable.getCardType() + "\n";
        responseText += "txId: " + payable.getTxId() + "\n";
        responseText += "terminalId: " + payable.getTerminalId() + "\n";
        responseText += "mid: " + payable.getMid() + "\n";
        responseText += "isEmv: " + payable.getIsEmv() + "\n";
        responseText += "txnStatus: " + payable.getTxnStatus() + "\n";

        txtResponse.setText(responseText);

    }
}
