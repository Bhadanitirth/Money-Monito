package com.example.login_signup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login_signup.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends BaseAdapter implements Filterable {

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

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_view, parent, false);
        }

        ImageView itemImage = convertView.findViewById(R.id.image_expense_cat);
        ImageView payImage = convertView.findViewById(R.id.paytype_img);
        TextView itemAmount = convertView.findViewById(R.id.amount);
        TextView itemNote = convertView.findViewById(R.id.note);
        TextView date = convertView.findViewById(R.id.date);

        HashMap<String, String> item = filteredData.get(position); // Use filteredData here

        itemAmount.setText(item.get("amount"));
        itemNote.setText(item.get("note"));
        date.setText(item.get("date"));

        String category_expense = item.get("expense_category");
        if (category_expense != null) {
            int imageResource = getCategoryImageResourceExpense(category_expense);
            itemImage.setImageResource(imageResource);
        } else {
            itemImage.setImageResource(R.drawable.other);
        }

        String category_income = item.get("income_category");
        if (category_income != null) {
            int imageResource = getCategoryImageResourceIncome(category_income);
            itemImage.setImageResource(imageResource);
        }

        String pay_type = item.get("expense_payment_method");
        if (pay_type != null) {
            int imageResource = getImageResourceIncome(pay_type);
            payImage.setImageResource(imageResource);
        } else {
            payImage.setImageResource(R.drawable.other);
        }

        pay_type = item.get("income_payment_method");
        if (pay_type != null) {
            int imageResource = getImageResourceIncome(pay_type);
            payImage.setImageResource(imageResource);
        }

        String fromAccount = item.get("from_account");
        if (fromAccount != null) {
            int imageResource = getImageResourceIncome(fromAccount);
            payImage.setImageResource(imageResource);
            itemImage.setImageResource(R.drawable.transfer);
        }

        return convertView;
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
        return R.drawable.other; // Placeholder image if category not found
    }

    private int getCategoryImageResourceIncome(String category) {
        for (int i = 0; i < incomeCategoryNames.length; i++) {
            if (incomeCategoryNames[i].equalsIgnoreCase(category)) {
                return incomeCategoryImages[i];
            }
        }
        return R.drawable.other; // Placeholder image if category not found
    }

    private int getImageResourceIncome(String category) {
        for (int i = 0; i < PaymentMethodNames.length; i++) {
            if (PaymentMethodNames[i].equalsIgnoreCase(category)) {
                return PaymentMethodImages[i];
            }
        }
        return R.drawable.other; // Placeholder image if category not found
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
}
