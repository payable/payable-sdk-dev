package com.payable.sdk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

public class Picker {

    public interface ProfilePickerListener {

        void onProfileSelected(PayableProfile payableProfile);

        String onRow(PayableProfile payableProfile);
    }

    public static void profilePicker(Context context, String title, final List<PayableProfile> payableProfiles, final ProfilePickerListener profilePickerListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        String[] profileNames = new String[payableProfiles.size()];

        for (int i = 0; i < payableProfiles.size(); i++) {
            profileNames[i] = profilePickerListener.onRow(payableProfiles.get(i));
        }

        builder.setItems(profileNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                profilePickerListener.onProfileSelected(payableProfiles.get(which));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void profilePicker(Context context, List<PayableProfile> payableProfiles, ProfilePickerListener profilePickerListener) {
        profilePicker(context, "Select Profile", payableProfiles, profilePickerListener);
    }
}
