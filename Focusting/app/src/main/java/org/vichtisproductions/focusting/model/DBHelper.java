package org.vichtisproductions.focusting.model;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by chris.teli on 3/28/2016.
 */
public class DBHelper {

    private void create_realm_data_object(ScreenTime screen_time, Context context) {
        Realm realm = Realm.getInstance(context);
        // Create ScreenTime Object
        screen_time = new ScreenTime();
        ScreenTime db_screen_time = realm.createObject(ScreenTime.class);
        db_screen_time = screen_time;
        realm.commitTransaction();
    }
}
