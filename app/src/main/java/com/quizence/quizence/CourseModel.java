package com.quizence.quizence;

import android.os.Parcel;
import android.os.Parcelable;

import com.quizence.quizence.model.MCQmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseModel implements Parcelable {

    private String mTitle, mType, mCourseTitle, mSourceID;
    private int mYear;
    private String mAuthor; //TODO: update the backend with the details of the author
    private List<MCQmodel> mQuestions;
    private boolean mIsAnswered;

    private static final String DESCRIPTION_KEY = "title", TYPE_KEY = "type",
        COURSE_TITLE_KEY = "course", QUESTIONS_KEY = "questions", YEAR_KEY = "year",
            QUESTION_TITLE_KEY = "question", OPTIONS_KEY = "options", ISANSWERED_KEY = "isAnswered",
                ID_KEY = "_id";

    public CourseModel(JSONObject jsonObject){
        mQuestions = new ArrayList<>();
        try {
            mTitle = jsonObject.getString(DESCRIPTION_KEY);
            mType = jsonObject.getString(TYPE_KEY);
            mCourseTitle = jsonObject.getString(COURSE_TITLE_KEY);
            mIsAnswered = jsonObject.getBoolean(ISANSWERED_KEY);
            mSourceID = jsonObject.getString(ID_KEY);
//            mYear = jsonObject.getInt(YEAR_KEY);
            JSONArray array = jsonObject.getJSONArray(QUESTIONS_KEY);
            parseQuestions(array);
        } catch (JSONException e) {
            e.getMessage();
        }
    }

    protected CourseModel(Parcel in) {
    }

    public static final Creator<CourseModel> CREATOR = new Creator<CourseModel>() {
        @Override
        public CourseModel createFromParcel(Parcel in) {
            return new CourseModel(in);
        }

        @Override
        public CourseModel[] newArray(int size) {
            return new CourseModel[size];
        }
    };

    private void parseQuestions(JSONArray jsonArray) throws JSONException {
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //TODO: finish parsing data
            MCQmodel mcQmodel = new MCQmodel(jsonObject.getString(QUESTION_TITLE_KEY),
                    jsonObject.getJSONArray(OPTIONS_KEY), i + 1,
                    mIsAnswered);
            mcQmodel.setSourceId(mSourceID);
            mQuestions.add(mcQmodel);
        }
    }

    public String getType(){
        return mType;
    }

    public String getDescription(){
        return mTitle;
    }

    public String getCourseTitle(){
        return mCourseTitle;
    }

    public int getYear(){
        return mYear;
    }

    public List<MCQmodel> getQuestions(){
        return mQuestions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
