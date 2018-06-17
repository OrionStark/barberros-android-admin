package com.example.orionstark.barberrosadmin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class BarberPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("StaticFieldLeak")
    private static BarberPreference session;
    private Context context;

    private final String BARBER_NAME_KEY = "barber.name";
    private final String BARBER_PHONE = "barber.phone";
    private final String BARBER_DESCRIPTION = "barber.description";
    private final String BARBER_IMAGE_STRING = "barber.image";

    private BarberPreference(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("BARBER_PREFERENCES", 0);
        this.editor = this.sharedPreferences.edit();
    }

    public static BarberPreference getInstance(Context context) {
        if ( session == null ) {
            session = new BarberPreference(context);
        }

        return session;
    }

    public void createPreference(String name, String phone, String description, String imageBase) {
        this.editor.putString(BARBER_NAME_KEY, name);
        this.editor.putString(BARBER_PHONE, phone);
        this.editor.putString(BARBER_DESCRIPTION, description);
        this.editor.putString(BARBER_IMAGE_STRING, imageBase);
        this.editor.commit();
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }

    public ArrayList<String> getBarberInfo() {
        ArrayList<String> barber = new ArrayList<>();
        barber.add(this.sharedPreferences.getString(BARBER_NAME_KEY, ""));
        barber.add(this.sharedPreferences.getString(BARBER_PHONE, ""));
        barber.add(this.sharedPreferences.getString(BARBER_DESCRIPTION, ""));
        barber.add(this.sharedPreferences.getString(BARBER_IMAGE_STRING, ""));

        return barber;
    }
}
