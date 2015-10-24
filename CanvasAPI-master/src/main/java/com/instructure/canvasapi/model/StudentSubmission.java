package com.instructure.canvasapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Brady Larson
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */

public class StudentSubmission extends CanvasModel<StudentSubmission> implements Parcelable {

    private long user_id;
    private ArrayList<Submission> submissions;
    private double computed_final_score;
    private double computed_current_score;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(ArrayList<Submission> submissions) {
        this.submissions = submissions;
    }

    public double getComputed_final_score() {
        return computed_final_score;
    }

    public void setComputed_final_score(double computed_final_score) {
        this.computed_final_score = computed_final_score;
    }

    public double getComputed_current_score() {
        return computed_current_score;
    }

    public void setComputed_current_score(double computed_current_score) {
        this.computed_current_score = computed_current_score;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Comparable
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public long getId() {
        return user_id;
    }

    @Override
    public Date getComparisonDate() {
        return null;
    }

    @Override
    public String getComparisonString() {
        return null;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Parcelable Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.user_id);
        dest.writeSerializable(this.submissions);
        dest.writeDouble(this.computed_final_score);
        dest.writeDouble(this.computed_current_score);
    }

    public StudentSubmission() {
    }

    private StudentSubmission(Parcel in) {
        this.user_id = in.readLong();
        this.submissions = (ArrayList<Submission>) in.readSerializable();
        this.computed_final_score = in.readDouble();
        this.computed_current_score = in.readDouble();
    }

    public static Creator<StudentSubmission> CREATOR = new Creator<StudentSubmission>() {
        public StudentSubmission createFromParcel(Parcel source) {
            return new StudentSubmission(source);
        }

        public StudentSubmission[] newArray(int size) {
            return new StudentSubmission[size];
        }
    };
}
