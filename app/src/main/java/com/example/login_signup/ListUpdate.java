package com.example.login_signup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.databinding.ListUpdateBinding;
import com.example.login_signup.fragments.Expense;
import com.example.login_signup.fragments.Income;
import com.example.login_signup.fragments.Transfer;

public class ListUpdate extends AppCompatActivity {

    ImageView back, delete;
    FinancialDB dbHelper;
    String id, type;
    ListUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the database helper
        dbHelper = new FinancialDB(this);

        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        if (savedInstanceState == null) {
            switch (type) {
                case "expense":
                    replaceFragment(new Expense(id));
                    break;
                case "income":
                    replaceFragment(new Income(id));
                    break;
                case "transfer":
                    replaceFragment(new Transfer(id));
                    break;
                default:
                    replaceFragment(new Expense(id));
                    break;
            }
        }

        back = findViewById(R.id.back);
        delete = findViewById(R.id.delet);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBackConfirmationDialog();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int expenseId = Integer.parseInt(id);
                        int count = dbHelper.delete(expenseId);

                        if (count > 0) {
                            Toast.makeText(ListUpdate.this, "Delete done", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ListUpdate.this, Before_Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ListUpdate.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showBackConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Do you really want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ListUpdate.this, Before_Home.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
