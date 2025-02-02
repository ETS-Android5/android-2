package mobileapp.ctemplar.com.ctemplarapp.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import mobileapp.ctemplar.com.ctemplarapp.BaseActivity;
import mobileapp.ctemplar.com.ctemplarapp.CTemplarApp;
import mobileapp.ctemplar.com.ctemplarapp.R;
import mobileapp.ctemplar.com.ctemplarapp.login.LoginActivity;
import mobileapp.ctemplar.com.ctemplarapp.main.MainActivityViewModel;
import mobileapp.ctemplar.com.ctemplarapp.repository.UserStore;
import mobileapp.ctemplar.com.ctemplarapp.utils.AppUtils;
import mobileapp.ctemplar.com.ctemplarapp.utils.DisplayUtils;
import mobileapp.ctemplar.com.ctemplarapp.view.pinlock.KeypadAdapter;
import mobileapp.ctemplar.com.ctemplarapp.view.pinlock.KeypadView;
import mobileapp.ctemplar.com.ctemplarapp.view.pinlock.PasscodeView;
import timber.log.Timber;

public class PINLockActivity extends BaseActivity {
    private static final String ALLOW_BACK_KEY = "allow_back";
    private static final int ATTEMPTS_TIMEOUT = 120;
    private static final int ATTEMPTS_COUNT = 3;
    private static final int ATTEMPTS_TOTAL_COUNT = 9;

    @BindView(R.id.activity_pin_lock_pass_code_view)
    PasscodeView passcodeView;

    @BindView(R.id.activity_pin_lock_key_pad_view)
    KeypadView keypadView;

    @BindView(R.id.activity_pin_lock_hint_text_view)
    TextView hintTextView;

    private MainActivityViewModel mainModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pin_lock;
    }

    private UserStore userStore;
    private boolean canBackPress = false;

    private final KeypadAdapter.KeypadListener mKeypadListener = new KeypadAdapter.KeypadListener() {
        @Override
        public void onComplete(String pinCode) {
            if (attemptsTimeout()) {
                return;
            }
            if (userStore.checkPINLock(pinCode)) {
                unlock();
            } else {
                notifyWrongPIN();
            }
        }

        @Override
        public void onPINChanged(int pinLength, String pinCode) {
            if (pinLength == 1) {
                hintTextView.setText("");
                hintTextView.setTextColor(ContextCompat.getColor(PINLockActivity.this,
                        R.color.secondaryTextColor));
            }
        }

        @Override
        public void onEmpty() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        if (intent != null) {
            canBackPress = intent.getBooleanExtra(ALLOW_BACK_KEY, false);
        }
        int displayRotation = DisplayUtils.getRotation(this);
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            if (canBackPress) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } else if (displayRotation == Surface.ROTATION_90 || displayRotation == Surface.ROTATION_270) {
                toolbar.setVisibility(View.GONE);
            }
        }
        userStore = CTemplarApp.getUserStore();
        keypadView.attachPasscodeView(passcodeView);
        keypadView.setKeypadListener(mKeypadListener);
        mainModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainModel.getLogoutResponseStatus().observe(this, responseStatus -> startSignInActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (canBackPress) {
            super.onBackPressed();
        }
    }

    public static void requestUnlockRequest(Activity activity, int requestCode) {
        Intent pinIntent = new Intent(activity, PINLockActivity.class);
        pinIntent.putExtra(ALLOW_BACK_KEY, true);
        activity.startActivityForResult(pinIntent, requestCode);
    }

    private void unlock() {
        Timber.d("unlock");
        userStore.setLockAttemptsCount(0);
        CTemplarApp.getInstance().onUnlocked();
        setResult(RESULT_OK);
        finish();
    }

    private boolean attemptsTimeout() {
        int attemptsCount = userStore.getLockAttemptsCount();
        long lastAttemptTimeDiff = (System.currentTimeMillis() - userStore.getLockLastAttemptTime()) / 1000;

        if (attemptsCount > ATTEMPTS_TOTAL_COUNT) {
            userStore.setLockAttemptsCount(0);
            mainModel.logout();
            return true;
        }

        if (attemptsCount > 0 && attemptsCount % ATTEMPTS_COUNT == 0 && lastAttemptTimeDiff < ATTEMPTS_TIMEOUT) {
            keypadView.resetKeypadView();
            hintTextView.setText(getString(R.string.invalid_pin_timeout,
                    ATTEMPTS_TIMEOUT - lastAttemptTimeDiff));
            hintTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            return true;
        }

        userStore.updateLockLastAttemptTime();
        userStore.setLockAttemptsCount(attemptsCount + 1);

        return false;
    }

    private void notifyWrongPIN() {
        Timber.d("notifyWrongPin");
        keypadView.resetKeypadView();
        hintTextView.setText(R.string.invalid_pin);
        hintTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        AppUtils.vibrate(getBaseContext(), 500);
    }

    private void startSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
