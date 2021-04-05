package com.quizence.quizence;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quizence.quizence.model.MCQmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mustapha Adeyosola on 26-Mar-20.
 */

public class QuizenceDataHolder {

    private static QuizenceDataHolder sQuizenceDataHolder;
    private List<CoursesBackbone> mCourses;
    private List<MCQmodel> mMCQquestions;
    private int mCurrentCourseIndex;

    private RequestQueue mRequestQueue;

    private QuizenceDataHolder(){
        mCourses = new ArrayList<>();

        CoursesBackbone coursesBackbone1 = new CoursesBackbone(0, "Paediatrics", 20, R.drawable.paediatrics);
        mCourses.add(coursesBackbone1);

        CoursesBackbone coursesBackbone2 = new CoursesBackbone(0, "OBGYN", 10, R.drawable.obgyn);
        mCourses.add(coursesBackbone2);

        CoursesBackbone coursesBackbone3 = new CoursesBackbone(0, "Medicine", 40, R.drawable.medicine);
        mCourses.add(coursesBackbone3);

        CoursesBackbone coursesBackbone4 = new CoursesBackbone(0, "Surgery", 35, R.drawable.surgery);
        mCourses.add(coursesBackbone4);

        CoursesBackbone coursesBackbone5 = new CoursesBackbone(0, "Psychiatry", 60, R.drawable.psychiatry);
        mCourses.add(coursesBackbone5);
    }

    //this method gets the singleton instance
    public static QuizenceDataHolder get(){
        if(sQuizenceDataHolder == null){
            sQuizenceDataHolder = new QuizenceDataHolder();
        }
        return sQuizenceDataHolder;
    }

    //sets a general course index
    public void setCurrentCourseIndex(int courseIndex){
        mCurrentCourseIndex = courseIndex;
    }

    public int getCurrentCourseIndex(){
        return mCurrentCourseIndex;
    }

    //returns the course at the specified position
    public CoursesBackbone getCourse(){
        return mCourses.get(mCurrentCourseIndex);
    }

    //returns the list of all the courses
    public List<CoursesBackbone> getCourses(){
        return mCourses;
    }

    public void parseData(JSONArray jsonArray) throws JSONException {
        if(mCourses.get(mCurrentCourseIndex).getAllQuestions().size() == 0){
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //TODO: create CourseModels and deploy to the backend
                CourseModel courseModel = new CourseModel(jsonObject);
                mCourses.get(mCurrentCourseIndex).addToAllQuestions(courseModel);
            }
        }
    }

    //get the questions from file in assets
    public List<MCQmodel> getQuestions(String filename, Context context){
        if(mMCQquestions == null){
            BufferedReader reader = null;
            StringBuilder fileText = new StringBuilder();

            try {
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
                String eachLine;
                while((eachLine = reader.readLine()) != null){
                    fileText.append(eachLine).append("\n");
                }
//                Log.v("fileText", fileText.toString());
                return parseJSON(fileText.toString(), context);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("File Opening Error", e.getMessage());
            } finally {
                try {
                    if(reader != null) reader.close();
                } catch (IOException e) {
                    Log.v("File Closing Error", e.getMessage());
                }
            }

            try {
                parseJSON(filename, context);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mMCQquestions;
    }

    public List<MCQmodel> getQuestions(){
        return mMCQquestions;
    }

    public void clearQuestionsList(){
        mMCQquestions = getCourse().getCurrentQuestions();
        if(getCourse().getCurrentQuestions().size() != 0) mMCQquestions = null;
    }

    //parseJSON from file
    private List<MCQmodel> parseJSON(String readFile, Context context) throws JSONException {
        mMCQquestions = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(readFile);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            MCQmodel mcqmodel = new MCQmodel(jsonObject.getString("title"),
                    jsonObject.getJSONArray("options"), i + 1, false);
            mMCQquestions.add(mcqmodel);
        }

        return mMCQquestions;
    }

    //maintain the RequestQueue in one place
    public void addToRequestQueue(JsonRequest jsonRequest, Context context){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }

        mRequestQueue.add(jsonRequest);
    }

    public void cancelRequestQueue(String classSimpleName){
        //TODO: cancel request in volley
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(classSimpleName);
        }
    }
}
