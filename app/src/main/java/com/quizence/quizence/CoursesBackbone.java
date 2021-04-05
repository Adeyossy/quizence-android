package com.quizence.quizence;

import android.os.Parcel;
import android.os.Parcelable;

import com.quizence.quizence.model.MCQmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mustapha Adeyosola on 26-Mar-20.
 */

public class CoursesBackbone implements Parcelable {

    private int mCourseId;
    private String mCourseName;
    private int mCourseQuestionsTotal;
    private int mDrawable;
    private List<MCQmodel> mCurrentQuestions = new ArrayList<>();
    private List<CourseModel> mAllQuestions = new ArrayList<>(); //list of all questions in db
//    private Object mCourseStats;

    public static final Parcelable.Creator<CoursesBackbone> CREATOR = new Creator<CoursesBackbone>() {
        @Override
        public CoursesBackbone createFromParcel(Parcel source) {
            return new CoursesBackbone(source);
        }

        @Override
        public CoursesBackbone[] newArray(int size) {
            return new CoursesBackbone[size];
        }
    };

    public CoursesBackbone(Parcel in){
        mCourseId = in.readInt();
        mCourseName = in.readString();
        mCourseQuestionsTotal = in.readInt();
        mDrawable = in.readInt();
        in.readTypedList(mCurrentQuestions, MCQmodel.CREATOR);
        in.readTypedList(mAllQuestions, CourseModel.CREATOR);
    }

    public CoursesBackbone(int courseId, String courseName, int courseQuestionsTotal, int drawable){
        mCourseId = courseId;
        mCourseName = courseName;
        mCourseQuestionsTotal = courseQuestionsTotal;
        mDrawable = drawable;
    }

    public void setCourseName(String courseName){}

    public String getCourseName(){
        return mCourseName;
    }

    public int getCourseId(){
        return mCourseId;
    }

    public int getCourseQuestionsTotal(){
        return mCourseQuestionsTotal;
    }

    public int getDrawable(){
        return mDrawable;
    }

    public void addToAllQuestions(CourseModel courseModels){
        //add this exam to the list of all questions
        mAllQuestions.add(courseModels);
    }

    public List<CourseModel> getAllQuestions(){
        return mAllQuestions;
    }

    public void setCurrentQuestions(int index){
        //set the current question from the list of questions
        mCurrentQuestions = mAllQuestions.get(index).getQuestions();
    }

    public List<MCQmodel> getCurrentQuestions(){
        return mCurrentQuestions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCourseId);
        dest.writeString(mCourseName);
        dest.writeInt(mCourseQuestionsTotal);
        dest.writeInt(mDrawable);
        dest.writeTypedList(mCurrentQuestions);
        dest.writeTypedList(mAllQuestions);
    }
}
