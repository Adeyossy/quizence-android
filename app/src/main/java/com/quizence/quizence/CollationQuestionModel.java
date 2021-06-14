package com.quizence.quizence;

import android.os.Parcel;

import com.quizence.quizence.model.MCQOptionModel;
import com.quizence.quizence.model.MCQmodel;

import org.json.JSONArray;

import java.util.ArrayList;

public class CollationQuestionModel extends MCQmodel {

    private String mCollationID;

    public CollationQuestionModel(String questionTitle, JSONArray options, int serialNumber,
                                  boolean isAnswered, String collationID, String id) {
        super(questionTitle, options, serialNumber, isAnswered);
        mCollationID = collationID;
    }

    public CollationQuestionModel(Parcel in){
        super(in);
        mCollationID = in.readString();
    }

    public void setCollationID(String collationID){
        mCollationID = collationID;
    }

    public String getCollationID(){
        return mCollationID;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeString(mCollationID);
    }
}
