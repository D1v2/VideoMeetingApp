package com.example.videomeetingapp.firebase;

import android.content.Intent;

import com.example.videomeetingapp.activities.IncomingInvitaionActivity;
import com.example.videomeetingapp.utilities.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type=remoteMessage.getData().get(Constants.REMOTE_MSG_TYPE);
        if(type!=null){
            if(type.equals(Constants.REMOTE_MSG_INVITATION)){
                Intent intent=new Intent(getApplicationContext(), IncomingInvitaionActivity.class);
                intent.putExtra(Constants.REMOTE_MSG_MEETING_TYPE
                ,remoteMessage.getData().get(Constants.REMOTE_MSG_MEETING_TYPE)
                );
                intent.putExtra(
                        Constants.KEY_FIRST_NAME,
                        remoteMessage.getData().get(Constants.KEY_FIRST_NAME)
                );
                intent.putExtra(
                        Constants.KEY_LAST_NAME,
                        remoteMessage.getData().get(Constants.KEY_LAST_NAME)
                );
                intent.putExtra(
                        Constants.KEY_EMAIL,
                        remoteMessage.getData().get(Constants.KEY_EMAIL)
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}