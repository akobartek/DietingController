package com.example.przemeksokolowski.dietingcontroller.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.przemeksokolowski.dietingcontroller.R;

public class Preferences {

    public static int getDailyLimit(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(context.getString(R.string.prefs_calories_key),
                Integer.parseInt(context.getString(R.string.prefs_calories_default)));
    }
}
