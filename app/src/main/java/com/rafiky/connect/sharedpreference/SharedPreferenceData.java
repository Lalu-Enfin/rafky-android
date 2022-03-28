package com.rafiky.connect.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rafiky.connect.utils.Constants;


/**
 * Created By Athul on 14-10-2019.
 */
public class SharedPreferenceData {

    private Context context;
    private String TAG = "SharedPreferenceData";

    public SharedPreferenceData(Context mContext) {
        this.context = mContext;
    }

    public final static String PREFS_NAME = Constants.PREFS_NAME;

    /**
     * Method for insert integer value to shared preference
     *
     * @param key   Key
     * @param value integer value
     */
    public void setInt(String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Method for fetching integer value to shared preference
     *
     * @param key key
     * @return integer value
     */
    public int getInt(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(key, 0);
    }

    /**
     * Method for insert string value to shared preference
     *
     * @param key   Key
     * @param value string value
     */
    public void setString(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Method for fetching string value to shared preference
     *
     * @param key key
     * @return string value
     */
    public String getString(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "");
    }

    /**
     * Method for insert boolean value to shared preference
     *
     * @param key   Key
     * @param value boolean value
     */
    public void setBool(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Method for fetching boolean value to shared preference
     *
     * @param key key
     * @return boolean value
     */
    public boolean getBool(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getBoolean(key, false);
    }

    /**
     * Method for deleting the shared preference values.
     */
    public void deleteAllPreference() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        prefs.edit().clear().apply();
    }

    public void setObject(String key,Object myObject){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myObject);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public String getObject(String key){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        Gson gson = new Gson();
        String json = sharedPref.getString(key, null);
        return json;
    }

}
