package com.example.adorablepet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize(){

        tv_daftar = findViewById(R.id.tv_daftar);

        tv_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActivity(RegisterActivity.class);
            }
        });

    }

    public void setActivity(Class activity) {
        Intent mainIntent = new Intent(LoginActivity.this, activity);
        startActivity(mainIntent);
        finish();
    }
}
