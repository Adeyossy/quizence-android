package com.quizence.quizence.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mustapha Adeyosola on 03-Apr-20.
 */

public class MCQmodel implements Parcelable {

    private String mCourse;
    private int mSerialNumber;
    private String mQuestionTitle;
    private List<MCQOptionModel> mOptions;
    private boolean mIsMarked, mIsAnswered;

    public static final Parcelable.Creator<MCQmodel> CREATOR = new Creator<MCQmodel>() {
        @Override
        public MCQmodel createFromParcel(Parcel source) {
            return new MCQmodel(source);
        }

        @Override
        public MCQmodel[] newArray(int size) {
            return new MCQmodel[size];
        }
    };

    MCQmodel(Parcel in){
        mOptions = new ArrayList<>();
        mQuestionTitle = in.readString();
        mIsMarked = in.readByte() != 0;
        mIsAnswered = in.readByte() != 0;
        in.readTypedList(mOptions, MCQOptionModel.CREATOR);
    }

    public MCQmodel(String questionTitle, JSONArray options, int serialNumber, boolean isAnswered){
        mQuestionTitle = questionTitle;
        mIsAnswered = isAnswered;
        try {
            parseJSONArray(options);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSerialNumber = serialNumber;
    }

    private void parseJSONArray(JSONArray options) throws JSONException {
        mOptions = new ArrayList<>();
        for(int i = 0; i < options.length(); i++){
            JSONObject option = options.getJSONObject(i);
            mOptions.add(new MCQOptionModel(option.getString("option"), option.getBoolean("answer"),
                    option.getString("explanation")));
        }
    }

    public String getQuestionTitle(){
        return mQuestionTitle;
    }

    public List<MCQOptionModel> getOptions(){
        Collections.shuffle(mOptions);
        if(mOptions.size() > 5) return mOptions.subList(0, 4);
        return mOptions;
    }

    public int getOptionScore(boolean userAnswer, int position){
        return mOptions.get(position).getOptionScore();
    }

    public void getQuestionScore(boolean userAnswer, int position){}

    public void setMarked(boolean isMarked){
        mIsMarked = isMarked;
    }

    public boolean getMarked(){
        return mIsMarked;
    }

    //indicate whether this question is answered
    public void setAnswered(boolean isAnswered){
        mIsAnswered = isAnswered;
    }

    //Is this question answered
    public boolean getAnswered(){
        return mIsAnswered;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuestionTitle);
        dest.writeTypedList(mOptions);
        dest.writeByte((byte) (mIsMarked ? 1 : 0));
        dest.writeByte((byte) (mIsAnswered ? 1 : 0));
    }
}
