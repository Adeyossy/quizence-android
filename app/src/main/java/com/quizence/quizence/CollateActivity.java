package com.quizence.quizence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollateActivity extends AppCompatActivity {

    private static final int VIEW_TYPES = 2;
    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_MAIN = 1;

    private int[] mCollateDrawables = { R.drawable.medicine_coloured, R.drawable.surgery_coloured,
            R.drawable.psychiatry_coloured, R.drawable.paediatrics_coloured, R.drawable.obgyn,
                R.drawable.dark_female_doctor, R.drawable.female_doctor };

    private String[] mCollateStrings = { "Medicine", "Surgery", "Psychiatry", "Paediatrics",
            "OBGYN", "Family Medicine", "PSM" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collate);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Collate Questions");
            getSupportActionBar().setSubtitle("Pick one of the courses below");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        GridView collateGridview = findViewById(R.id.collate_gridview);

        BaseAdapter collateAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mCollateStrings.length + 1;
            }

            @Override
            public String getItem(int position) {
                return mCollateStrings[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if(getItemViewType(position) == VIEW_TYPE_MAIN){
                    if(convertView == null){
                        convertView = LayoutInflater.from(CollateActivity.this)
                                .inflate(R.layout.collate_adapter_view, parent, false);
                    }
                    LinearLayout linearLayout = convertView.findViewById(R.id.collate_adapter_linearlayout);
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent addIntent = new Intent(CollateActivity.this,
                                    NewCollationActivity.class);
                            addIntent.putExtra(NewCollationActivity.COURSE_NAME_EXTRA, getItem(position - 1));
                            QuizenceDataHolder.get().setCurrentCollationIndex(position);
                            startActivity(addIntent);
                        }
                    });

                    CardView cardView = convertView.findViewById(R.id.collate_adapter_cardview);
//                cardView.setMinimumHeight(cardView.getWidth());

                    ImageView collateImage = convertView.findViewById(R.id.collate_adapter_imageview);
                    collateImage.setImageResource(mCollateDrawables[position - 1]);
//                collateImage.setImageTintList(getResources().getColorStateList(R.color.image_tint));
//                collateImage.setImageTintMode(PorterDuff.Mode.DARKEN);

                    TextView collateTitle = convertView.findViewById(R.id.collate_adapter_title);
                    collateTitle.setText(mCollateStrings[position - 1]);
                }

                if(getItemViewType(position) == VIEW_TYPE_TITLE){
                    if(convertView == null){
                        convertView = LayoutInflater.from(CollateActivity.this)
                                .inflate(R.layout.title_subtitle_gridlayout, parent, false);
                    }

                    TextView textView = convertView.findViewById(R.id.title_subtitle_grid_text);
                    textView.setText(R.string.choose_course_collate);
                }

                return convertView;
            }

            @Override
            public int getViewTypeCount() {
                return VIEW_TYPES;
            }

            @Override
            public int getItemViewType(int position) {
                if(position == 0) {
                    return VIEW_TYPE_TITLE;
                }
                return VIEW_TYPE_MAIN;
            }
        };

        collateGridview.setAdapter(collateAdapter);
    }
}
