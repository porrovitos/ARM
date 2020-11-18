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
import com.example.armmrk.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewDocActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("docs");
    Button save_button, print_button;
    EditText editTextDocNum, editTextName;
    Spinner spinner_otdel,spinner_spec,spinner_tip;
    String[] ItemList,ItemList1, ItemList2;
    ArrayAdapter<String> adapter,adapter1,adapter2;
    long invoiceNo = 0;
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
        CreatePDF();
    }

    private void CreatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250,350,1).create();
        PdfDocument.Page mypage = pdfDocument.startPage(myPageInfo);
        Canvas canvas = mypage.getCanvas();

        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText("Договор N"+String.valueOf(invoiceNo) ,20,20,paint);
    }

    private void PressSaveButtin() {
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Docs docs = new Docs();
                docs.setInvoiceNo(String.valueOf(invoiceNo));
                docs.setStudentName(String.valueOf(editTextName.getText()));
                docs.setOtdelName(spinner_otdel.getSelectedItem().toString());
                docs.setSpecialnostName(spinner_spec.getSelectedItem().toString());
                docs.setEducationTipName(spinner_tip.getSelectedItem().toString());
                myRef.child(String.valueOf(invoiceNo+ 1)).setValue(docs);
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