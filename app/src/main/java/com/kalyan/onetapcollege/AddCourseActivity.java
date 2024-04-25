package com.kalyan.onetapcollege;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList;
    private DatabaseReference databaseReference;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        username = getIntent().getStringExtra("username");


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_courses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("courses");

        // Initialize courseList and adapter
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList, username);
        recyclerView.setAdapter(adapter);

        // Fetch courses from Firebase
        fetchCourses();
    }

    private void fetchCourses() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    String courseCode = courseSnapshot.getKey();
                    String courseName = courseSnapshot.child("courseName").getValue(String.class);
                    String courseCredits = courseSnapshot.child("creditHours").getValue(Long.class)+"";
                    Course course = new Course(courseCode, courseName, courseCredits);
                    courseList.add(course);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}

class Course {
    private String courseCode;
    private String courseName;
    private String courseCredits;

    public Course(String courseCode, String courseName, String courseCredits) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseCredits = courseCredits;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCredits() {
        return courseCredits;
    }

    public void setCourseCredits(String courseCredits) {
        this.courseCredits = courseCredits;
    }
}
