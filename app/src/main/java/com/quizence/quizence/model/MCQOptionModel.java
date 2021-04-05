package com.quizence.quizence.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mustapha Adeyosola on 03-Apr-20.
 */

public class MCQOptionModel implements Parcelable {

    private String mOption;
    private boolean mIsMarked, mIsAnswer;
    private int mUserAnswer;
    private String mExplanation;

    public MCQOptionModel(String option, boolean isAnswer, String explanation){
        mOption = option;
        mIsAnswer = isAnswer;
        mExplanation = explanation;
    }

    private MCQOptionModel(Parcel in) {
        mOption = in.readString();
        mIsAnswer = in.readByte() != 0;
        mUserAnswer = in.readInt();
        mIsMarked = in.readByte() != 0;
        mExplanation = in.readString();
    }

    public static final Creator<MCQOptionModel> CREATOR = new Creator<MCQOptionModel>() {
        @Override
        public MCQOptionModel createFromParcel(Parcel in) {
            return new MCQOptionModel(in);
        }

        @Override
        public MCQOptionModel[] newArray(int size) {
            return new MCQOptionModel[size];
        }
    };

    public void setOption(String option){
        mOption = option;
    }

    //returns the text of the option
    public String getOptionText(){
        return mOption;
    }

    public void setAnswer(boolean isAnswer){
        mIsAnswer = isAnswer;
    }

    public boolean getAnswer(){
        return mIsAnswer;
    }

    //this method is for future proofing when we introduce exam mode or want to check the answer
    //after all options are selected
    public void setUserAnswer(int userAnswer){
        mUserAnswer = userAnswer;
    }

    public int getUserAnswer(){
        return mUserAnswer;
    }

    //this method calculates the score of the user-selected option
    public int getOptionScore(){
        int answer = mIsAnswer ? 1 : 2;
        if(mUserAnswer == answer) return 1;
        return 0;
    }

    public void setMarked(boolean isMarked){
        mIsMarked = isMarked;
    }

    public boolean getMarked(){
        return mIsMarked;
    }

    //method to set the explanation of the option as either the answer or not
    public void setExplanation(String explanation){
        mExplanation = explanation;
    }

    //when queried for the explanation, this method should return it
    public String getExplanation(){
        return mExplanation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOption);
        dest.writeInt(mUserAnswer);
        dest.writeByte((byte) (mIsAnswer ? 1 : 0));
        dest.writeByte((byte) (mIsMarked ? 1 : 0));
        dest.writeString(mExplanation);
    }
}
