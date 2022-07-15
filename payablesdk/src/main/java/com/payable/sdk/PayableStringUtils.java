package com.payable.sdk;

public class PayableStringUtils {

    public static String cardTypeToString(int cardType) {

        if (cardType == Payable.CARD_TYPE_VISA) {
            return "VISA";
        } else if (cardType == Payable.CARD_TYPE_MASTER) {
            return "MASTER";
        } else if (cardType == Payable.CARD_TYPE_AMEX) {
            return "AMEX";
        } else if (cardType == Payable.CARD_TYPE_CUP) {
            return "CUP";
        } else if (cardType == Payable.CARD_TYPE_JCB) {
            return "JCB";
        } else {
            return null;
        }
    }

    public static int cardTypeInt(String cardType) {

        if (cardType == null) return 0;

        if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_VISA))) {
            return Payable.CARD_TYPE_VISA;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_MASTER))) {
            return Payable.CARD_TYPE_MASTER;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_AMEX))) {
            return Payable.CARD_TYPE_AMEX;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_CUP))) {
            return Payable.CARD_TYPE_CUP;
        } else if (cardType.equals(cardTypeToString(Payable.CARD_TYPE_JCB))) {
            return Payable.CARD_TYPE_JCB;
        } else {
            return 0;
        }
    }
}
