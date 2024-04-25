package com.kalyan.onetapcollege;

import android.content.Intent;
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

public class DownloadPdfActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PdfAdapter pdfAdapter;
    private List<PdfItem> pdfItemList;
    private String courseCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_pdf);

        Intent i = getIntent();
        courseCode = i.getStringExtra("courseCode");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pdfItemList = new ArrayList<>();
        pdfAdapter = new PdfAdapter(this, pdfItemList);
        recyclerView.setAdapter(pdfAdapter);

        // Fetch data from Firebase Realtime Database
        fetchPdfDataFromFirebase();
    }

    private void fetchPdfDataFromFirebase() {
        // Assuming you have the courseCode

        DatabaseReference pdfsRef = FirebaseDatabase.getInstance().getReference().child("coursePdfs").child(courseCode);
        pdfsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String coursePdfId = dataSnapshot.getKey();
                    String pdfName = dataSnapshot.child("pdfName").getValue(String.class);
                    // Assuming 'coursePdf' is the key for PDF URL
                    String pdfUrl = dataSnapshot.child("coursePdf").getValue(String.class);
                    pdfItemList.add(new PdfItem(coursePdfId, pdfName, pdfUrl));
                }
                pdfAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}
class PdfItem {
    private String coursePdfId;
    private String pdfName;
    private String pdfUrl;

    public PdfItem(String coursePdfId, String pdfName, String pdfUrl) {
        this.coursePdfId = coursePdfId;
        this.pdfName = pdfName;
        this.pdfUrl = pdfUrl;
    }

    public String getCoursePdfId() {
        return coursePdfId;
    }

    public String getPdfName() {
        return pdfName;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }
}
