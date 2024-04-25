package com.kalyan.onetapcollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    String email, password, userId, username;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;

    private Fragment4 fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        email = i.getStringExtra("email");
        password = i.getStringExtra("password");
        userId = i.getStringExtra("userId");
        username = i.getStringExtra("username");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.frame_layout);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

        loadFragment(fragment1, userId, password, email);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.nav_item1) {
                    loadFragment(fragment1, userId, password, email);
                } else if(item.getItemId()==R.id.nav_item2) {
                    loadFragment(fragment2, userId, password, email);
                } else if(item.getItemId()==R.id.nav_item3) {
                    loadFragment(fragment3, userId, password, email);
                } else if(item.getItemId()==R.id.nav_item4) {
                    loadFragment(fragment4, userId, password, email);
                }

                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, String userId, String password, String email){
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("userId", userId);
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }
}
