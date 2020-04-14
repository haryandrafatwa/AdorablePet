package com.example.adorablepet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_first, et_last, et_username, et_email, et_phonenumber, et_password;
    private TextView tv_login;
    private Button btnSignUp;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private FirebaseAuth mAuth;
    private DatabaseReference userRefs;
    private StorageReference dummyDispPict;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize(){

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        et_first = findViewById(R.id.et_first_name_daftar);
        et_last = findViewById(R.id.et_last_name_daftar);
        et_username = findViewById(R.id.et_username_daftar);
        et_email = findViewById(R.id.et_email_daftar);
        et_phonenumber = findViewById(R.id.et_phonenumber_daftar);
        et_password = findViewById(R.id.et_password_daftar);
        btnSignUp = findViewById(R.id.btn_signup);
        tv_login = findViewById(R.id.tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActivity(LoginActivity.class);
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWithEmailandPassword();
            }
        });
    }

    private void registerWithEmailandPassword() {
        final String first = et_first.getText().toString();
        final String last =  et_last.getText().toString();
        final String email = et_email.getText().toString();
        final String username = et_username.getText().toString();
        final String telephone = et_phonenumber.getText().toString();
        String password = et_password.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(telephone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(first) || TextUtils.isEmpty(last))
        {
            Toast.makeText(this, "Please complete all form first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mDialog.setTitle("Sign Up");
            mDialog.setCancelable(true);
            mDialog.setMessage("Wait a minute .. ");
            mDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserID);
                        HashMap userMap = new HashMap();
                        userMap.put("username",username);
                        userMap.put("name",first+" "+last);
                        userMap.put("first_name",first);
                        userMap.put("last_name",last);
                        userMap.put("phonenumber",telephone);
                        userMap.put("email",email);
                        userMap.put("notif","on");
                        userMap.put("theme","off");
                        userRefs.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(Task task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                    setActivity(MainActivity.class);
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                }
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                }
            });
        }
    }

    public void setActivity(Class activity) {
        Intent mainIntent = new Intent(RegisterActivity.this, activity);
        startActivity(mainIntent);
        finish();
    }

}
