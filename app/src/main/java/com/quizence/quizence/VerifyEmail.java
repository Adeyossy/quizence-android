package com.quizence.quizence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class VerifyEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);
    }
}
