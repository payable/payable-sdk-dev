package com.payabledemo.com.payabledemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsideActivity extends AppCompatActivity implements PayableListener {

    private static final String TAG = "PAYable_Demo";
    private static final int PAYABLE_REQUEST_CODE = 145;

    EditText edtAmount;
    Button btnPay;
    TextView txtResponse;

    double saleAmount = 0;

    Payable payableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnPay = findViewById(R.id.btnPay);
        txtResponse = findViewById(R.id.txtResponse);

        payableClient = new Payable();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payableSale();
            }
        });

    }

    private void payableSale() {

        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        payableClient.setClientName("NAME");
        payableClient.setClientId("456");
        payableClient.setSaleAmount(saleAmount);

        try {
            startActivityForResult(payableClient.getPaymentIntent(), Payable.PAYABLE_REQUEST_CODE);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "PAYABLE_APP_NOT_INSTALLED", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Payable.PAYABLE_REQUEST_CODE) {
            if (payableClient != null) {
                payableClient.setResponseCallback(data, this);
            }
        }
    }

    @Override
    public void onPaymentSuccess(Payable payable) {
        updateMyUI(payable);
    }

    @Override
    public void onPaymentFailure(Payable payable) {
        updateMyUI(payable);
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
