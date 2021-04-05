package com.quizence.quizence;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private String[] mDashboardItems = {
            "Notes", "Quiz", "Question Bank"
    };

    private int[] mDashboardIcons = {
            R.drawable.ic_book_black_24dp, R.drawable.ic_assessment_black_24dp, R.drawable.ic_help_black_24dp, R.drawable.ic_note_black_24dp
    };

    private String mDisplayName, mEmail;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        String title = "Dashboard";
        if(mFirebaseUser != null){
            mDisplayName = mFirebaseUser.getDisplayName();
            mEmail = mFirebaseUser.getEmail();
            if(mDisplayName != null && !mDisplayName.equals("")) title = title.concat(", ").concat(mDisplayName);
        }

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setSubtitle(mEmail);
        }

        ImageView imageView = findViewById(R.id.activity_main_avatar);
        imageView.setClipToOutline(true);

        GridView dashboardGridView = findViewById(R.id.dashboard_gridview);
        dashboardGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mDashboardItems.length;
            }

            @Override
            public String getItem(int position) {
                return mDashboardItems[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) convertView = getLayoutInflater().inflate(R.layout.gridview_layout, parent, false);

                final CardView cardView = convertView.findViewById(R.id.gridview_root);
                cardView.setMinimumHeight(cardView.getWidth());

                switch (position){
                    case 1:
                    case 2:
                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                launchSelectionActivity(cardView);
                            }
                        });
                }

                //set the icon for the imageview
                ImageView iconImgView = convertView.findViewById(R.id.main_section_icon);
                iconImgView.setImageResource((mDashboardIcons[position]));

                //set the text for the textview
                TextView textView = convertView.findViewById(R.id.main_section_text);
                textView.setText(mDashboardItems[position]);
                return convertView;
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    mDisplayName = user.getDisplayName();
                }else{
                    authenticateUser();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null){
            authenticateUser();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionbar_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.actionbar_logout:
                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(this, MainActivity.this));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void authenticateUser(){
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setTheme(R.style.AppTheme)
                .setAvailableProviders(providers).build(), RC_SIGN_IN);
    }

    public void launchSelectionActivity(View view){
        Intent selectionIntent = new Intent(this, SelectionActivity.class);
        startActivity(selectionIntent);
    }
}
