package com.instructure.canvasapi.model;

import android.os.Parcel;

import java.util.Date;

/**
 * @author Josh Ruesch
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */

public class LTITool extends CanvasModel<LTITool>{

    private long id;
    private String name;
    private String url;

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Required Overrides
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Date getComparisonDate() {
        return null;
    }

    @Override
    public String getComparisonString() {
        return getName();
    }


    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////
    public LTITool() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
    }

    private LTITool(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.url = in.readString();
    }

    public static Creator<LTITool> CREATOR = new Creator<LTITool>() {
        public LTITool createFromParcel(Parcel source) {
            return new LTITool(source);
        }

        public LTITool[] newArray(int size) {
            return new LTITool[size];
        }
    };
}
