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

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, Object>> incomeList;

    private final String[] incomeCategoryNames = {
            "Other", "Salary", "Sold Items", "Coupons"
    };
    private final int[] incomeCategoryImages = {
            R.drawable.other, R.drawable.salary, R.drawable.sold, R.drawable.coupon
    };

    public IncomeAdapter(Context context, ArrayList<HashMap<String, Object>> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_analysis, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        HashMap<String, Object> income = incomeList.get(position);
        holder.amount.setText("₹" + income.get("amount").toString());
        String category =income.get("income_category").toString();
        holder.category.setText(category);

        int imageResId = getCategoryImage(category);
        holder.incomeImage.setImageResource(imageResId);
    }

    private int getCategoryImage(String category) {
        for (int i = 0; i < incomeCategoryNames.length; i++) {
            if (incomeCategoryNames[i].equalsIgnoreCase(category)) {
                return incomeCategoryImages[i];
            }
        }
        return R.drawable.other;
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    // ViewHolder class
    public class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView category;
        final ImageView incomeImage;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.category);
            incomeImage=itemView.findViewById(R.id.expense_image);
        }
    }
}
