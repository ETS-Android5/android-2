package mobileapp.ctemplar.com.ctemplarapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

import mobileapp.ctemplar.com.ctemplarapp.CTemplarApp;
import mobileapp.ctemplar.com.ctemplarapp.repository.UserStore;

public class LocaleUtils extends ContextWrapper {
    public LocaleUtils(Context base) {
        super(base);
    }

    public static ContextWrapper getContextWrapper(Context context) {
        UserStore userStore = CTemplarApp.getUserStore();
        String languageKey = userStore.getLanguageKey();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || "auto".equals(languageKey)) {
            return new ContextWrapper(context);
        }
        Locale newLocale = new Locale(languageKey);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(newLocale);

        LocaleList localeList = new LocaleList(newLocale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);

        context = context.createConfigurationContext(configuration);

        return new ContextWrapper(context);
    }

    public static void setLocale(Context context) {
        UserStore userStore = CTemplarApp.getUserStore();
        String languageKey = userStore.getLanguageKey();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O || "auto".equals(languageKey)) {
            return;
        }
        Locale newLocale = new Locale(languageKey);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        Locale.setDefault(newLocale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
        } else {
            configuration.locale = newLocale;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
