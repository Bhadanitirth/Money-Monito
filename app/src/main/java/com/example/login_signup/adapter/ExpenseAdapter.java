package com.example.login_signup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_signup.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final Context context;
    private final ArrayList<HashMap<String, Object>> expenseList;

    private final String[] categoryNames = {
            "Other", "Food", "Shopping", "Travelling", "Medical", "Education",
            "Rent", "Entertainment", "Personal Care", "Gift and Donation"
    };
    private final int[] categoryImages = {
            R.drawable.other, R.drawable.food, R.drawable.shopping, R.drawable.travel,
            R.drawable.medical, R.drawable.education, R.drawable.rent, R.drawable.game,
            R.drawable.personal_care, R.drawable.gift_and_donation
    };

    public ExpenseAdapter(Context context, ArrayList<HashMap<String, Object>> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_analysis, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        HashMap<String, Object> expense = expenseList.get(position);
        String category = expense.get("expense_category").toString();
        holder.amount.setText("₹" + expense.get("amount").toString());
        holder.category.setText(category);

        int imageResId = getCategoryImage(category);
        holder.expenseImage.setImageResource(imageResId);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    private int getCategoryImage(String category) {
        for (int i = 0; i < categoryNames.length; i++) {
            if (categoryNames[i].equalsIgnoreCase(category)) {
                return categoryImages[i];
            }
        }
        return R.drawable.other;
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        final TextView amount;
        final TextView category;
        final ImageView expenseImage;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.category);
            expenseImage = itemView.findViewById(R.id.expense_image);
        }
    }
}
