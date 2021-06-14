package com.quizence.quizence;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import com.quizence.quizence.model.MCQmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizenceUtilities {

    public static final String DESCRIPTION_KEY = "title", TYPE_KEY = "type",
            COURSE_TITLE_KEY = "course", QUESTIONS_KEY = "questions", YEAR_KEY = "year",
            QUESTION_TITLE_KEY = "question", OPTIONS_KEY = "options", ISANSWERED_KEY = "isAnswered";

    QuizenceUtilities(){}

    public static MCQmodel[] parseQuestions(JSONArray jsonArray, boolean isAnswered) throws JSONException {
        //
        MCQmodel[] questions = new MCQmodel[jsonArray.length()];
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //TODO: finish parsing data
            String questionTitle = "";
            String options = null;
            if(jsonObject.has(QUESTION_TITLE_KEY))
                questionTitle = jsonObject.getString(QUESTION_TITLE_KEY);
            MCQmodel mcQmodel = new MCQmodel(questionTitle,
                    jsonObject.getJSONArray(OPTIONS_KEY), i + 1,
                    isAnswered);
//            mQuestions.add(mcQmodel);
            if(jsonObject.has("_id")) mcQmodel.setId(jsonObject.getString("_id"));
            if(jsonObject.has("collationid")) mcQmodel.setSourceId(
                    jsonObject.getString("collationid"));
            questions[i] = mcQmodel;
        }

        return questions;
    }

    public static ObjectAnimator playAnimation(Button activeButton) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(activeButton, scaleX, scaleY).setDuration(200);
        animator.setInterpolator(new OvershootInterpolator());
        return animator;
    }
}
