package com.kalyan.onetapcollege;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AdminAddPdfActivity extends AppCompatActivity {

    private Spinner spinnerCourses;
    private Button btnSelectPdf, btnUploadPdf;

    private DatabaseReference coursesRef, courseReff;
    private StorageReference pdfStorageRef;

    private static final int REQUEST_PDF = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_pdf);

        spinnerCourses = findViewById(R.id.spinner_courses);
        btnSelectPdf = findViewById(R.id.btn_select_pdf);
        btnUploadPdf = findViewById(R.id.btn_upload_pdf);

        coursesRef = FirebaseDatabase.getInstance().getReference().child("courses");
        courseReff = FirebaseDatabase.getInstance().getReference().child("coursePdfs");
        pdfStorageRef = FirebaseStorage.getInstance().getReference().child("course_pdfs");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapter);

        // Populate spinner with course codes from Firebase Realtime Database
        coursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String courseCode = dataSnapshot.getKey();
                    adapter.add(courseCode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminAddPdfActivity.this, "Failed to load courses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSelectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdf();
            }
        });

        btnUploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdf();
            }
        });
    }

    private void selectPdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQUEST_PDF);
    }

    private void uploadPdf() {
        if (spinnerCourses.getSelectedItem() != null) {
            final String courseCode = spinnerCourses.getSelectedItem().toString();
            if (selectedPdfUri != null) {
                final String randomPdfCode = UUID.randomUUID().toString();
                final String pdfName = getFileName(selectedPdfUri); // Get the name of the PDF file
                StorageReference fileRef = pdfStorageRef.child(courseCode).child(randomPdfCode + ".pdf");
                fileRef.putFile(selectedPdfUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the download URL of the uploaded file
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Store the URL and name in the Realtime Database under the course code
                                        courseReff.child(courseCode).child(randomPdfCode).child("coursePdf").setValue(uri.toString());
                                        courseReff.child(courseCode).child(randomPdfCode).child("pdfName").setValue(pdfName)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(AdminAddPdfActivity.this, "PDF uploaded successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AdminAddPdfActivity.this, "Failed to store PDF details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminAddPdfActivity.this, "Failed to upload PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Please select a PDF file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get the file name from URI
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private Uri selectedPdfUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PDF && resultCode == RESULT_OK && data != null) {
            selectedPdfUri = data.getData();

        }
    }
}
