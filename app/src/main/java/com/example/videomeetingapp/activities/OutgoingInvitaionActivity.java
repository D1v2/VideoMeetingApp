package com.example.videomeetingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videomeetingapp.R;
import com.example.videomeetingapp.models.User;
import com.example.videomeetingapp.network.ApiClient;
import com.example.videomeetingapp.network.ApiService;
import com.example.videomeetingapp.utilities.Constants;
import com.example.videomeetingapp.utilities.PreferenceManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingInvitaionActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private String inviterToken=null;
    String meetingRoom=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitaion);

        preferenceManager=new PreferenceManager(getApplicationContext());

        ImageView imageMeetingType=findViewById(R.id.iamgeMeetingType);
        String meetingType=getIntent().getStringExtra("type");
        if(meetingType!=null){
            if(meetingType.equals("video")){
                imageMeetingType.setImageResource(R.drawable.ic_videocam);
            }
        }
        TextView textFirstChar=findViewById(R.id.textFirstChar);
        TextView textUsername=findViewById(R.id.textUserName);
        TextView textEmail=findViewById(R.id.textEmail);

        User user=(User)getIntent().getSerializableExtra("user");
        if (user!=null){
            textFirstChar.setText(user.firstName.substring(0,1));
            textUsername.setText(String.format("%s %s",user.firstName,user.lastName));
            textEmail.setText(user.email);
        }
        ImageView imageStopInvitation=findViewById(R.id.imageStopInvitation);
        imageStopInvitation.setOnClickListener(v -> {
            if (user!=null){
                cancleInvitation(user.token);
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult()!=null){
                inviterToken=task.getResult().getToken();
                if(meetingType!=null && user!=null){
                    initiateMeeting(meetingType,user.token);
                }
            }
        });
    }

    private void initiateMeeting(String meetingType,String receiverToken){
        try {
            JSONArray tokens=new JSONArray();
            tokens.put(receiverToken);

            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();
            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MSG_MEETING_TYPE,meetingType);
            data.put(Constants.KEY_FIRST_NAME,preferenceManager.getString(Constants.KEY_FIRST_NAME));
            data.put(Constants.KEY_LAST_NAME,preferenceManager.getString(Constants.KEY_LAST_NAME));
            data.put(Constants.KEY_EMAIL,preferenceManager.getString(Constants.KEY_EMAIL));
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN,inviterToken);

            meetingRoom=preferenceManager.getString(Constants.KEY_USER_ID)+"_"+
                    UUID.randomUUID().toString().substring(0,5);
            data.put(Constants.REMOTE_MSG_MEETING_ROOM,meetingRoom);

            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            sendRemoteMessage(body.toString(),Constants.REMOTE_MSG_INVITATION);

        }catch (Exception e){
            Toast.makeText(OutgoingInvitaionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendRemoteMessage(String remoteBodyMessage, String type){
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constants.getRemoteMessageHeaders(),remoteBodyMessage
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful()){
                    if(type.equals(Constants.REMOTE_MSG_INVITATION)){
                        Toast.makeText(OutgoingInvitaionActivity.this,"Invitation set sucessFully",Toast.LENGTH_SHORT).show();
                    }else if(type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)){
                        Toast.makeText(OutgoingInvitaionActivity.this,"Invitation Cancelled",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(OutgoingInvitaionActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(OutgoingInvitaionActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void cancleInvitation(String receiverToken){
        try{
            JSONArray tokens=new JSONArray();
            tokens.put(receiverToken);

            JSONObject body=new JSONObject();
            JSONObject data=new JSONObject();
            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE,Constants.REMOTE_MSG_INVITATION_CANCELLED);

            body.put(Constants.REMOTE_MSG_DATA,data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens);
            sendRemoteMessage(body.toString(),Constants.REMOTE_MSG_INVITATION_RESPONSE);

        }catch (Exception e){
            Toast.makeText(OutgoingInvitaionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private BroadcastReceiver invitationResponseReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type=intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type!=null){
                if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPT)){
                    try {
                        URL serverURL = new URL("http://meet.jit.si");
                        JitsiMeetConferenceOptions conferenceOptions =
                                new JitsiMeetConferenceOptions.Builder()
                                        .setServerURL(serverURL)
                                        .setWelcomePageEnabled(false)
                                        .setRoom(getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM)).build();
                        JitsiMeetActivity.launch(OutgoingInvitaionActivity.this, conferenceOptions);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else if (type.equals(Constants.REMOTE_MSG_INVITATION_REJECT)){
                    Toast.makeText(context,"Invitation Rejected",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }
}
