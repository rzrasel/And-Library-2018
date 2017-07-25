package com.sm.cmdss.SharePref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Created by Rz Rasel on 20-Sep-16.
 */
public class SharePrefHandler {
    //|------------------------------------------------------------|
    private SharedPreferences sharedPreferences;
    private Context context;
    private static SharePrefHandler instance = null;
    //|------------------------------------------------------------|
    //|------------------------------------------------------------|

    public static SharePrefHandler getInstance(Context argContext) {
        if (instance == null) {
            instance = new SharePrefHandler(argContext);
        }
        return instance;
    }

    //|------------------------------------------------------------|
    public SharePrefHandler(Context argContext) {
        context = argContext;
    }
    //|------------------------------------------------------------|

    public void addData(String argKey, String argValue) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(argKey, argValue);
        editor.commit();
    }
    //|------------------------------------------------------------|

    public String getData(String argKey) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String retVal = sharedPreferences.getString(argKey, null);
        return retVal;
    }

    //|------------------------------------------------------------|
    public Map<String, ?> getAllKeys() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> keys = sharedPreferences.getAll();

        /*for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }*/
        return keys;
    }

    //|------------------------------------------------------------|
    public void clearByKey(String argKey) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(argKey).commit();
    }

    //|------------------------------------------------------------|
    public void clearAll() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    //|------------------------------------------------------------|
}
