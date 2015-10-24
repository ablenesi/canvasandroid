package com.instructure.canvasapi.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.instructure.canvasapi.utilities.FileUtilities;

import java.util.Date;

/**
 * @author Josh Ruesch
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */

public class MediaComment extends CanvasComparable<MediaComment>{

    private static final long serialVersionUID = 1L;

    public enum MediaType { AUDIO, VIDEO }

    //TODO: handle thumbnail of video?

    private String media_id;
    private String display_name;
    private String url;
    private String media_type;

    @SerializedName("content-type")
    private String content_type;

    ///////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////

    public String getMediaId() {
        return media_id;
    }
    public String getDisplayName(Date created_at) {
        if(display_name == null || display_name.equals("null"))
            return created_at.toLocaleString() + "." + FileUtilities.getFileExtensionFromMimetype(content_type);
        else
            return display_name;
    }
    public String getUrl() {
        return url;
    }
    public MediaType getMediaType() {
        if("video".equals(media_type)) {
            return MediaType.VIDEO;
        } else {
           return MediaType.AUDIO;
        }
    }
    public String getMimeType() {
        return content_type;
    }

    public String getFileName(){
       if(media_id == null || url == null){
           return null;
       }

       String[] split = url.split("=");
       return media_id + "."+split[split.length-1];
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
        return display_name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.media_id);
        dest.writeString(this.display_name);
        dest.writeString(this.url);
        dest.writeString(this.media_type);
        dest.writeString(this.content_type);
    }

    public MediaComment() {
    }

    private MediaComment(Parcel in) {
        this.media_id = in.readString();
        this.display_name = in.readString();
        this.url = in.readString();
        this.media_type = in.readString();
        this.content_type = in.readString();
    }

    public static Creator<MediaComment> CREATOR = new Creator<MediaComment>() {
        public MediaComment createFromParcel(Parcel source) {
            return new MediaComment(source);
        }

        public MediaComment[] newArray(int size) {
            return new MediaComment[size];
        }
    };
}
