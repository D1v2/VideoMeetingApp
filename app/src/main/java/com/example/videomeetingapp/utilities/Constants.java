package com.example.videomeetingapp.utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FCM_TOKEN = "fcm_token";


    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE="type";
    public static final String REMOTE_MSG_INVITATION="invitation";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";
    public static final String REMOTE_MSG_MEETING_TYPE="meeting_type";
    public static final String REMOTE_MSG_INVITER_TOKEN="inviterToken";
    public static final String REMOTE_MSG_INVITATION_RESPONSE="invitationResponse";
    public static final String REMOTE_MSG_INVITATION_ACCEPT="accept";
    public static final String REMOTE_MSG_INVITATION_REJECT="reject";
    public static final String REMOTE_MSG_INVITATION_CANCELLED="cancelled";
    public static final String REMOTE_MSG_MEETING_ROOM="meetingRoom";


    public static HashMap<String ,String > getRemoteMessageHeaders(){
        HashMap<String,String>headers=new HashMap<>();
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAPoMMiDk:APA91bGvp4iZRxgReS_2rt7H5rJNAIIjZ6uPzgkfbhbD8VxodZnYZATsv2yV5mgh0WvNjKrSWwINFAw7UGZrsLAdI5crTEynA8P6vRpFk3i6Mt9f_FIf_nkic3FDOWGn8sUJ7-SvHuxM"
        );
        headers.put(
                Constants.REMOTE_MSG_CONTENT_TYPE,"application/json"
        );
        return headers;
    }
}
