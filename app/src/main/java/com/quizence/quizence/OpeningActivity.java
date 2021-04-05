package com.quizence.quizence;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OpeningActivity extends AppCompatActivity {

    private int[] mOpeningImages = { R.drawable.opening_activity, R.drawable.opening_activity_2,
        R.drawable.opening_activity_3};
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        TextView userNameOrEmail = findViewById(R.id.user_name);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
            mUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(mUsername != null) userNameOrEmail.setText(mUsername);

        Random random = new Random();
        int randomImageNumber = random.nextInt(3);

        ImageView openingImage = findViewById(R.id.opening_image);
        openingImage.setImageResource(mOpeningImages[randomImageNumber]);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(OpeningActivity.this, MainActivity.class));
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
