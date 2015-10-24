package com.instructure.canvasapi.api;

import android.content.Context;

import com.instructure.canvasapi.model.UnreadConversationCount;
import com.instructure.canvasapi.model.UnreadNotificationCount;
import com.instructure.canvasapi.utilities.APIHelpers;
import com.instructure.canvasapi.utilities.CanvasCallback;

import retrofit.Callback;
import retrofit.http.GET;

/**
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */
public class UnreadCountAPI extends BuildInterfaceAPI {

    interface UnreadCountsInterface {
        @GET("/conversations/unread_count")
        void getUnreadConversationCount(Callback<UnreadConversationCount> callback);

        @GET("/users/self/activity_stream/summary")
        void getNotificationsCount(Callback<UnreadNotificationCount[]> callback);

        /////////////////////////////////////////////////////////////////////////////
        // Synchronous
        /////////////////////////////////////////////////////////////////////////////

        @GET("/conversations/unread_count")
        UnreadConversationCount getUnreadConversationCountSynchronous();
    }

    /////////////////////////////////////////////////////////////////////////
    // API Calls
    /////////////////////////////////////////////////////////////////////////

    public static void getUnreadConversationCount(CanvasCallback<UnreadConversationCount> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        buildCacheInterface(UnreadCountsInterface.class, callback).getUnreadConversationCount(callback);
        buildInterface(UnreadCountsInterface.class, callback).getUnreadConversationCount(callback);
    }

    public static void getUnreadNotificationsCount(CanvasCallback<UnreadNotificationCount[]> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        buildCacheInterface(UnreadCountsInterface.class, callback).getNotificationsCount(callback);
        buildInterface(UnreadCountsInterface.class, callback).getNotificationsCount(callback);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Synchronous
    //
    // If Retrofit is unable to parse (no network for example) Synchronous calls
    // will throw a nullPointer exception. All synchronous calls need to be in a
    // try catch block.
    /////////////////////////////////////////////////////////////////////////////

    public static String getUnreadConversationsCountSynchronous(Context context){

        try{
            return  buildInterface(UnreadCountsInterface.class, context).getUnreadConversationCountSynchronous().getUnreadCount();
        } catch (Exception E){
            return null;
        }
    }
}
