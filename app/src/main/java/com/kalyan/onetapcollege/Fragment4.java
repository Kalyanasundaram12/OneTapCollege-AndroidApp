package com.kalyan.onetapcollege;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.ImageView;

public class Fragment4 extends Fragment {

    String userId, email, password, username;
    private Button registerComplaintBtn, statusBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_4, container, false);

        userId = getArguments().getString("userId");
        password = getArguments().getString("password");
        email = getArguments().getString("email");
        username = getArguments().getString("username");

        // Initialize views
        registerComplaintBtn = view.findViewById(R.id.register_complaint_btn);
        statusBtn = view.findViewById(R.id.status_btn);

        // Set click listeners
        registerComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterComplaintActivity();
            }
        });

        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatusActivity();
            }
        });

        return view;
    }

    private void openRegisterComplaintActivity() {
        Intent intent = new Intent(getActivity(), RegisterComplaintActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("password", password);
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openStatusActivity() {
        Intent intent = new Intent(getActivity(), StatusActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("password", password);
        intent.putExtra("email", email);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
