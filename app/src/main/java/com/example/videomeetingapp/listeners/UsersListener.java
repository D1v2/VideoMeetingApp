package com.example.videomeetingapp.listeners;

import com.example.videomeetingapp.models.User;

public interface UsersListener {
    void initiatedVideoMeeting(User user);

    void initiatedAudioMeeting(User user);
    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}