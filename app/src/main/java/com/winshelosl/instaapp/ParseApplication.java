package com.winshelosl.instaapp;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("yFwtrdcD3WAxscr0vQaF1qd7UdHTiZ9V75W1HACe")
                .clientKey("E4YkkZKuQdfPwIQZdOIdmbhy7Yb7Qv10hTF4p3i1")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
