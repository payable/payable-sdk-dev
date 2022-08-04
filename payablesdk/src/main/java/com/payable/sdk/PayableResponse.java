package com.payable.sdk;

import com.google.gson.annotations.Expose;

public class PayableResponse {

    @Expose
    public int status;

    @Expose
    public String txId;

    @Expose
    public String error;

    protected final String maskedCardNo(String lastFour, String zeroToSix) {

        String maskedNo = "XXXX-XXXX-XXXX-" + (lastFour == null ? "XXXX" : lastFour);

        try {
            String zeroToFour = zeroToSix.substring(0, 4);
            String fourToSix = zeroToSix.substring(4, 6);
            int cardType = getCardType(zeroToSix);
            maskedNo = zeroToFour + "-" + fourToSix + (cardType == Payable.CARD_TYPE_AMEX ? "XXXX-X" : "XX-XXXX-") + lastFour;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return maskedNo;
    }

    private int getCardType(String zeroToSix) {

        String firstFourDigit = zeroToSix.substring(0, 4);
        int maskedPanNumeric = Integer.parseInt(firstFourDigit);
        int firstDigit = Integer.parseInt(zeroToSix.substring(0, 1));
        int firstTwoDigits = Integer.parseInt(zeroToSix.substring(0, 2));
        int firstThreeDigits = Integer.parseInt(zeroToSix.substring(0, 3));

        if (firstTwoDigits == 34 || firstTwoDigits == 37) {
            return Payable.CARD_TYPE_AMEX;
        }

        if (firstDigit == 4) {
            return Payable.CARD_TYPE_VISA;
        }

        if ((((firstThreeDigits >= 300 && firstThreeDigits <= 305) || firstThreeDigits == 3095 ||
                firstTwoDigits == 36 || firstTwoDigits == 38 || firstTwoDigits == 39))) {
            return Payable.CARD_TYPE_DINERS;
        }

        if ((maskedPanNumeric >= 2221 && maskedPanNumeric <= 2720) ||
                (firstTwoDigits >= 51 && firstTwoDigits <= 55)) {
            return Payable.CARD_TYPE_MASTER;
        }

        if (firstTwoDigits == 62 || firstTwoDigits == 81) {
            return Payable.CARD_TYPE_CUP;
        }

        if (maskedPanNumeric >= 3528 && maskedPanNumeric <= 3589) {
            return Payable.CARD_TYPE_JCB;
        }

        return Payable.CARD_TYPE_OTHER;
    }
}
