package com.example.login_signup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_signup.ListUpdate;
import com.example.login_signup.Premium;
import com.example.login_signup.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private ArrayList<HashMap<String, String>> originalData;
    private ArrayList<HashMap<String, String>> filteredData;
    private final ItemFilter filter = new ItemFilter();

    private final String[] expenseCategoryNames = {
            "Other", "Food", "Shopping", "Travelling", "Medical", "Education",
            "Rent", "Entertainment", "Personal Care", "Gift and Donation"
    };
    private final int[] expenseCategoryImages = {
            R.drawable.other, R.drawable.food, R.drawable.shopping, R.drawable.travel,
            R.drawable.medical, R.drawable.education, R.drawable.rent, R.drawable.game,
            R.drawable.personal_care, R.drawable.gift_and_donation
    };

    private final String[] incomeCategoryNames = {
            "Other", "Salary", "Sold Items", "Coupons"
    };
    private final int[] incomeCategoryImages = {
            R.drawable.other, R.drawable.salary, R.drawable.sold, R.drawable.coupon
    };

    private final String[] PaymentMethodNames = {
            "Cash", "Bank Account"
    };
    private final int[] PaymentMethodImages = {
            R.drawable.cash, R.drawable.bank
    };

    public CustomAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> item = filteredData.get(position);

        holder.itemAmount.setText(item.get("amount"));
        holder.itemNote.setText(item.get("note"));
        holder.date.setText(item.get("date"));

        String category_expense = item.get("expense_category");
        if (category_expense != null) {
            int imageResource = getCategoryImageResourceExpense(category_expense);
            holder.itemImage.setImageResource(imageResource);
        } else {
            holder.itemImage.setImageResource(R.drawable.other);
        }

        String category_income = item.get("income_category");
        if (category_income != null) {
            int imageResource = getCategoryImageResourceIncome(category_income);
            holder.itemImage.setImageResource(imageResource);
        }

        String pay_type = item.get("expense_payment_method");
        if (pay_type != null) {
            int imageResource = getImageResourceIncome(pay_type);
            holder.payImage.setImageResource(imageResource);
        } else {
            holder.payImage.setImageResource(R.drawable.other);
        }

        pay_type = item.get("income_payment_method");
        if (pay_type != null) {
            int imageResource = getImageResourceIncome(pay_type);
            holder.payImage.setImageResource(imageResource);
        }

        String fromAccount = item.get("from_account");
        if (fromAccount != null) {
            int imageResource = getImageResourceIncome(fromAccount);
            holder.payImage.setImageResource(imageResource);
            holder.itemImage.setImageResource(R.drawable.transfer);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListUpdate.class);
            intent.putExtra("id", item.get("id"));
            intent.putExtra("amount", item.get("amount"));
            intent.putExtra("note", item.get("note"));
            intent.putExtra("date", item.get("date"));
            intent.putExtra("type", item.get("type"));
            intent.putExtra("time", item.get("time"));
            intent.putExtra("from_account", item.get("from_account"));
            intent.putExtra("to_account", item.get("to_account"));
            intent.putExtra("expense_category", item.get("expense_category"));
            intent.putExtra("income_category", item.get("income_category"));
            intent.putExtra("income_payment_method", item.get("income_payment_method"));
            intent.putExtra("expense_payment_method", item.get("expense_payment_method"));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private int getCategoryImageResourceExpense(String category) {
        for (int i = 0; i < expenseCategoryNames.length; i++) {
            if (expenseCategoryNames[i].equalsIgnoreCase(category)) {
                return expenseCategoryImages[i];
            }
        }
        return R.drawable.other;
    }

    private int getCategoryImageResourceIncome(String category) {
        for (int i = 0; i < incomeCategoryNames.length; i++) {
            if (incomeCategoryNames[i].equalsIgnoreCase(category)) {
                return incomeCategoryImages[i];
            }
        }
        return R.drawable.other;
    }

    private int getImageResourceIncome(String category) {
        for (int i = 0; i < PaymentMethodNames.length; i++) {
            if (PaymentMethodNames[i].equalsIgnoreCase(category)) {
                return PaymentMethodImages[i];
            }
        }
        return R.drawable.other;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<HashMap<String, String>> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalData);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (HashMap<String, String> item : originalData) {
                    String amount = item.get("amount");
                    String note = item.get("note");
                    String date = item.get("date");

                    if ((amount != null && amount.toLowerCase().contains(filterPattern)) ||
                            (note != null && note.toLowerCase().contains(filterPattern)) ||
                            (date != null && date.toLowerCase().contains(filterPattern))) {
                        filteredList.add(item);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData.clear();
            filteredData.addAll((ArrayList<HashMap<String, String>>) results.values);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage, payImage;
        TextView itemAmount, itemNote, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.image_expense_cat);
            payImage = itemView.findViewById(R.id.paytype_img);
            itemAmount = itemView.findViewById(R.id.amount);
            itemNote = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }
    }
}
