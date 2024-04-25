package com.kalyan.onetapcollege;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        usernameEditText = findViewById(R.id.adminEmailEditText);
        passwordEditText = findViewById(R.id.adminPasswordEditText);
        loginButton = findViewById(R.id.adminLoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // No need for userPortalIcon, as there's no redirection for users
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Check if username and password are "admin"
        if (username.equals("admin") && password.equals("admin")) {
            // Redirect to AdminDashboard
            Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Show error toast
            Toast.makeText(AdminLoginActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
        }
    }
}
