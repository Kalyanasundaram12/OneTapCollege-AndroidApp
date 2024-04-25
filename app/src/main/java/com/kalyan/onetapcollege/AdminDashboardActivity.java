package com.kalyan.onetapcollege;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        CardView addCourseCard = findViewById(R.id.addCourseCard);
        CardView complaintsCard = findViewById(R.id.complaintsCard);
        CardView addPdfCard = findViewById(R.id.addPdfCard);

        addPdfCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminDashboardActivity.this, AdminAddPdfActivity.class);
                startActivity(i);
            }
        });

        addCourseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action for Add Course option
                Intent i = new Intent(AdminDashboardActivity.this, AdminAddCourseActivity.class);
                startActivity(i);
//                Toast.makeText(AdminDashboardActivity.this, "Add Course Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        complaintsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action for Complaints option
                Intent i = new Intent(AdminDashboardActivity.this, AdminComplaints.class);
                startActivity(i);
//                Toast.makeText(AdminDashboardActivity.this, "Complaints Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
