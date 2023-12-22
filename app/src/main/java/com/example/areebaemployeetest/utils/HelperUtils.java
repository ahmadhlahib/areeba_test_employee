package com.example.areebaemployeetest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

public class HelperUtils {
    private Activity activity;
    public HelperUtils() {
    }

    public HelperUtils(Activity activity) {
        this.activity = activity;
    }

    public void showAlertDialog(String message)
    {
        // Create an AlertDialog.Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        // Set the title and message for the AlertDialog
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setMessage(message);
        // Set an OK button and its click listener
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action to perform when OK is clicked
                dialog.dismiss(); // Close the dialog
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static ProgressDialog GetLoadingDialog(Activity activity,
                                                  String message) {
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(message);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        return pd;
    }
}
