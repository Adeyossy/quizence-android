package com.quizence.quizence;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mustapha Adeyosola on 26-Mar-20.
 */

public class CourseSelectionFragment extends Fragment {

    private static final String COURSEBACKBONE_KEY = "CourseBackboneKey";

    private OnCourseSelectedListener mListener;
    private CoursesBackbone mCourseBackbone;
    private int mCounter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCourseBackbone = (CoursesBackbone) getArguments().getParcelable(COURSEBACKBONE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        CardView cardView = view.findViewById(R.id.fragment_selection_cardview);
//        cardView.setCardBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));

        TextView textView = view.findViewById(R.id.fragment_selection_title);
        textView.setText(mCourseBackbone.getCourseName());

        TextView subtitleText = view.findViewById(R.id.fragment_selection_subtitle);
        String subtitle = mCourseBackbone.getCourseQuestionsTotal() + " Questions";
        subtitleText.setText(subtitle);

        PropertyValuesHolder prop1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, 0f, 6f);
        PropertyValuesHolder prop2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.03f);
        PropertyValuesHolder prop3 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.03f);
        final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(cardView, prop1, prop2, prop3).setDuration(400);

        ImageView imageView = view.findViewById(R.id.fragment_selection_image);
        imageView.setImageResource(mCourseBackbone.getDrawable());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView) v;
                mCounter++;
                boolean state = (mCounter % 2) == 1;
                if(state){
                    animator.start();
                    img.setImageTintList(getResources().getColorStateList(R.color.image_tint));
                    img.setImageTintMode(PorterDuff.Mode.OVERLAY);
                }else{
                    animator.reverse();
                    img.setImageTintList(null);
                }

                mListener.onCourseSelected(state, mCourseBackbone.getCourseName().toLowerCase());
                //TODO: remove the harcoded text and put mCourseBackbone.getCourseName();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static CourseSelectionFragment newInstance(CoursesBackbone coursesBackbone){
        CourseSelectionFragment courseSelectionFragment = new CourseSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(COURSEBACKBONE_KEY, coursesBackbone);
        courseSelectionFragment.setArguments(bundle);
        return courseSelectionFragment;
    }

    public void setOnCourseSelectedListener(OnCourseSelectedListener listener){
        mListener = listener;
    }

    public interface OnCourseSelectedListener{
        public void onCourseSelected(boolean state, String courseTitle);
    }
}