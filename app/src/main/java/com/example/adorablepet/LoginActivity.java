package com.example.adorablepet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_daftar,tv_reset;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    private Button btnMasuk;
    private EditText et_email, et_password;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize(){

        tv_daftar = findViewById(R.id.tv_daftar);
        tv_reset = findViewById(R.id.tv_lupa_password);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        btnMasuk = findViewById(R.id.btn_masuk);
        et_email = findViewById(R.id.et_email_login);
        et_password = findViewById(R.id.et_password_login);

        tv_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActivity(RegisterActivity.class);
            }
        });

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActivity(ResetPasswordActivity.class);
            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!et_email.getText().toString().matches(emailPattern)) {
                    et_email.setTextColor(Color.RED);
                } else {
                    et_email.setTextColor(getResources().getColor(R.color.colorInputText));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithEmailandPassword();
            }
        });

    }

    private void loginWithEmailandPassword() {

        String email = et_email.getText().toString();
        String pass = et_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please fill your email first", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please fill your password first", Toast.LENGTH_SHORT).show();
        } else {
            mDialog.setMessage("Wait a minute...");
            mDialog.setCancelable(false);
            mDialog.setTitle("Sign In");
            mDialog.show();

            //method bawaan dari firebase auth untuk melakukan proses sign in menggunakan email dan password yang telah didaftarkan
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        setActivity(MainActivity.class);
                        Toast.makeText(LoginActivity.this, "Sign In Success", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            setActivity(MainActivity.class);
        }
    }

    public void setActivity(Class activity) {
        Intent mainIntent = new Intent(LoginActivity.this, activity);
        startActivity(mainIntent);
        finish();
    }
}
