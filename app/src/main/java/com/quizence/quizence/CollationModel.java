package com.quizence.quizence;

import com.quizence.quizence.model.MCQmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class CollationModel {

    private String mCollationId;
    private String mPosting, mSubPosting, mSet, mGroup, course;
    private long mCollationDate;
    private int mNumberOfQuestions;
    private int mTestHour, mTestMinute, mTestYear,
            mTestMonth, mTestDayOfMonth;
    private String mQuestionType, mTestDurationText, mTestDurationNumber, mTestDurationUnit;
    private long mTestDuration;
    private List<MCQmodel> mQuestions = new ArrayList<>();

    CollationModel(String posting){
        mPosting = posting;
        course = mPosting.toLowerCase();
        mTestHour = -1;
        mTestMinute = -1;
        mTestYear = -1;
        mTestMonth = -1;
        mTestDayOfMonth = -1;
    }

    CollationModel(String posting, String subPosting) {
        this(posting);
        mSubPosting = subPosting;
    }

    void setCollationId(String id){
        mCollationId = id;
    }

    String getCollationId(){
        return mCollationId;
    }

    void setPosting(String posting) {
        mPosting = posting;
    }

    String getPosting() {
        return mPosting;
    }

    void setSubPosting(String subPosting) {
        mSubPosting = subPosting;
    }

    String getSubPosting() {
        return mSubPosting;
    }

    void setSet(String set) {
        mSet = set;
    }

    String getSet() {
        return mSet;
    }

    void setGroup(String group) {
        mGroup = group;
    }

    String getGroup() {
        return mGroup;
    }

    void adjustCollationTime(int hourOfDay, int minute){
        mTestHour = hourOfDay;
        mTestMinute = minute;
    }

    void setCollationDate(int year, int month, int dayOfMonth){
        mTestYear = year;
        mTestMonth = month;
        mTestDayOfMonth = dayOfMonth;
        if(mTestHour != -1 && mTestMinute != -1) {
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.set(year, month, dayOfMonth, mTestHour, mTestMinute);
            mCollationDate = tempCalendar.getTimeInMillis();
        }
    }

    void setCollationDate(long date) {
        mCollationDate = date;
    }

    void setCollationDate() {
        if(mTestYear != -1 && mTestMonth != -1 && mTestDayOfMonth != -1 && mTestHour != -1
        && mTestMinute != -1){
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.set(mTestYear, mTestMonth, mTestDayOfMonth, mTestHour, mTestMinute);
            mCollationDate = newCalendar.getTimeInMillis();
        }
    }

    long getCollationDate() {
        return mCollationDate;
    }

    void setNumberOfQuestions(int numberOfQuestions) {
        mNumberOfQuestions = numberOfQuestions;
    }

    int getNumberOfQuestions(){
        return mNumberOfQuestions;
    }

    void setQuestionType(String questionType) {
        mQuestionType = questionType;
    }

    String getQuestionType() {
        return mQuestionType;
    }

    void setTestDurationNumber(int value){
        mTestDurationNumber = String.valueOf(value);
        if(mTestDurationUnit != null){
            setTestDuration(value, mTestDurationUnit);
        }
    }

    String getTestDurationNumber(){
        return mTestDurationNumber;
    }

    void setTestDurationUnit(String unit) {
        mTestDurationUnit = unit;
        if(mTestDurationNumber != null){
            setTestDuration(Integer.parseInt(mTestDurationNumber), unit);
        }
    }

    String getTestDurationUnit(){
        return mTestDurationUnit;
    }

    void setTestDuration(int value, String unit){
        setTestDurationText(value, unit);
        unit = unit.toLowerCase();

        if(unit.equals("minutes")){
            value = value * 60 * 1000;
        }

        if(unit.equals("hour(s)")){
            value = value * 60 * 60 * 1000;
        }

        mTestDuration = value;
    }

    long getTestDuration(){
        return mTestDuration;
    }

    void setTestDurationText(int value, String unit){
        mTestDurationText = String.valueOf(value).concat(" ").concat(unit);
    }

    String getTestDurationText(){
        return mTestDurationText;
    }

    void addQuestion(MCQmodel question){
        mQuestions.add(question);
    }

    boolean removeQuestion(MCQmodel question) {
        return mQuestions.remove(question);
    }

    void setQuestions(MCQmodel[] model){
        mQuestions = Arrays.asList(model);
    }

    List<MCQmodel> getQuestions(){
        return mQuestions;
    }
}
