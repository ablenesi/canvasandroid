package com.instructure.canvasapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2015 Instructure. All rights reserved.
 */
public class QuizSubmissionQuestion extends CanvasModel<QuizSubmissionQuestion> {

    //The ID of the QuizQuestion this answer is for.
    private long id;

    //Whether this question is flagged.
    private boolean flagged;

    //The possible answers for this question when those possible answers are
    //necessary.  The presence of this parameter is dependent on permissions.
    private QuizSubmissionAnswer[] answers;

    private int position;

    //The ID of the quiz
    @SerializedName("quiz_id")
    private long quizId;

    //Name of the question
    @SerializedName("question_name")
    private String questionName;

    //Type of the question. See QuizQuestion for QUESTION_TYPE
    @SerializedName("question_type")
    private String questionType;

    //Text of the question
    @SerializedName("question_text")
    private String questionText;

    //sometimes is a String, sometimes an array depending on the question type
    private Object answer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public QuizSubmissionAnswer[] getAnswers() {
        return answers;
    }

    public void setAnswers(QuizSubmissionAnswer[] answers) {
        this.answers = answers;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public QuizQuestion.QUESTION_TYPE getQuestionType() {
        return QuizQuestion.parseQuestionType(questionType);
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public Date getComparisonDate() {
        return null;
    }

    @Override
    public String getComparisonString() {
        return null;
    }

    @Override
    public int compareTo(QuizSubmissionQuestion quizSubmissionQuestion) {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeByte(flagged ? (byte) 1 : (byte) 0);
        dest.writeParcelableArray(this.answers, flags);
        dest.writeInt(this.position);
        dest.writeLong(this.quizId);
        dest.writeString(this.questionName);
        dest.writeString(this.questionType);
        dest.writeString(this.questionText);
        dest.writeSerializable((Serializable)this.answer);
    }

    public QuizSubmissionQuestion() {
    }

    private QuizSubmissionQuestion(Parcel in) {
        this.id = in.readLong();
        this.flagged = in.readByte() != 0;
        this.answers = (QuizSubmissionAnswer[])in.readParcelableArray(QuizSubmissionAnswer.class.getClassLoader());
        this.position = in.readInt();
        this.quizId = in.readLong();
        this.questionName = in.readString();
        this.questionType = in.readString();
        this.questionText = in.readString();
        this.answer = in.readSerializable();
    }

    public static final Parcelable.Creator<QuizSubmissionQuestion> CREATOR = new Parcelable.Creator<QuizSubmissionQuestion>() {
        public QuizSubmissionQuestion createFromParcel(Parcel source) {
            return new QuizSubmissionQuestion(source);
        }

        public QuizSubmissionQuestion[] newArray(int size) {
            return new QuizSubmissionQuestion[size];
        }
    };
}
