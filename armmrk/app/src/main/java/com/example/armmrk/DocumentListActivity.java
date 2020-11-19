package com.example.armmrk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.armmrk.Models.Docs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class DocumentListActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("docs");
    DatabaseReference retriveRes;

    String invoiceNo;
    String studentName;
    String otdelName;
    String specialnostName;
    String educationTipName;
    String docNo;

    Button button;
    EditText NoOfDoc;
    DataTable dataTable;
    DataTableHeader header;

    ArrayList<DataTableRow> rows = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);
        button = findViewById(R.id.oldPrintBtn);
        NoOfDoc = findViewById(R.id.oldPrintEditText);
        dataTable = findViewById(R.id.data_table );
        header = new DataTableHeader.Builder()
                .item("ID",5)
                .item("Имя документа",20)
                .build();
        loadTable();
        clickPrint();
    }

    private void clickPrint() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printPdf(NoOfDoc.getText().toString());
            }
        });
    }

    private void printPdf(String studentNo) {
        retriveRes = FirebaseDatabase.getInstance().getReference().child("docs").child(studentNo);
        retriveRes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = (String) snapshot.child("invoiceNo").getValue();
                studentName = (String) snapshot.child("studentName").getValue();
                otdelName = (String) snapshot.child("otdelName").getValue();
                specialnostName = (String) snapshot.child("specialnostName").getValue();
                educationTipName = (String) snapshot.child("educationTipName").getValue();
                docNo = (String) snapshot.child("docNo").getValue();

                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1000,700,1).create();
                PdfDocument.Page mypage = pdfDocument.startPage(myPageInfo);
                Canvas canvas = mypage.getCanvas();

                paint.setColor(Color.rgb(0,0,0));

                paint.setTextSize(30f);
                canvas.drawText("Договор N"+ docNo ,400,60,paint);
                paint.setTextSize(20f);
                canvas.drawText("г. Минск",800,110,paint);
                canvas.drawText("Учреждение образование \"Беларуский государственный университет информатики и ",70,190,paint);
                canvas.drawText("радиоэлектроники\" филиал \"Минский радиотехнический колледж\", в лице директора Анкуда ",70,210,paint);
                canvas.drawText( "Сергея Николаевича, действующего на основании доверенности № 29-01/2479-01 от 01.09.2016,",70,230,paint);
                canvas.drawText( "с одной стороны, и гражданин" + studentName + ", именуемый в дальнейшем Учащийся.",70,250,paint);
                canvas.drawText( "1. Предмет договора - подготовка специалиста со средним специальным образованием по",100,280,paint);
                canvas.drawText( "специальности " + specialnostName+ " квалификации техник-программист. Форме образования: " + educationTipName,70,300,paint);
                canvas.drawText( "Отделение: " +otdelName,70,320,paint);
                canvas.drawText("Подпись учащегося: ",70,450, paint);
                canvas.drawText(studentName,600,450, paint);
                canvas.drawText("___________________",350,450, paint);

                pdfDocument.finishPage(mypage);

                File file = new File(DocumentListActivity.this.getExternalFilesDir("/"), studentName+".pdf");

                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pdfDocument.close();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadTable() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataTableRow row = new DataTableRow.Builder()
                            .value(String.valueOf(dataSnapshot.child("invoiceNo").getValue()))
                            .value(String.valueOf(dataSnapshot.child("studentName").getValue()))
                            .build();
                    rows.add(row);
                }
                dataTable.setHeader(header);
                dataTable.setRows(rows);
                dataTable.inflate(DocumentListActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}