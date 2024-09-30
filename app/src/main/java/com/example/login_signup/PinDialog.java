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

    // Constructor for the PinDialog, accepting the context and listener
    public PinDialog(Context context, PinDialogListener listener, String phone) {
        this.context = context;
        this.enteredPin = new StringBuilder();
        this.pinDialogListener = listener;
        this.phone=phone;
    }

    // Method to display the dialog
    public void showDialog() {
        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter PIN");

        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_enter_pin, null);
        builder.setView(dialogView);

        // Reference to the EditText to display entered PIN
        pinEditText = dialogView.findViewById(R.id.editTextPin);
        sdptxt = dialogView.findViewById(R.id.sdptxt);
        sdptxt.setText(phone);

        // GridLayout containing number buttons
        GridLayout gridLayout = dialogView.findViewById(R.id.numberGridLayout);

        // Handle the 'b0' button outside the GridLayout
        Button b0 = dialogView.findViewById(R.id.b0);  // Assuming b0 is in the dialog layout
        b0.setOnClickListener(v -> {
            if (enteredPin.length() < 4) {
                enteredPin.append("0");  // Append '0' to the PIN
                pinEditText.setText(enteredPin.toString());  // Update EditText with PIN
            }
        });

        // Loop through the GridLayout to assign click listeners to the buttons
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) gridLayout.getChildAt(i);
            button.setOnClickListener(v -> {
                // Append the clicked button number to the entered PIN
                if (enteredPin.length() < 4) {
                    enteredPin.append(button.getText().toString());
                    pinEditText.setText(enteredPin.toString());
                }
            });
        }

        // Handle the Clear button
        ImageButton clearButton = dialogView.findViewById(R.id.Clear_text);
        clearButton.setOnClickListener(v -> {
            if (enteredPin.length() > 0) {
                enteredPin.deleteCharAt(enteredPin.length() - 1);
                pinEditText.setText(enteredPin.toString());
            }
        });

        // Set positive button (OK) to submit the PIN
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (enteredPin.length() == 4) {
                    // Call the listener's onPinEntered method and pass the entered PIN
                    pinDialogListener.onPinEntered(enteredPin.toString());
                } else {
                    Toast.makeText(context, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set negative button (Cancel) to close the dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enteredPin.setLength(0);  // Clear PIN when dialog is dismissed
                dialog.dismiss();
            }
        });

        // Show the dialog
        builder.create().show();
    }

    // Interface to handle PIN submission
    public interface PinDialogListener {
        void onPinEntered(String pin);  // Method to be called when a valid PIN is entered
    }
}
