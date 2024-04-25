package com.kalyan.onetapcollege;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course1> courseList;

    String userId, email, password, username;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        userId = getArguments().getString("userId");
        password = getArguments().getString("password");
        email = getArguments().getString("email");
        username = getArguments().getString("username");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("RegisteredCourses").child(username);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddCourseActivity by passing userId as an extra
                Intent intent = new Intent(getActivity(), AddCourseActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String courseCode = snapshot.getKey();
                    String courseName = snapshot.child("courseName").getValue(String.class);
                    String courseCredits = snapshot.child("courseCredit").getValue(String.class);
                    String selectedSlot = snapshot.child("selectedSlot").getValue(String.class);
                    courseList.add(new Course1(courseCode, courseName, courseCredits, selectedSlot));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load courses.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        private List<Course1> courseList;

        public CourseAdapter(List<Course1> courseList) {
            this.courseList = courseList;
        }

        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
            return new CourseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            Course1 course = courseList.get(position);
            holder.courseCode.setText(course.getCourseCode());
            holder.courseName.setText(course.getCourseName());
            holder.courseCredits.setText(course.getCourseCredits());
            holder.selectedSlot.setText(course.getSelectedSlot());

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove course from Firebase
                    databaseReference.child(course.getCourseCode()).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Course deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to delete course", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }

        public class CourseViewHolder extends RecyclerView.ViewHolder {
            TextView courseCode, courseName, courseCredits, selectedSlot;
            ImageView deleteButton;

            public CourseViewHolder(@NonNull View itemView) {
                super(itemView);
                courseCode = itemView.findViewById(R.id.course_code);
                courseName = itemView.findViewById(R.id.course_name);
                courseCredits = itemView.findViewById(R.id.course_credits);
                selectedSlot = itemView.findViewById(R.id.selected_slot);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }
        }
    }
}

class Course1 {

    private String courseCode;
    private String courseName;
    private String courseCredits;
    private String selectedSlot;

    public Course1(String courseCode, String courseName, String courseCredits, String selectedSlot) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseCredits = courseCredits;
        this.selectedSlot = selectedSlot;
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

    public String getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(String selectedSlot) {
        this.selectedSlot = selectedSlot;
    }
}

