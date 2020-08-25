package com.example.videomeetingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videomeetingapp.R;
import com.example.videomeetingapp.models.User;

public class OutgoingInvitaionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitaion);
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
        imageStopInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
