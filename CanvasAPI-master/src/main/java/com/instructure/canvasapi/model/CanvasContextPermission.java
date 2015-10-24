package com.instructure.canvasapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Copyright (c) 2014 Instructure. All rights reserved.
 */

public class CanvasContextPermission implements Parcelable {

    // Course Permissions
    @SerializedName("create_discussion_topic")
    private boolean canCreateDiscussionTopic;

    // User Permissions
    @SerializedName("can_update_name")
    private boolean canUpdateName;

    @SerializedName("can_update_avatar")
    private boolean canUpdateAvatar;

    @SerializedName("create_announcement")
    private boolean canCreateAnnouncement;

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ///////////////////////////////////////////////////////////////////////////

    public boolean canUpdateName() {
        return canUpdateName;
    }

    public void setCanUpdateName(boolean canUpdateName) {
        this.canUpdateName = canUpdateName;
    }

    public boolean canUpdateAvatar() {
        return canUpdateAvatar;
    }

    public void setCanUpdateAvatar(boolean canUpdateAvatar) {
        this.canUpdateAvatar = canUpdateAvatar;
    }

    public boolean canCreateDiscussionTopic(){
        return this.canCreateDiscussionTopic;
    }

    public void setCanCreateDiscussionTopic(boolean canCreateDiscussionTopic){
        this.canCreateDiscussionTopic = canCreateDiscussionTopic;
    }

    public boolean canCreateAnnouncement() {
        return canCreateAnnouncement;
    }

    public void setCanCreateAnnouncement(boolean canCreateAnnouncement) {
        this.canCreateAnnouncement = canCreateAnnouncement;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////
    public CanvasContextPermission() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(canUpdateName ? (byte) 1 : (byte) 0);
        dest.writeByte(canUpdateAvatar ? (byte) 1 : (byte) 0);
        dest.writeByte(canCreateDiscussionTopic ? (byte) 1 : (byte) 0);
        dest.writeByte(canCreateAnnouncement ? (byte) 1 : (byte) 0);
    }
    private CanvasContextPermission(Parcel in) {
        this.canUpdateName = in.readByte() != 0;
        this.canUpdateAvatar = in.readByte() != 0;
        this.canCreateDiscussionTopic = in.readByte() != 0;
        this.canCreateAnnouncement = in.readByte() != 0;
    }

    public static Creator<CanvasContextPermission> CREATOR = new Creator<CanvasContextPermission>() {
        public CanvasContextPermission createFromParcel(Parcel source) {
            return new CanvasContextPermission(source);
        }

        public CanvasContextPermission[] newArray(int size) {
            return new CanvasContextPermission[size];
        }
    };

}
