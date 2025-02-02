package mobileapp.ctemplar.com.ctemplarapp.login;

import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.OnClick;
import mobileapp.ctemplar.com.ctemplarapp.BaseFragment;
import mobileapp.ctemplar.com.ctemplarapp.LoginActivityActions;
import mobileapp.ctemplar.com.ctemplarapp.R;
import mobileapp.ctemplar.com.ctemplarapp.net.ResponseStatus;
import mobileapp.ctemplar.com.ctemplarapp.utils.HtmlUtils;
import timber.log.Timber;

public class ConfirmResetPasswordFragment extends BaseFragment {
    @BindView(R.id.fragment_confirm_reset_password_hint)
    TextView txtHint;

    private LoginActivityViewModel loginActivityModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_reset_password;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Timber.e("FragmentActivity is null");
            return;
        }

        loginActivityModel = new ViewModelProvider(activity).get(LoginActivityViewModel.class);
        loginActivityModel.getResponseStatus().observe(getViewLifecycleOwner(), this::handleResponseStatus);
        setListeners();
    }

    @OnClick(R.id.fragment_confirm_reset_password_back)
    public void onClickBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.fragment_confirm_reset_password_btn)
    public void onClickConfirm() {
        if (loginActivityModel.getRecoverPasswordRequest() != null) {
            // loginActivityModel.changeAction(LoginActivityActions.SHOW_PROGRESS_DIALOG);
            loginActivityModel.showProgressDialog();
            loginActivityModel.recoverPassword();
        }
    }

    private void setListeners() {
        Spanned resetPasswordHint = HtmlUtils.fromHtml(getString(R.string.title_confirm_reset_password_hint));
        Spanned furtherQuestionsHint = HtmlUtils.fromHtml(getString(R.string.title_further_question_hint));
        txtHint.setText(TextUtils.concat(resetPasswordHint, " ", furtherQuestionsHint));
        txtHint.setLinkTextColor(getResources().getColor(R.color.colorLinkBlue));
        txtHint.setMovementMethod(LinkMovementMethod.getInstance());
        txtHint.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
    }

    public void handleResponseStatus(@Nullable ResponseStatus status) {
        if (status != null) {
            // loginActivityModel.changeAction(LoginActivityActions.HIDE_PROGRESS_DIALOG);
            loginActivityModel.hideProgressDialog();

            switch (status) {
                case RESPONSE_ERROR:
                    Toast.makeText(getActivity(), getString(R.string.error_reset_password), Toast.LENGTH_LONG).show();
                    break;
                case RESPONSE_ERROR_RECOVER_PASS_FAILED:
                    Toast.makeText(getActivity(), getString(R.string.error_recover_password), Toast.LENGTH_LONG).show();
                    break;
                case RESPONSE_NEXT_RECOVER_PASSWORD:
                    loginActivityModel.changeAction(LoginActivityActions.CHANGE_FRAGMENT_RESET_CODE);
                    break;
            }
        }
    }
}
