package com.example.adorablepet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editText;
    private Button btnReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initialize();
    }

    public void initialize()
    {
        editText = (EditText) findViewById(R.id.et_email_reset);
        btnReset = (Button) findViewById(R.id.btn_reset);
        mAuth = FirebaseAuth.getInstance();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertReset();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setActivity(LoginActivity.class);
    }

    private void resetPasswordUser() {
        String email = editText.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please fill the email address first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //ini merupakan method bawaan dari firebase auth untuk mengirim link reset password ke email yang didaftarkan
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        setActivity(LoginActivity.class);
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //method untuk menampilkan pop up, pesan dialog ketika ingin mereset password
    public void alertReset(){ // fungsi untuk membuat alert dialog ketika ingin logout
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog2.setTitle("Reset Confirmation");

        alertDialog2.setCancelable(false);

        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure want to reset your password?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog
                resetPasswordUser();
            }
        });

        alertDialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog2.show();

    }

    private void setActivity(Class activity) { // fungsi untuk kelarin activity terakhir, dan diganti ke activity baru trus dikirim ke halaman login
        Intent mainIntent = new Intent(this, activity);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        this.finish();
    }
}
