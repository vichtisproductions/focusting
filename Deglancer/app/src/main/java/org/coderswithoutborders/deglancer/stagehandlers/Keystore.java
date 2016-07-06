package org.coderswithoutborders.deglancer.stagehandlers;

/**
 * Created by Lauripal on 2.7.2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import timber.log.Timber;

public class Keystore { //Did you remember to vote up my example?
    private static Keystore store;
    private SharedPreferences SP;
    private static String filename="Keys";

    private Keystore(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename,0);
    }

    public static Keystore getInstance(Context context) {
        if (store == null) {
            Timber.d("NEW STORE");
            store = new Keystore(context);
        }
        return store;
    }

    public void put(String key, String value) {
        Editor editor;

        editor = SP.edit();
        editor.putString(key, value);
        // editor.commit(); // Stop everything and do an immediate save!
        editor.apply();//Keep going and save when you are not busy - Available only in APIs 9 and above.  This is the preferred way of saving.
    }

    public String get(String key) {
        return SP.getString(key, null);

    }

    public int getInt(String key) {
        return SP.getInt(key, 0);
    }

    public void putInt(String key, int num) {
        Editor editor;
        editor = SP.edit();

        editor.putInt(key, num);
        editor.apply();
    }

    public long getLong(String key) {
        return SP.getLong(key, 0);
    }

    public void putLong(String key, long num) {
        Editor editor;
        editor = SP.edit();

        editor.putLong(key, num);
        editor.apply();
    }


    public void clear(){
        Editor editor;
        editor = SP.edit();

        editor.clear();
        editor.apply();
    }

    public void remove(){
        Editor editor;
        editor = SP.edit();

        editor.remove(filename);
        editor.apply();
    }
}