package com.example.videomeetingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videomeetingapp.R;
import com.example.videomeetingapp.utilities.Constants;

public class IncomingInvitaionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitaion);
        ImageView imageMeetingType=findViewById(R.id.iamgeMeetingType);
        String meetingType=getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);
        if(meetingType!=null){
            if(meetingType.equals("video")){
                imageMeetingType.setImageResource(R.drawable.ic_videocam);
            }
        }

        TextView textFirstChar=findViewById(R.id.textFirstChar);
        TextView textUsername=findViewById(R.id.textUserName);
        TextView textEmail=findViewById(R.id.textEmail);

        String firstname=getIntent().getStringExtra(Constants.KEY_FIRST_NAME);
        if(firstname!=null){
            textFirstChar.setText(firstname.substring(0,1));
        }

        textUsername.setText(String.format("%s %s",firstname,getIntent().getStringExtra(Constants.KEY_LAST_NAME)));
        textEmail.setText(getIntent().getStringExtra(Constants.KEY_EMAIL));
    }
}
