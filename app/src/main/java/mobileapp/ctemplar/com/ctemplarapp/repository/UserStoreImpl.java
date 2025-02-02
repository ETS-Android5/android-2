package mobileapp.ctemplar.com.ctemplarapp.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import mobileapp.ctemplar.com.ctemplarapp.net.entity.UserEntity;
import mobileapp.ctemplar.com.ctemplarapp.utils.EditTextUtils;
import mobileapp.ctemplar.com.ctemplarapp.utils.EncodeUtils;

import java.net.Proxy;

public class UserStoreImpl implements UserStore {
    private static UserStoreImpl instance;

    private final SharedPreferences preferences;
    private final SharedPreferences globalPreferences;

    private static final String PREF_USER = "pref_user";
    private static final String KEY_USER_TOKEN = "key_user_token";
    private static final String KEY_USER_LAST_FORCE_REFRESH_TOKEN_TIME = "key_user_last_force_refresh_token_attempt_time";
    private static final String KEY_FIREBASE_TOKEN = "key_firebase_token";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";
    private static final String KEY_PASSWORD_HASHED = "key_password_hashed";
    private static final String KEY_KEEP_ME_LOGGED_IN = "key_keep_me_logged_in";
    private static final String KEY_TIMEZONE = "key_timezone";

    private static final String KEY_SIGNATURE_ENABLED = "key_signature_enabled";
    private static final String KEY_NOTIFICATIONS_ENABLED = "key_notifications_enabled";
    private static final String KEY_CONTACTS_ENCRYPTION_ENABLED = "key_contacts_encryption_enabled";
    private static final String KEY_KEEP_DECRYPTED_SUBJECTS_ENABLED = "key_keep_decrypted_subjects_enabled";
    private static final String KEY_DRAFTS_AUTO_SAVE_ENABLED = "key_drafts_auto_save_enabled";
    private static final String KEY_AUTO_READ_EMAIL_ENABLED = "key_auto_read_email_enabled";
    private static final String KEY_INCLUDE_ORIGINAL_MESSAGE = "key_include_original_message";
    private static final String KEY_BLOCK_EXTERNAL_IMAGES_ENABLED = "key_block_external_images_enabled";
    private static final String KEY_WARN_EXTERNAL_LINK_ENABLED = "key_warn_external_link_enabled";
    private static final String KEY_REPORT_BUGS_ENABLED = "key_report_bugs_enabled";

    private static final String KEY_PIN_LOCK = "key_pin_lock";
    private static final String KEY_AUTO_LOCK_TIME = "key_auto_lock_time";
    private static final String KEY_LAST_PAUSE_TIME = "key_last_pause_time";
    private static final String KEY_IS_LOCKED = "key_is_locked";
    private static final String KEY_LOCK_LAST_ATTEMPT_TIME = "key_lock_last_attempt_time";
    private static final String KEY_LOCK_ATTEMPTS_COUNT = "key_lock_attempts_count";
    private static final String KEY_IS_PRIME_DIALOG_SHOWN = "key_is_prime_dialog_shown";

    private static final String KEY_PROXY_TOR_ENABLED = "key_proxy_tor_enabled";
    private static final String KEY_PROXY_CUSTOM_ENABLED = "key_proxy_http_enabled";
    private static final String KEY_PROXY_TYPE = "key_proxy_type";
    private static final String KEY_PROXY_IP = "key_proxy_ip";
    private static final String KEY_PROXY_PORT = "key_proxy_port";

    private static final String KEY_DARK_MODE = "key_dark_mode";
    private static final String KEY_HIDE_APP_PREVIEW = "key_hide_app_preview";
    private static final String KEY_LANGUAGE = "key_language";

    public static UserStoreImpl getInstance(Context context) {
        if (instance == null) {
            instance = new UserStoreImpl(context);
        }
        return instance;
    }

