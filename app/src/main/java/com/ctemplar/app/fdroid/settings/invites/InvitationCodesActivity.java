package com.ctemplar.app.fdroid.settings.invites;

import static com.ctemplar.app.fdroid.settings.SettingsActivity.USER_IS_PRIME;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.ctemplar.app.fdroid.BaseActivity;
import com.ctemplar.app.fdroid.R;
import com.ctemplar.app.fdroid.databinding.ActivityInvitationCodesBinding;
import com.ctemplar.app.fdroid.repository.dto.DTOResource;
import com.ctemplar.app.fdroid.repository.dto.PagableDTO;
import com.ctemplar.app.fdroid.repository.dto.invites.InviteCodeDTO;
import com.ctemplar.app.fdroid.utils.ThemeUtils;
import com.ctemplar.app.fdroid.utils.ToastUtils;

import java.util.List;

public class InvitationCodesActivity extends BaseActivity {
    private ActivityInvitationCodesBinding binding;
    private InvitationCodesViewModel viewModel;
    private InvitationCodesAdapter invitationCodesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.setTheme(this);
        binding = ActivityInvitationCodesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(InvitationCodesViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        if (intent != null) {
            boolean prime = intent.getBooleanExtra(USER_IS_PRIME, false);
            showNote(!prime);
            binding.generateCodeButton.setEnabled(prime);
        }
        invitationCodesAdapter = new InvitationCodesAdapter();
        binding.codesRecyclerView.setAdapter(invitationCodesAdapter);
        binding.generateCodeButton.setOnClickListener(v -> generateInviteCode());
        viewModel.getInviteCodesLiveData().observe(this, this::handleInviteCodes);
        requestInviteCodes();
        showList(false);
    }

    private void handleInviteCodes(DTOResource<PagableDTO<InviteCodeDTO>> resource) {
        showProgressBar(false);
        if (!resource.isSuccess()) {
            ToastUtils.showToast(this, resource.getError());
            return;
        }
        List<InviteCodeDTO> items = resource.getDto().getResults();
        invitationCodesAdapter.setItems(items);
        showList(!items.isEmpty());
    }

    private void requestInviteCodes() {
        viewModel.getInviteCodes(10, 0);
    }

    private void generateInviteCode() {
        showProgressBar(true);
        binding.generateCodeButton.setEnabled(false);
        viewModel.generateInviteCode().observe(this, resource -> {
            showProgressBar(false);
            binding.generateCodeButton.setEnabled(true);
            if (!resource.isSuccess()) {
                ToastUtils.showToast(getApplicationContext(), resource.getError());
                return;
            }
            requestInviteCodes();
        });
    }

    private void showProgressBar(boolean state) {
        binding.progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    private void showList(boolean state) {
        binding.headerLinearLayout.setVisibility(state ? View.VISIBLE : View.GONE);
        binding.codesRecyclerView.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    private void showNote(boolean state) {
        binding.noteTextView.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
