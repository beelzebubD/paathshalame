package com.singh.divyanshu.paathshala;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Student extends AppCompatActivity {

    private TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        t1=findViewById(R.id.StudentUsername);
        t1.setText(getIntent().getExtras().getString("username"));
    }
}
