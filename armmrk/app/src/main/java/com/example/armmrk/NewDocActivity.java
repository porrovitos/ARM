package com.example.armmrk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.armmrk.Models.Docs;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class NewDocActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("docs");
    Button save_button, print_button;
    EditText editTextDocNum, editTextName;
    Spinner spinner_otdel,spinner_spec,spinner_tip;
    String[] ItemList,ItemList1, ItemList2;
    ArrayAdapter<String> adapter,adapter1,adapter2;
    long invoiceNo = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_doc);
        FindViewById();
        PressSaveButtin();
        PressPrintButton();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void PressPrintButton() {
        print_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePDF();
            }
        });

    }

    private void CreatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1000,700,1).create();
        PdfDocument.Page mypage = pdfDocument.startPage(myPageInfo);
        Canvas canvas = mypage.getCanvas();

        paint.setColor(Color.rgb(0,0,0));

        paint.setTextSize(30f);
        canvas.drawText("Договор N"+ editTextDocNum.getText() ,400,60,paint);
        paint.setTextSize(20f);
        canvas.drawText("г. Минск",800,110,paint);
        canvas.drawText("Учреждение образование \"Беларуский государственный университет информатики и ",70,190,paint);
        canvas.drawText("радиоэлектроники\" филиал \"Минский радиотехнический колледж\", в лице директора Анкуда ",70,210,paint);
        canvas.drawText( "Сергея Николаевича, действующего на основании доверенности № 29-01/2479-01 от 01.09.2016,",70,230,paint);
        canvas.drawText( "с одной стороны, и гражданин" + editTextName.getText() + ", именуемый в дальнейшем Учащийся.",70,250,paint);
        canvas.drawText( "1. Предмет договора - подготовка специалиста со средним специальным образованием по",100,280,paint);
        canvas.drawText( "специальности " +spinner_spec.getSelectedItem().toString()+ " квалификации техник-программист. Форме образования: " + spinner_tip.getSelectedItem().toString(),70,300,paint);
        canvas.drawText( "Отделение: " +spinner_otdel.getSelectedItem().toString(),70,320,paint);
        canvas.drawText("Подпись учащегося: ",70,450, paint);
        canvas.drawText(String.valueOf(editTextName.getText()),600,450, paint);
        canvas.drawText("___________________",350,450, paint);

        pdfDocument.finishPage(mypage);

        File file = new File(this.getExternalFilesDir("/"), String.valueOf(editTextName.getText())+".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();

    }

    private void PressSaveButtin() {
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Docs docs = new Docs();
                docs.setInvoiceNo(String.valueOf(invoiceNo));
                docs.setDocNo(String.valueOf(editTextDocNum.getText()));
                docs.setStudentName(String.valueOf(editTextName.getText()));
                docs.setOtdelName(spinner_otdel.getSelectedItem().toString());
                docs.setSpecialnostName(spinner_spec.getSelectedItem().toString());
                docs.setEducationTipName(spinner_tip.getSelectedItem().toString());
                myRef.child(String.valueOf(invoiceNo)).setValue(docs);
            }
        });
    }

    private void FindViewById() {
        save_button = findViewById(R.id.btnSaveNewDoc);
        print_button = findViewById(R.id.btnPrint);
        editTextDocNum = findViewById(R.id.editTextDocNum);
        editTextName = findViewById(R.id.editTextName);
        spinner_otdel = findViewById(R.id.spinner_otdel);
        spinner_spec = findViewById(R.id.spinner_spec);
        spinner_tip = findViewById(R.id.spinner_tip);

        ItemList = new String[]{"ПОИТ", "ЭВС","ПМС","ТЭРЭС","Минтис"};
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ItemList);
        spinner_spec.setAdapter(adapter);

        ItemList1 = new String[]{"Электроники", "Компьютерных технологий"};
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ItemList1);
        spinner_otdel.setAdapter(adapter1);

        ItemList2 = new String[]{"дневная", "заочная"};
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ItemList2);
        spinner_tip.setAdapter(adapter2);
    }
}