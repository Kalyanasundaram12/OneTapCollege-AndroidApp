package com.kalyan.onetapcollege;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {
    String userId, email, password, username;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private List<Course> courseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        userId = getArguments().getString("userId");
        password = getArguments().getString("password");
        email = getArguments().getString("email");
        username = getArguments().getString("username");

        // Set greeting text
        TextView textGreeting = view.findViewById(R.id.text_greeting);
        textGreeting.setText("Welcome " + username + "!");

        return view;
    }
}
