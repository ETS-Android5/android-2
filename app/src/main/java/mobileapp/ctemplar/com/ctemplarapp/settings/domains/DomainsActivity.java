package mobileapp.ctemplar.com.ctemplarapp.settings.domains;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import mobileapp.ctemplar.com.ctemplarapp.BaseActivity;
import mobileapp.ctemplar.com.ctemplarapp.R;
import mobileapp.ctemplar.com.ctemplarapp.databinding.ActivityCustomDomainsBinding;
import mobileapp.ctemplar.com.ctemplarapp.repository.dto.DTOResource;
import mobileapp.ctemplar.com.ctemplarapp.repository.dto.domains.CustomDomainsDTO;
import mobileapp.ctemplar.com.ctemplarapp.utils.ThemeUtils;
import mobileapp.ctemplar.com.ctemplarapp.utils.ToastUtils;

public class DomainsActivity extends BaseActivity implements DomainsAdapter.ItemClickListener {
    private ActivityCustomDomainsBinding binding;

    private DomainsViewModel domainsViewModel;
    private DomainsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.setTheme(this);
        binding = ActivityCustomDomainsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        domainsViewModel = new ViewModelProvider(this).get(DomainsViewModel.class);
        adapter = new DomainsAdapter();
        adapter.setItemClickListener(this);
        binding.domainsRecyclerView.setAdapter(adapter);
        domainsViewModel.getCustomDomains().observe(this, this::handleCustomDomains);
        binding.addNewDomainButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DomainActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        domainsViewModel.customDomainsRequest();
    }

    private void handleCustomDomains(DTOResource<CustomDomainsDTO> dtoResource) {
        binding.progressBar.setVisibility(View.GONE);
        if (!dtoResource.isSuccess()) {
            ToastUtils.showToast(this, dtoResource.getError());
            return;
        }
        adapter.setItems(dtoResource.getDto().getResults());
    }

    @Override
    public void onCatchAllEmail(int domainId, boolean catchAll, String catchAllEmail) {
        domainsViewModel.updateCustomDomain(domainId, catchAll, catchAllEmail)
                .observe(this, domainDTO -> {
                    if (domainDTO != null) {
                        ToastUtils.showToast(this, R.string.saved_successfully);
                    }
                });
    }

    @Override
    public void onDelete(int domainId, String domainName) {
         new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_domain_title, domainName))
                .setMessage(R.string.delete_domain_dsecription)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteCustomDomain(domainId))
                .setNeutralButton(R.string.btn_cancel, null)
                .show();
    }

    private void deleteCustomDomain(int domainId) {
        domainsViewModel.deleteCustomDomain(domainId).observe(this, booleanDTOResource -> {
            if (!booleanDTOResource.isSuccess()) {
                ToastUtils.showToast(this, booleanDTOResource.getError());
                return;
            }
            domainsViewModel.customDomainsRequest();
        });
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
