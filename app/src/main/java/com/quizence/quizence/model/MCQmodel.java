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

    private String mCourse, mId, mSourceID;
    private int mSerialNumber;
    private String mQuestionTitle;
    private List<MCQOptionModel> mOptions;
    private boolean mIsMarked, mIsAnswered, mIsAnswerShown;

    private static final String OPTION_TEXT = "option";
    private static final String OPTION_ANSWER = "answer";
    public static final String OPTION_ISANSWER = "isAnswer";
    private static final String OPTION_EXPLANATION = "explanation";

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

    public MCQmodel(){
        mOptions = new ArrayList<>();
        setOptionsInModel();
    }

    public MCQmodel(Parcel in){
        mOptions = new ArrayList<>();
        mQuestionTitle = in.readString();
        mIsMarked = in.readByte() != 0;
        mIsAnswered = in.readByte() != 0;
        mIsAnswerShown = in.readByte() != 0;
        mId = in.readString();
        in.readTypedList(mOptions, MCQOptionModel.CREATOR);
    }

    public MCQmodel(String questionTitle, JSONArray options,
                    int serialNumber, boolean isAnswered){
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
            String optionText = "";
            boolean answer = false;
            String explanation = "";
            if(option.has(OPTION_TEXT)) optionText = option.getString(OPTION_TEXT);
            if(option.has(OPTION_ANSWER)) answer = option.getBoolean(OPTION_ANSWER);
            if(option.has(OPTION_EXPLANATION)) explanation = option.getString(OPTION_EXPLANATION);
            mOptions.add(new MCQOptionModel(optionText, answer, explanation));
        }
    }

    private void setOptionsInModel(){
        for(int i = 0; i < 5; i++){
            mOptions.add(new MCQOptionModel());
        }
    }

    public void setId(String id){
        mId = id;
    }

    public String getId(){
        return mId;
    }

    public void setSourceId(String sourceId){
        mSourceID = sourceId;
    }

    public String getSourceId(){
        return mSourceID;
    }

    public String getSerialNumber(){
        return String.valueOf(mSerialNumber);
    }

    public void setQuestionTitle(String title){
        mQuestionTitle = title;
    }

    public String getQuestionTitle(){
        return mQuestionTitle;
    }

    public List<MCQOptionModel> getOptions(){
//        Collections.shuffle(mOptions);
        if(mOptions.size() > 5) return mOptions.subList(0, 4);
        return mOptions;
    }

    public List<MCQOptionModel> getOptionsNoShuffle(){
        return mOptions;
    }

    public void addOption(MCQOptionModel optionModel){
        mOptions.add(optionModel);
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

    //Is the answer shown in view
    public void setAnswerShown(boolean answerShown){
        mIsAnswerShown = answerShown;
    }

    public boolean getAnswerShown(){
        return mIsAnswerShown;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuestionTitle);
        dest.writeString(mSourceID);
        dest.writeTypedList(mOptions);
        dest.writeByte((byte) (mIsMarked ? 1 : 0));
        dest.writeByte((byte) (mIsAnswered ? 1 : 0));
        dest.writeByte((byte) (mIsAnswerShown ? 1 : 0));
    }
}
