package com.alihashem.moghtrb.models;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import java.util.Locale;

public class MyContextWrapper extends ContextWrapper {
    private static final String TAG = "MyContextWrapper";

    public MyContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context, Locale newLocale) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        Log.i(TAG, "wrap: current" + Build.VERSION.SDK_INT);
        Log.i(TAG, "wrap: N " + Build.VERSION.SDK_INT);
        Log.i(TAG, "wrap: jelly " + Build.VERSION_CODES.JELLY_BEAN_MR2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            configuration.setLocale(newLocale);
            Log.i(TAG, "wrap: current in 2");
            context = context.createConfigurationContext(configuration);

        } else {
            Log.i(TAG, "wrap: current in 3");
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }

        return new ContextWrapper(context);
    }
}