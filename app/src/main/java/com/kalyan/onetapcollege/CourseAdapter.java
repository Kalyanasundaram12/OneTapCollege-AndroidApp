package com.kalyan.onetapcollege;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<Course> courseList;
    private String username;

    public CourseAdapter(List<Course> courseList, String username) {
        this.courseList = courseList;
        this.username = username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textCourseCode.setText(course.getCourseCode());
        holder.textCourseName.setText(course.getCourseName());
        holder.textCourseCredits.setText(course.getCourseCredits());

        holder.buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Course clickedCourse = courseList.get(adapterPosition);
                    showOptionsDialog(holder.itemView.getContext(), username, clickedCourse);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private void showOptionsDialog(final android.content.Context context, final String username, final Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Option");

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("courses").child(course.getCourseCode());
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String slot1 = dataSnapshot.child("slot1").getValue(String.class);
                    String slot2 = dataSnapshot.child("slot2").getValue(String.class);

                    String[] options;
                    if (slot1 != null && slot2 != null) {
                        options = new String[]{slot1, slot2};
                    } else if (slot1 != null) {
                        options = new String[]{slot1};
                    } else if (slot2 != null) {
                        options = new String[]{slot2};
                    } else {
                        options = new String[]{};
                    }

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectedSlot = options[which];
                            checkSlotClash(context, username, course, selectedSlot);
                        }
                    });
                    builder.show();
                } else {
                    // Handle case where course data does not exist
                    Toast.makeText(context, "Course data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void checkSlotClash(final android.content.Context context, final String username, final Course course, final String selectedSlot) {
        DatabaseReference registeredCoursesRef = FirebaseDatabase.getInstance().getReference().child("RegisteredCourses").child(username);

        Query clashQuery = registeredCoursesRef.orderByChild("selectedSlot").equalTo(selectedSlot);
        clashQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean slotClash = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String existingCourseCode = snapshot.getKey();
                    if (!existingCourseCode.equals(course.getCourseCode())) {
                        // Slot clash found
                        slotClash = true;
                        break;
                    }
                }
                if (slotClash) {
                    // Notify user about slot clash
                    Toast.makeText(context, "Course clashed with another course in the same slot", Toast.LENGTH_SHORT).show();
                } else {
                    // No slot clash, add the course
                    addCourseToRegisteredCourses(username, course.getCourseCode(), course.getCourseName(), course.getCourseCredits(), selectedSlot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void addCourseToRegisteredCourses(String username, String courseCode, String courseName, String courseCredits, String selectedSlot) {
        DatabaseReference registeredCoursesRef = FirebaseDatabase.getInstance().getReference().child("RegisteredCourses").child(username).child(courseCode);
        registeredCoursesRef.child("courseCredit").setValue(courseCredits);
        registeredCoursesRef.child("courseName").setValue(courseName);
        registeredCoursesRef.child("selectedSlot").setValue(selectedSlot);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCourseCode;
        TextView textCourseName;
        TextView textCourseCredits;
        Button buttonAddCourse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourseCode = itemView.findViewById(R.id.text_course_code);
            textCourseName = itemView.findViewById(R.id.text_course_name);
            textCourseCredits = itemView.findViewById(R.id.text_course_credits);
            buttonAddCourse = itemView.findViewById(R.id.button_add_course);
        }
    }
}