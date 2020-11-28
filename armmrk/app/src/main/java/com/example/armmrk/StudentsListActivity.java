package com.example.armmrk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.armmrk.Models.Docs;
import com.example.armmrk.Models.Students;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class StudentsListActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("students");

    long invoiceNo = 1;

    Button button, back;
    EditText studentName;
    DataTable dataTable;
    DataTableHeader header;

    ArrayList<DataTableRow> rows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        button = findViewById(R.id.oldAddBtn);
        studentName = findViewById(R.id.oldAddEditText);
        dataTable = findViewById(R.id.data_table_students );

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        header = new DataTableHeader.Builder()
                .item("ID",5)
                .item("Имя абитуриента",20)
                .build();
        loadTable();
        PressSaveButtin();
        back = findViewById(R.id.BackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentsListActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
    private void loadTable() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            rows.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataTableRow row = new DataTableRow.Builder()
                            .value(String.valueOf(dataSnapshot.child("invoiceNo").getValue()))
                            .value(String.valueOf(dataSnapshot.child("studentName").getValue()))
                            .build();
                    rows.add(row);
                }
                dataTable.setHeader(header);
                dataTable.setRows(rows);
                dataTable.inflate(StudentsListActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void PressSaveButtin() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Students students = new Students();
                students.setInvoiceNo(String.valueOf(invoiceNo + 1));
                students.setStudentName(String.valueOf(studentName.getText()));
                myRef.child(String.valueOf(invoiceNo + 1)).setValue(students); // Не работает
                loadTable();
            }
        });
    }
}