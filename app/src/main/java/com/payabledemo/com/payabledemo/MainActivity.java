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

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends Payable {

    private static final String TAG = "PAYable_Demo";
    private static final int PAYABLE_REQUEST_CODE = 145;

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

        //if (true) return;


        /*txtResponse.setText("");

        // Do the validations
        if (edtAmount.getText() != null && !edtAmount.getText().toString().trim().isEmpty()) {

            // Get value from EditText and convert it to Double
            saleAmount = Double.parseDouble(edtAmount.getText().toString());

            // Convert to two decimal places
            saleAmount = Double.parseDouble(new DecimalFormat("0.00").format(saleAmount));

            // Call the PAYable Intent
            try {

                Intent i = new Intent(getString(R.string.payable_package));
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                i.putExtra("PAY_AMOUNT", saleAmount);
                i.putExtra("CLIENT_ID", this.getString(R.string.payable_client_id));
                i.putExtra("CLIENT_NAME", this.getString(R.string.payable_client_name));

                startActivityForResult(i, PAYABLE_REQUEST_CODE);

            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "Application not found: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }*/
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

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYABLE_REQUEST_CODE) {

            if (data != null) {

                int statusCode = data.getIntExtra("STATUS_CODE", 0);
                double responseAmount = data.getDoubleExtra("PAY_AMOUNT", 0);

                String ccLast4 = data.getStringExtra("ccLast4");
                int cardType = data.getIntExtra("cardType", 0);
                String txId = data.getStringExtra("txId");
                String terminalId = data.getStringExtra("terminalId");
                String mid = data.getStringExtra("mid");
                int isEmv = data.getIntExtra("isEmv", 0);
                int txnStatus = data.getIntExtra("txnStatus", 0);

                String responseText = "statusCode: " + statusCode + "\n";
                responseText += "responseAmount: " + responseAmount + "\n";
                responseText += "ccLast4: " + ccLast4 + "\n";
                responseText += "cardType: " + cardType + "\n";
                responseText += "txId: " + txId + "\n";
                responseText += "terminalId: " + terminalId + "\n";
                responseText += "mid: " + mid + "\n";
                responseText += "isEmv: " + isEmv + "\n";
                responseText += "txnStatus: " + txnStatus + "\n";

                txtResponse.setText(responseText);

            }

        }
    }*/
}