    public UserStoreImpl(Context context) {
        preferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
        globalPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void saveUserToken(String token) {
        preferences.edit().putString(KEY_USER_TOKEN, token).apply();
    }

    @Override
    public String getUserToken() {
        return preferences.getString(KEY_USER_TOKEN, "");
    }

    @Override
    public void updateLastForceRefreshTokenAttemptTime() {
        preferences.edit().putLong(KEY_USER_LAST_FORCE_REFRESH_TOKEN_TIME, System.currentTimeMillis()).apply();
    }

    @Override
    public long getLastForceRefreshTokenAttemptTime() {
        return preferences.getLong(KEY_USER_LAST_FORCE_REFRESH_TOKEN_TIME, 0);
    }

    @Override
    public void saveFirebaseToken(String token) {
        preferences.edit().putString(KEY_FIREBASE_TOKEN, token).apply();
    }

    @Override
    public String getFirebaseToken() {
        return preferences.getString(KEY_FIREBASE_TOKEN, "");
    }

    @Override
    public void saveUserPref(String username, String password, String passwordHashed) {
        preferences.edit()
                .putString(KEY_USERNAME, username)
                .putString(KEY_PASSWORD, password)
                .putString(KEY_PASSWORD_HASHED, passwordHashed)
                .apply();
    }

    @Override
    public UserEntity getUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername(preferences.getString(KEY_USERNAME, ""));
        entity.setPassword(preferences.getString(KEY_PASSWORD, ""));
        entity.setPasswordHashed(preferences.getString(KEY_PASSWORD_HASHED, ""));
        entity.setToken(preferences.getString(KEY_USER_TOKEN, ""));
        return entity;
    }

    @Override
    public void clearToken() {
        preferences.edit().remove(KEY_USER_TOKEN).apply();
    }

    @Override
    public void logout() {
        preferences.edit().clear().apply();
        globalPreferences.edit().clear().apply();
    }

    @Override
    public void saveUsername(String username) {
        preferences.edit().putString(KEY_USERNAME, username).apply();
    }

    @Override
    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    @Override
    public void savePassword(String password) {
        preferences.edit().putString(KEY_PASSWORD, password).apply();
    }

    @Override
    public String getUserPassword() {
        return preferences.getString(KEY_PASSWORD, "");
    }

    @Override
    public void saveKeepMeLoggedIn(boolean state) {
        preferences.edit().putBoolean(KEY_KEEP_ME_LOGGED_IN, state).apply();
    }

    @Override
    public boolean getKeepMeLoggedIn() {
        return preferences.getBoolean(KEY_KEEP_ME_LOGGED_IN, false);
    }

    @Override
    public void saveTimeZone(String timezone) {
        preferences.edit().putString(KEY_TIMEZONE, timezone).apply();
    }

    @Override
    public String getTimeZone() {
        return preferences.getString(KEY_TIMEZONE, "");
    }

    @Override
    public void setSignatureEnabled(boolean state) {
        preferences.edit().putBoolean(KEY_SIGNATURE_ENABLED, state).apply();
    }

    @Override
    public boolean isSignatureEnabled() {
        return preferences.getBoolean(KEY_SIGNATURE_ENABLED, true);
    }

