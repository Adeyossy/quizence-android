package com.quizence.quizence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        String toolbarTitle = "Score";
        quizenceToolbar.setTitle(toolbarTitle);
        quizenceToolbar.setSubtitle(""); //TODO: add course as subtitle
    }
}
