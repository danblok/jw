package com.example.jewelry;

import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Utils {

    public static String getTrimmedValue(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static void showAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .create().show();
    }
}
