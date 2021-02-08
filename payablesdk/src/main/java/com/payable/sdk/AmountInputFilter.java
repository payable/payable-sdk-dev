package com.payable.sdk;


import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountInputFilter implements InputFilter {

    Pattern pattern;
    private double maxAmount;
    private Context context;
    private Listener eventListener;

    public interface Listener {
        void onMaxReached(double amount);
    }

    public AmountInputFilter(Context context, int digitsBeforeDecimal, int digitsAfterDecimal, double maxAmount, Listener eventListener) {
        this.maxAmount = maxAmount;
        this.context = context;
        this.eventListener = eventListener;
        pattern = Pattern.compile("(([1-9]{1}[0-9]{0," + (digitsBeforeDecimal - 1) + "})?||[0]{1})((\\.[0-9]{0," + digitsAfterDecimal + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd) {

        // Remove the string out of destination that is to be replaced.
        String newString = destination.toString().substring(0, destinationStart) + destination.toString().substring(destinationEnd, destination.toString().length());

        // Add the new string in.
        newString = newString.substring(0, destinationStart) + source.toString() + newString.substring(destinationStart, newString.length());

        // Now check if the new string is valid.
        Matcher matcher = pattern.matcher(newString);

        // Max amount check
        if (newString.equals(".")) {
            return "";
        }

        if (!source.equals(".") && !isNumeric(newString)) {
            return "";
        }

        if (!newString.isEmpty() && !source.equals(".") && Double.parseDouble(newString) > maxAmount) {
            if (eventListener != null) {
                eventListener.onMaxReached(maxAmount);
            }
            return "";
        }

        if (matcher.matches()) {
            // Returning null indicates that the input is valid.
            return null;
        }

        // Returning the empty string indicates the input is invalid.
        return "";
    }

    public static boolean isNumeric(String string) {
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }

    public static InputFilter[] getFilter(Context context, double maxAmount, Listener eventListener) {
        return new InputFilter[]{
                new AmountInputFilter(context, 10, 2, maxAmount, eventListener)
        };
    }

    public static InputFilter[] getFilter(Context context, double maxAmount) {
        return AmountInputFilter.getFilter(context, maxAmount, null);
    }
}