package mobileapp.ctemplar.com.ctemplarapp.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class AppUtils {

    public static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // ToDo
    public static String formatDate(String stringDate) {
        if(!TextUtils.isEmpty(stringDate)) {
            DateFormat parseFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
            DateFormat viewFormat = new SimpleDateFormat("h:mm a d.MM.yyyy", Locale.getDefault());
            try {
                Date date = parseFormat.parse(stringDate);
                stringDate = viewFormat.format(date);
                return stringDate;
            } catch (ParseException e) {
                Timber.e("DateParse error: %s", e.getMessage());
            }
        }

        return null;
    }
}
