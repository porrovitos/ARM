package com.example.armmrk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.armmrk.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    Button SighInButton;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;

    RelativeLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SighInButton = findViewById(R.id.signin_button);

        root = findViewById(R.id.root_window);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        SighInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSighInWindow();
            }
        });

    }
    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("РЕГИСТРАЦИЯ");
        dialog.setMessage("Введите имя пользователя и пароль");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signin_window = inflater.inflate(R.layout.signin_window, null);
        dialog.setView(signin_window);

        final MaterialEditText Email = signin_window.findViewById(R.id.email_feald);
        final MaterialEditText Password = signin_window.findViewById(R.id.password_feald);

        dialog.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("ЗАРЕГЕСТРИРОВАТЬСЯ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(Email.getText().toString()) && TextUtils.isEmpty(Password.getText().toString())){
                    Snackbar.make(root,"Введите логин и пароль!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Email.getText().toString())){
                    Snackbar.make(root, "Введите вашу почту!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Password.getText().toString())){
                    Snackbar.make(root, "Введите ваш пароль!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(Email.getText().toString(),Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(Email.getText().toString());
                                user.setPassword(Password.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(root, "Все ок!", Snackbar.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });
            }

        });
        dialog.show();
    }
    private void showSighInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ВОЙТИ");
        dialog.setMessage("Введите имя пользователя и пароль");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signin_window = inflater.inflate(R.layout.signin_window, null);
        dialog.setView(signin_window);

        final MaterialEditText Email = signin_window.findViewById(R.id.email_feald);
         final MaterialEditText Password = signin_window.findViewById(R.id.password_feald);

        dialog.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("ВОЙТИ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(Email.getText().toString()) && TextUtils.isEmpty(Password.getText().toString())){
                    Snackbar.make(root,"Введите логин и пароль!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Email.getText().toString())){
                    Snackbar.make(root, "Введите вашу почту!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Password.getText().toString())){
                    Snackbar.make(root, "Введите ваш пароль!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                auth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(MainActivity.this, HomeActivity.class )); //Изменить потом обратно на HomeActivity!!!
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root,"Неправильно введен логин или пароль! ",Snackbar.LENGTH_LONG);
                    }
                });
            }
        });
        dialog.show();
    }
}