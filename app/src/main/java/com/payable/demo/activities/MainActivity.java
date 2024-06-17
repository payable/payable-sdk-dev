package com.payable.demo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.payable.demo.R;
import com.payable.sdk.AmountInputFilter;
import com.payable.sdk.Payable;
import com.payable.sdk.PayableEventListener;
import com.payable.sdk.PayableListener;
import com.payable.sdk.PayableProfile;
import com.payable.sdk.PayableProgressListener;
import com.payable.sdk.PayableResponse;
import com.payable.sdk.PayableSale;
import com.payable.sdk.PayableTxStatusResponse;
import com.payable.sdk.PayableTxStatusResponseV2;
import com.payable.sdk.Picker;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PayableListener {

    EditText edtAmount, edtTracking, edtEmail, edtSMS, edtTxnId, edtOrderId;
    Button btnPayCard, btnPayWallet, btnPay, btnProfile, btnVoid, btnStatus, btnStatusV2;
    TextView txtResponse, actTitle;

    double saleAmount = 0;
    String selectedProfile;

    // 1. Declare Payable Client
    Payable payableClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        edtTracking = findViewById(R.id.edtTracking);
        edtEmail = findViewById(R.id.edtEmail);
        edtSMS = findViewById(R.id.edtSMS);
        edtTxnId = findViewById(R.id.edtTxnId);
        edtOrderId = findViewById(R.id.edtOrderId);
        btnPayCard = findViewById(R.id.btnPayCard);
        btnPayWallet = findViewById(R.id.btnPayWallet);
        btnPay = findViewById(R.id.btnPay);
        btnProfile = findViewById(R.id.btnProfile);
        btnVoid = findViewById(R.id.btnVoid);
        btnStatus = findViewById(R.id.btnStatus);
        btnStatusV2 = findViewById(R.id.btnStatusV2);
        txtResponse = findViewById(R.id.txtResponse);
        actTitle = findViewById(R.id.actTitle);
        actTitle.setText("Main Activity");

        edtAmount.setFilters(AmountInputFilter.getFilter(this, 100000));

        // 2. Set Payable Client
        payableClient = Payable.createPayableClient(this, "1452", "FOOD_COURT", "C6DFA0B215B2CF24EF04794F718A3FC8");

        btnPayCard.setOnClickListener(v -> {

            hideSoftKeyboard(edtAmount);

            // 3. Call your method
            payableSale(Payable.METHOD_CARD);
        });

        btnPayWallet.setOnClickListener(v -> {

            hideSoftKeyboard(edtAmount);

            // 3. Call your method
            payableSale(Payable.METHOD_WALLET);
        });

        btnPay.setOnClickListener(v -> payableSale(Payable.METHOD_ANY));

        btnProfile.setOnClickListener(v -> payableClient.requestProfileList());

        /**
         * Advanced Usage (Optional):
         * If you want to receive the progress updates of the ongoing payment, you need to register a progress listener
         * and make sure you unregister the listener using unregisterProgressListener() method on activity onDestroy() method
         *
         */
        payableClient.registerProgressListener(new PayableProgressListener() {

            @Override
            public void onCardInteraction(int action, PayableSale payableSale) {
                Log.e("TEST_IMPL", "background: onCardInteraction: " + action + " => " + payableSale.toString());
                updateTxtResponse("background: onCardInteraction => " + action);
                Toast.makeText(getApplicationContext(), "background: onCardInteraction", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaymentAccepted(PayableSale payableSale) {
                Log.e("TEST_IMPL", "background: onPaymentAccepted: " + payableSale.toString());
                updateTxtResponse("background: onPaymentAccepted => " + payableSale.getTxnTypeName());
                Toast.makeText(getApplicationContext(), "background: onPaymentAccepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaymentRejected(PayableSale payableSale) {
                Log.e("TEST_IMPL", "background: onPaymentRejected => " + payableSale.toString());
                updateTxtResponse("background: onPaymentRejected: " + payableSale.getMessage());
                Toast.makeText(getApplicationContext(), "background: onPaymentRejected", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Advanced Usage (Optional):
         * If you want to make any requests to PAYable and get responses, register the event listener
         * and make sure you unregister the listener using unregisterEventListener() method on activity onDestroy() method
         *
         */
        payableClient.registerEventListener(new PayableEventListener() {
            @Override
            public void onProfileList(final List<PayableProfile> payableProfiles) {

                for (PayableProfile payableProfile : payableProfiles) {
                    updateFreshTxtResponse("tid: " + payableProfile.tid + " " + payableProfile.currency + " name: " + payableProfile.name + " inst: " + payableProfile.installment);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Select Profile");

                String[] profileNames = new String[payableProfiles.size()];
                for (int i = 0; i < payableProfiles.size(); i++) {
                    profileNames[i] = "tid: " + payableProfiles.get(i).tid + " " + payableProfiles.get(i).currency + " : name: " + payableProfiles.get(i).name + " inst: " + payableProfiles.get(i).installment;
                }

                builder.setItems(profileNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedProfile = payableProfiles.get(which).tid;
                        btnProfile.setText("Selected Profile: " + selectedProfile);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onVoid(PayableResponse payableResponse) {
                updateFreshTxtResponse("onVoid: " + payableResponse.status + " txId: " + payableResponse.txId + " error: " + payableResponse.error);
            }

            @Override
            public void onTransactionStatus(PayableTxStatusResponse payableResponse) {
                if (payableResponse.error != null) {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.status + " txId: " + payableResponse.txId + " error: " + payableResponse.error);
                } else {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.toFormattedString());
                }
            }

            @Override
            public void onTransactionStatusV2(PayableTxStatusResponseV2 payableResponse) {
                if (payableResponse.error != null) {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.status + " txId: " + " error: " + payableResponse.error);
                } else {
                    updateFreshTxtResponse("onTransactionStatus: " + payableResponse.toFormattedString());
                }
            }
        });

        btnVoid.setOnClickListener(v -> {
            if (!edtTxnId.getText().toString().isEmpty()) {
                Picker.cardTypePicker(MainActivity.this, cardType -> payableClient.requestVoid(edtTxnId.getText().toString(), cardType));
            }
        });

        btnStatus.setOnClickListener(v -> {
            if (!edtTxnId.getText().toString().isEmpty()) {
                Picker.cardTypePicker(MainActivity.this, cardType -> payableClient.requestTransactionStatus(edtTxnId.getText().toString(), cardType));
            }
        });

        btnStatusV2.setOnClickListener(v -> {
            if (!edtOrderId.getText().toString().isEmpty()) {
                Picker.cardTypePicker(MainActivity.this, cardType -> payableClient.requestTransactionStatusV2(edtOrderId.getText().toString(), cardType));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        payableClient.unregisterProgressListener();
        payableClient.unregisterEventListener();
    }

    private void payableSale(int paymentMethod) {

        if (edtAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Amount is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Convert sale amount to double from EditText
        saleAmount = Double.parseDouble(edtAmount.getText().toString());

        // 5. start the payment request to PAYable app with the callback listener
        PayableSale payableSale = new PayableSale(saleAmount, paymentMethod);

        if (!edtEmail.getText().toString().isEmpty()) {
            payableSale.setReceiptEmail(edtEmail.getText().toString());
        }

        if (!edtSMS.getText().toString().isEmpty()) {
            payableSale.setReceiptSMS(edtSMS.getText().toString());
        }

        if (!edtTracking.getText().toString().isEmpty()) {
            payableSale.setOrderTracking(edtTracking.getText().toString());
        }

        if (selectedProfile != null) {
            payableSale.setTerminalId(selectedProfile);
        }

        payableClient.startPayment(payableSale, this);

        // Deprecated implementations
        // payableClient.startPayment(saleAmount, paymentMethod, "{ \"ORDER_TRACKING\" : \"SDK-TEST\" }", this);
        // payableClient.startPayment(saleAmount, paymentMethod, this);
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
        txtResponse.setText("foreground: onPaymentStart => " + payableSale.getSaleAmount());
        return true;
    }

    // 8. onPaymentSuccess method
    @Override
    public void onPaymentSuccess(PayableSale payableSale) {
        updateTxtResponse("foreground: onPaymentSuccess => " + payableSale.getTxId());
        updateTxtResponse(payableSale);

        edtTxnId.setText(payableSale.getTxId());
    }

    // 9. onPaymentFailure method
    @Override
    public void onPaymentFailure(PayableSale payableSale) {
        updateTxtResponse("foreground: onPaymentFailure => " + payableSale.getMessage());
        updateTxtResponse(payableSale);
    }

    // 10. Update..
    private void updateTxtResponse(PayableSale payableSale) {

        String responseText = "\nstatusCode: " + payableSale.getStatusCode() + "\n";
        responseText += "responseAmount: " + payableSale.getSaleAmount() + "\n";
        responseText += "ccLast4: " + payableSale.getCcLast4() + "\n";
        responseText += "cardNo: " + payableSale.getCardNo() + "\n";
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
        responseText += "orderTracking: " + payableSale.getOrderTracking() + "\n";

        updateTxtResponse(responseText);
    }

    private void updateTxtResponse(String message) {
        txtResponse.setText(txtResponse.getText().toString() + "\n" + message);
    }

    private void updateFreshTxtResponse(String message) {
        txtResponse.setText("");
        updateTxtResponse(message);
    }

    protected void hideSoftKeyboard(EditText input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
}
