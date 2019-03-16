package org.vichtisproductions.focusting.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataUtils {

    private final static String PREFERENCE_NAME = "UserInformation";

    private final RxSharedPreferences userPreferences;

    @Inject
    DataUtils(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        userPreferences = RxSharedPreferences.create(prefs);
    }

    @Nullable
    public String getUsername() {
        return userPreferences.getString("username", null).get();
    }

    public void setUsername(@Nullable String username) {
        userPreferences.getString("username").set(username);
    }
}