    @Override
    public void setNotificationsEnabled(boolean state) {
        preferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, state).apply();
    }

    @Override
    public boolean isNotificationsEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    @Override
    public void setContactsEncryptionEnabled(boolean state) {
        preferences.edit().putBoolean(KEY_CONTACTS_ENCRYPTION_ENABLED, state).apply();
    }

    @Override
    public boolean isContactsEncryptionEnabled() {
        return preferences.getBoolean(KEY_CONTACTS_ENCRYPTION_ENABLED, false);
    }

    @Override
    public void setKeepDecryptedSubjectsEnabled(boolean state) {
        preferences.edit().putBoolean(KEY_KEEP_DECRYPTED_SUBJECTS_ENABLED, state).apply();
    }

    @Override
    public boolean isKeepDecryptedSubjectsEnabled() {
        return preferences.getBoolean(KEY_KEEP_DECRYPTED_SUBJECTS_ENABLED, true);
    }

    @Override
    public void setDraftsAutoSaveEnabled(boolean state) {
        preferences.edit().putBoolean(KEY_DRAFTS_AUTO_SAVE_ENABLED, state).apply();
    }

    @Override
    public boolean isDraftsAutoSaveEnabled() {
        return preferences.getBoolean(KEY_DRAFTS_AUTO_SAVE_ENABLED, false);
    }

    @Override
    public void setAutoReadEmailEnabled(boolean state) {
        globalPreferences.edit().putBoolean(KEY_AUTO_READ_EMAIL_ENABLED, state).apply();
    }

    @Override
    public boolean isAutoReadEmailEnabled() {
        return globalPreferences.getBoolean(KEY_AUTO_READ_EMAIL_ENABLED, true);
    }

    @Override
    public void setIncludeOriginalMessage(boolean state) {
        globalPreferences.edit().putBoolean(KEY_INCLUDE_ORIGINAL_MESSAGE, state).apply();
    }

    @Override
    public boolean isIncludeOriginalMessage() {
        return globalPreferences.getBoolean(KEY_INCLUDE_ORIGINAL_MESSAGE, true);
    }

    @Override
    public void setBlockExternalImagesEnabled(boolean state) {
        globalPreferences.edit().putBoolean(KEY_BLOCK_EXTERNAL_IMAGES_ENABLED, state).apply();
    }

    @Override
    public boolean isBlockExternalImagesEnabled() {
        return globalPreferences.getBoolean(KEY_BLOCK_EXTERNAL_IMAGES_ENABLED, false);
    }

    @Override
    public void setWarnExternalLinkEnabled(boolean state) {
        globalPreferences.edit().putBoolean(KEY_WARN_EXTERNAL_LINK_ENABLED, state).apply();
    }

    @Override
    public boolean isWarnExternalLinkEnabled() {
        return globalPreferences.getBoolean(KEY_WARN_EXTERNAL_LINK_ENABLED, true);
    }

    @Override
    public void setReportBugsEnabled(boolean state) {
        preferences.edit().putBoolean(KEY_REPORT_BUGS_ENABLED, state).apply();
    }

    @Override
    public boolean isReportBugsEnabled() {
        return preferences.getBoolean(KEY_REPORT_BUGS_ENABLED, false);
    }

    @Override
    public void setPINLock(String pinCode) {
        String generatedHash = EncodeUtils.generateHash(getUserPassword(), pinCode);
        preferences.edit().putString(KEY_PIN_LOCK, generatedHash).apply();
    }

    @Override
    public boolean checkPINLock(@Nullable String pinCode) {
        if (pinCode == null) {
            return false;
        }
        String pinCodeHash = preferences.getString(KEY_PIN_LOCK, null);
        if (pinCodeHash == null) {
            return true;
        }
        String generatedHash = EncodeUtils.generateHash(getUserPassword(), pinCode);
        return TextUtils.equals(pinCodeHash, generatedHash);
    }

    @Override
    public void disablePINLock() {
        preferences.edit().putString(KEY_PIN_LOCK, null).apply();
    }

    @Override
    public boolean isPINLockEnabled() {
        return EditTextUtils.isNotEmpty(preferences.getString(KEY_PIN_LOCK, null));
    }

    @Override
    public void setAutoLockTime(int timeInMillis) {
        preferences.edit().putInt(KEY_AUTO_LOCK_TIME, timeInMillis).apply();
    }

    @Override
    public int getAutoLockTime() {
        return preferences.getInt(KEY_AUTO_LOCK_TIME, 60000);
    }

    @Override
    public void setLastPauseTime(long lastPauseTimeInMillis) {
        preferences.edit().putLong(KEY_LAST_PAUSE_TIME, lastPauseTimeInMillis).apply();
    }

    @Override
    public long getLastPauseTime() {
        return preferences.getLong(KEY_LAST_PAUSE_TIME, Long.MAX_VALUE);
    }

    @Override
    public void setLocked(boolean locked) {
        preferences.edit().putBoolean(KEY_IS_LOCKED, locked).apply();
    }

    @Override
    public boolean isLocked() {
        return preferences.getBoolean(KEY_IS_LOCKED, false);
    }

    @Override
    public void updateLockLastAttemptTime() {
        preferences.edit().putLong(KEY_LOCK_LAST_ATTEMPT_TIME, System.currentTimeMillis()).apply();
    }

    @Override
    public long getLockLastAttemptTime() {
        return preferences.getLong(KEY_LOCK_LAST_ATTEMPT_TIME, 0);
    }

    @Override
    public void setLockAttemptsCount(int attemptsCount) {
        preferences.edit().putInt(KEY_LOCK_ATTEMPTS_COUNT, attemptsCount).apply();
    }

    @Override
    public int getLockAttemptsCount() {
        return preferences.getInt(KEY_LOCK_ATTEMPTS_COUNT, 0);
    }

    @Override
    public void setPrimeDialogShown(boolean value) {
        preferences.edit().putBoolean(KEY_IS_PRIME_DIALOG_SHOWN, value).apply();
    }

    @Override
    public boolean isPrimeDialogShown() {
        return preferences.getBoolean(KEY_IS_PRIME_DIALOG_SHOWN, false);
    }

    @Override
    public void setProxyTorEnabled(boolean value) {
        preferences.edit().putBoolean(KEY_PROXY_TOR_ENABLED, value).apply();
    }

    @Override
    public boolean isProxyTorEnabled() {
        return preferences.getBoolean(KEY_PROXY_TOR_ENABLED, false);
    }

    @Override
    public void setProxyCustomEnabled(boolean value) {
        preferences.edit().putBoolean(KEY_PROXY_CUSTOM_ENABLED, value).apply();
    }

    @Override
    public boolean isProxyCustomEnabled() {
        return preferences.getBoolean(KEY_PROXY_CUSTOM_ENABLED, false);
    }

    @Override
    public void setProxyTypeIndex(int proxyTypeIndex) {
        preferences.edit().putInt(KEY_PROXY_TYPE, proxyTypeIndex).apply();
    }

    public int getProxyTypeIndex() {
        return preferences.getInt(KEY_PROXY_TYPE, 0);
    }

    @Override
    public Proxy.Type getProxyType() {
        return preferences.getInt(KEY_PROXY_TYPE, 0) == 0
                ? Proxy.Type.HTTP
                : Proxy.Type.SOCKS;
    }

    @Override
    public void setProxyIP(String value) {
        preferences.edit().putString(KEY_PROXY_IP, value).apply();
    }

    @Override
    public String getProxyIP() {
        return preferences.getString(KEY_PROXY_IP, null);
    }

    @Override
    public void setProxyPort(int value) {
        preferences.edit().putInt(KEY_PROXY_PORT, value).apply();
    }

    @Override
    public int getProxyPort() {
        return preferences.getInt(KEY_PROXY_PORT, 0);
    }

    @Override
    public void setDarkModeValue(int value) {
        preferences.edit().putInt(KEY_DARK_MODE, value).apply();
        AppCompatDelegate.setDefaultNightMode(value);
    }

    @Override
    public int getDarkModeValue() {
        return preferences.getInt(KEY_DARK_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    @Override
    public void setDarkModeKey(String key) {
        int value;
        switch (key) {
            case "on":
                value = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case "off":
                value = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            default:
                value = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        preferences.edit().putInt(KEY_DARK_MODE, value).apply();
        AppCompatDelegate.setDefaultNightMode(value);
    }

    @Override
    public String getDarkModeKey() {
        int value = preferences.getInt(KEY_DARK_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        switch (value) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                return "on";
            case AppCompatDelegate.MODE_NIGHT_NO:
                return "off";
            default:
                return "auto";
        }
    }

    @Override
    public void setHideAppPreview(boolean value) {
        preferences.edit().putBoolean(KEY_HIDE_APP_PREVIEW, value).apply();
    }

    @Override
    public boolean isHideAppPreview() {
        return preferences.getBoolean(KEY_HIDE_APP_PREVIEW, false);
    }

    @Override
    public void setLanguageKey(String key) {
        preferences.edit().putString(KEY_LANGUAGE, key).apply();
    }

    @Override
    public String getLanguageKey() {
        return preferences.getString(KEY_LANGUAGE, "auto");
    }
}
