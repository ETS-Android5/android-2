package mobileapp.ctemplar.com.ctemplarapp.folders;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import mobileapp.ctemplar.com.ctemplarapp.BaseActivity;
import mobileapp.ctemplar.com.ctemplarapp.R;
import mobileapp.ctemplar.com.ctemplarapp.net.ResponseStatus;
import mobileapp.ctemplar.com.ctemplarapp.utils.EditTextUtils;

public class AddFolderActivity extends BaseActivity {

    final private static String PICKED_COLOR_1 = "#7272a8";
    final private static String PICKED_COLOR_2 = "#d05859";
    final private static String PICKED_COLOR_3 = "#c26cc7";
    final private static String PICKED_COLOR_4 = "#7568d1";
    final private static String PICKED_COLOR_5 = "#6aa9d2";
    final private static String PICKED_COLOR_6 = "#5fc7b8";
    final private static String PICKED_COLOR_7 = "#72bb74";
    final private static String PICKED_COLOR_8 = "#72bb74";
    final private static String PICKED_COLOR_9 = "#e6c14c";
    final private static String PICKED_COLOR_10 = "#e6994d";
    final private static String PICKED_COLOR_11 = "#cf7e7d";
    final private static String PICKED_COLOR_12 = "#c893cb";
    final private static String PICKED_COLOR_13 = "#9c94d0";
    final private static String PICKED_COLOR_14 = "#a9c4d5";

    private AddFolderViewModel addFolderModel;

    @BindView(R.id.activity_add_folder_action_add)
    Button buttonAddFolder;
    @BindView(R.id.activity_add_folder_colors_layout)
    RadioButtonTableLayout radioGroupColor;
    @BindView(R.id.folder_color_1)
    RadioButton firstRadioButton;
    @BindView(R.id.activity_add_folder_input)
    EditText editTextFolderName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_folder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.activity_add_folder_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null ) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addFolderModel = ViewModelProviders.of(this).get(AddFolderViewModel.class);
        addFolderModel.getResponseStatus().observe(this, responseStatus -> {
            if (responseStatus == null || responseStatus == ResponseStatus.RESPONSE_ERROR) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_folder_not_created), Toast.LENGTH_SHORT).show();
            } else if (responseStatus == ResponseStatus.RESPONSE_COMPLETE) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_folder_created), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        editTextFolderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextFolderName.getText().toString().length() > 3) {
                    buttonAddFolder.setBackgroundResource(R.color.colorDarkBlue2);
                } else {
                    buttonAddFolder.setBackgroundResource(R.color.colorGreyLight3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        buttonAddFolder.setBackgroundResource(R.color.colorGreyLight3);
        radioGroupColor.setActive(firstRadioButton);
    }

    @OnClick(R.id.activity_add_folder_action_add)
    public void OnClickAddFolder() {
        String folderName = editTextFolderName.getText().toString();
        String folderColor = "";

        if (radioGroupColor.getCheckedRadioButtonId() != -1) {
            int selectedColor = radioGroupColor.getCheckedRadioButtonId();
            folderColor = getPickerColor(selectedColor);
        }

        if (EditTextUtils.isTextValid(folderName) && EditTextUtils.isTextLength(folderName, 4, 30)) {
            addFolderModel.addFolder(folderName, folderColor);
            editTextFolderName.setError(null);
        } else {
            editTextFolderName.setError(getResources().getString(R.string.txt_folder_name_hint));
        }
    }

    public static String getPickerColor(int selectedColor) {
        String folderColor = PICKED_COLOR_1;

        if (selectedColor == R.id.folder_color_2) {
            folderColor = PICKED_COLOR_2;
        } else if (selectedColor == R.id.folder_color_3) {
            folderColor = PICKED_COLOR_3;
        } else if (selectedColor == R.id.folder_color_4) {
            folderColor = PICKED_COLOR_4;
        } else if (selectedColor == R.id.folder_color_5) {
            folderColor = PICKED_COLOR_5;
        } else if (selectedColor == R.id.folder_color_6) {
            folderColor = PICKED_COLOR_6;
        } else if (selectedColor == R.id.folder_color_7) {
            folderColor = PICKED_COLOR_7;
        } else if (selectedColor == R.id.folder_color_8) {
            folderColor = PICKED_COLOR_8;
        } else if (selectedColor == R.id.folder_color_9) {
            folderColor = PICKED_COLOR_9;
        } else if (selectedColor == R.id.folder_color_10) {
            folderColor = PICKED_COLOR_10;
        } else if (selectedColor == R.id.folder_color_11) {
            folderColor = PICKED_COLOR_11;
        } else if (selectedColor == R.id.folder_color_12) {
            folderColor = PICKED_COLOR_12;
        } else if (selectedColor == R.id.folder_color_13) {
            folderColor = PICKED_COLOR_13;
        } else if (selectedColor == R.id.folder_color_14) {
            folderColor = PICKED_COLOR_14;
        }

        return folderColor;
    }

    @OnClick(R.id.activity_add_folder_action_cancel)
    public void OnClickCancel() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
