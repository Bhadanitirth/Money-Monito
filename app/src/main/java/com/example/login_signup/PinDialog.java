package com.example.login_signup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PinDialog {

    private Context context;
    private StringBuilder enteredPin;
    private PinDialogListener pinDialogListener;
    String phone;
    EditText pinEditText;
    TextView sdptxt;

    public PinDialog(Context context, PinDialogListener listener, String phone) {
        this.context = context;
        this.enteredPin = new StringBuilder();
        this.pinDialogListener = listener;
        this.phone=phone;
    }

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter PIN");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_enter_pin, null);
        builder.setView(dialogView);

        pinEditText = dialogView.findViewById(R.id.editTextPin);
        sdptxt = dialogView.findViewById(R.id.sdptxt);
        sdptxt.setText(phone);


        GridLayout gridLayout = dialogView.findViewById(R.id.numberGridLayout);

        Button b0 = dialogView.findViewById(R.id.b0);
        b0.setOnClickListener(v -> {
            if (enteredPin.length() < 4) {
                enteredPin.append("0");
                pinEditText.setText(enteredPin.toString());
            }
        });

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setOnClickListener(v -> {

                if (enteredPin.length() < 4) {
                    enteredPin.append(button.getText().toString());
                    pinEditText.setText(enteredPin.toString());
                }
            });
        }


        ImageButton clearButton = dialogView.findViewById(R.id.Clear_text);
        clearButton.setOnClickListener(v -> {
            if (enteredPin.length() > 0) {
                enteredPin.deleteCharAt(enteredPin.length() - 1);
                pinEditText.setText(enteredPin.toString());
            }
        });

               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (enteredPin.length() == 4) {

                    pinDialogListener.onPinEntered(enteredPin.toString());
                } else {
                    Toast.makeText(context, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enteredPin.setLength(0);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public interface PinDialogListener {
        void onPinEntered(String pin);
    }
}
