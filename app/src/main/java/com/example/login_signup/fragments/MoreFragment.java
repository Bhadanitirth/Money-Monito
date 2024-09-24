package com.example.login_signup.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login_signup.LoginSignup.Login;
import com.example.login_signup.Premium;
import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.example.login_signup.Tags;
import com.example.login_signup.allEntry;


public class MoreFragment extends Fragment {

    ImageView profile_img;
    TextView logout,name;
    Intent intent;
    SharedPreferences sdp;
    CardView Transaction_card,GO_Premium_card,Tags_card;
    Login_Signin_Db database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_more, container, false);

        logout=view.findViewById(R.id.log_out);
        Transaction_card=view.findViewById(R.id.Transaction_card);
        GO_Premium_card=view.findViewById(R.id.GO_Premium_card);
        Tags_card=view.findViewById(R.id.Tags_card);
        name =view.findViewById(R.id.profile_name);
        profile_img=view.findViewById(R.id.profile_img);

        sdp = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String phone = sdp.getString("phone", "null");

        Login_Signin_Db dbHelper = new Login_Signin_Db(requireContext());
        String firstName = dbHelper.getFirstNameByPhone(phone);


        database=new Login_Signin_Db(getActivity());

        profile_img=view.findViewById(R.id.profile_img);
        String gender=database.getGender(phone);
        if(gender.equals("Male")){
            profile_img.setImageResource(R.drawable.male);
        }else if(gender.equals("FeMale")){
            profile_img.setImageResource(R.drawable.female);
        }else{
            profile_img.setImageResource(R.drawable.coustom);
        }

        if (name != null) {
            name.setText(String.format(firstName));
        }

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sdp.edit();
                    editor.clear();
                    editor.commit();

                    intent=new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
            });

        Transaction_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent=new Intent(getActivity(), allEntry.class);
                startActivity(intent);
            }
        });

        GO_Premium_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent=new Intent(getActivity(), Premium.class);
                startActivity(intent);
            }
        });

        Tags_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent=new Intent(getActivity(), Tags.class);
                startActivity(intent);
            }
        });


    return view;
    }
}