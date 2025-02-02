package mobileapp.ctemplar.com.ctemplarapp.contacts;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import mobileapp.ctemplar.com.ctemplarapp.BaseActivity;
import mobileapp.ctemplar.com.ctemplarapp.R;
import mobileapp.ctemplar.com.ctemplarapp.net.ResponseStatus;
import mobileapp.ctemplar.com.ctemplarapp.net.response.contacts.ContactData;
import mobileapp.ctemplar.com.ctemplarapp.utils.EditTextUtils;

public class AddContactActivity extends BaseActivity {
    @BindView(R.id.activity_add_contact_name_input)
    EditText editTextContactName;

    @BindView(R.id.activity_add_contact_email_input)
    EditText editTextContactEmail;

    @BindView(R.id.activity_add_contact_phone_input)
    EditText editTextContactPhoneNumber;

    @BindView(R.id.activity_add_contact_phone_input_second)
    EditText editTextContactPhoneNumberSecond;

    @BindView(R.id.activity_add_contact_address_input)
    EditText editTextContactAddress;

    @BindView(R.id.activity_add_contact_note_input)
    EditText editTextContactNote;

    @BindView(R.id.add_contact_toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_contact;
    }

    private AddContactViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(AddContactViewModel.class);
        viewModel.getResponseStatus().observe(this, this::handleResponse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_contact:
                saveContact();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void handleResponse(ResponseStatus responseStatus) {
        if (responseStatus == null || responseStatus == ResponseStatus.RESPONSE_ERROR) {
            Toast.makeText(this, R.string.toast_not_saved, Toast.LENGTH_SHORT).show();
        } else if (responseStatus == ResponseStatus.RESPONSE_COMPLETE) {
            onBackPressed();
        } else {
            Toast.makeText(this, R.string.toast_undefined_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveContact() {
        String contactName = EditTextUtils.getText(editTextContactName);
        String contactEmail = EditTextUtils.getText(editTextContactEmail);
        String contactPhone = EditTextUtils.getText(editTextContactPhoneNumber);
        String contactPhoneSecond = EditTextUtils.getText(editTextContactPhoneNumberSecond);
        String contactAddress = EditTextUtils.getText(editTextContactAddress);
        String contactNote = EditTextUtils.getText(editTextContactNote);

        if (contactName.isEmpty()) {
            editTextContactName.setError(getString(R.string.txt_enter_name));
        } else {
            editTextContactName.setError(null);
        }
        if (Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches()) {
            editTextContactEmail.setError(null);
        } else {
            editTextContactEmail.setError(getString(R.string.txt_enter_valid_email));
        }
        if (editTextContactName.getError() != null || editTextContactEmail.getError() != null) {
            return;
        }

        ContactData contactData = new ContactData();
        contactData.setName(contactName);
        contactData.setEmail(contactEmail);
        contactData.setPhone(contactPhone);
        contactData.setPhone2(contactPhoneSecond);
        contactData.setAddress(contactAddress);
        contactData.setNote(contactNote);

        viewModel.saveContact(contactData);
    }
}
