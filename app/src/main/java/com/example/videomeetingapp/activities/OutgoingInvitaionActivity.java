package com.example.videomeetingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingInvitaionActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private String inviterToken=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitaion);

        preferenceManager=new PreferenceManager(getApplicationContext());
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult()!=null){
                inviterToken=task.getResult().getToken();
            }
        });

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
        imageStopInvitation.setOnClickListener(v -> onBackPressed());
        if(meetingType!=null && user!=null){
            initiateMeeting(meetingType,user.token);
        }
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
}
