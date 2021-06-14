package com.quizence.quizence.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mustapha Adeyosola on 03-Apr-20.
 */

public class MCQOptionModel implements Parcelable {

    private String option;
    private boolean isAnswered, answer;
    private transient int mUserAnswer;
    private String explanation;

    public MCQOptionModel(){
        option = "";
        explanation = "";
    }

    public MCQOptionModel(String option, boolean isAnswer, String explanation){
        this.option = option;
        answer = isAnswer;
        this.explanation = explanation;
    }

    private MCQOptionModel(Parcel in) {
        option = in.readString();
        answer = in.readByte() != 0;
        mUserAnswer = in.readInt();
        isAnswered = in.readByte() != 0;
        explanation = in.readString();
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
        this.option = option;
    }

    //returns the text of the option
    public String getOptionText(){
        return option;
    }

    public void setAnswer(boolean isAnswer){
        answer = isAnswer;
    }

    public boolean getAnswer(){
        return answer;
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
        int answer = this.answer ? 1 : 2;
        if(mUserAnswer == answer) return 1;
        return 0;
    }

    public void setMarked(boolean isMarked){
        isAnswered = isMarked;
    }

    public boolean getMarked(){
        return isAnswered;
    }

    //method to set the explanation of the option as either the answer or not
    public void setExplanation(String explanation){
        this.explanation = explanation;
    }

    //when queried for the explanation, this method should return it
    public String getExplanation(){
        return explanation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(option);
        dest.writeInt(mUserAnswer);
        dest.writeByte((byte) (answer ? 1 : 0));
        dest.writeByte((byte) (isAnswered ? 1 : 0));
        dest.writeString(explanation);
    }
}
