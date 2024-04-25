package com.kalyan.onetapcollege;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment {

    ListView listView;
    DatabaseReference coursesRef;
    List<String> courseCodes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        listView = view.findViewById(R.id.course_listview);
        courseCodes = new ArrayList<>();

        coursesRef = FirebaseDatabase.getInstance().getReference().child("coursePdfs");

        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseCodes.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    String courseCode = courseSnapshot.getKey();
                    courseCodes.add(courseCode);
                }
                updateListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourseCode = courseCodes.get(position);
                Intent intent = new Intent(getContext(), DownloadPdfActivity.class);
                intent.putExtra("courseCode", selectedCourseCode);
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, courseCodes);
        listView.setAdapter(adapter);
    }
}
