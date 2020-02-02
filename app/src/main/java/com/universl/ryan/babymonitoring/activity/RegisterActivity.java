package com.universl.ryan.babymonitoring.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.universl.ryan.babymonitoring.R;
import com.universl.ryan.babymonitoring.helper.InputValidation;
import com.universl.ryan.babymonitoring.model.User;
import com.universl.ryan.babymonitoring.sql.DatabaseHelper;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayout_Name;
    private TextInputLayout textInputLayout_Email;
    private TextInputLayout textInputLayout_Password;
    private TextInputLayout textInputLayoutConfirm_Password;

    private TextInputEditText textInputEditText_Name;
    private TextInputEditText textInputEditText_Email;
    private TextInputEditText textInputEditText_Password;
    private TextInputEditText textInputEditTextConfirm_Password;

    private AppCompatButton appCompatButton_Register;
    private AppCompatTextView appCompatTextView_LoginLink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        init_Views();
        init_Listeners();
        init_Objects();
    }

    /**
     * initialize views
     */
    private void init_Views() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayout_Name = findViewById(R.id.textInputLayoutName);
        textInputLayout_Email = findViewById(R.id.textInputLayoutEmail);
        textInputLayout_Password = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirm_Password = findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditText_Name = findViewById(R.id.textInputEditTextName);
        textInputEditText_Email = findViewById(R.id.textInputEditTextEmail);
        textInputEditText_Password = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirm_Password = findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButton_Register = findViewById(R.id.appCompatButtonRegister);

        appCompatTextView_LoginLink = findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void init_Listeners() {
        appCompatButton_Register.setOnClickListener(this);
        appCompatTextView_LoginLink.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void init_Objects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void postDataToSQLite() {
        if (inputValidation.isInputEditText_Filled(textInputEditText_Name, textInputLayout_Name, getString(R.string.error_message_name))) {
            return;
        }
        if (inputValidation.isInputEditText_Filled(textInputEditText_Email, textInputLayout_Email, getString(R.string.error_message_email))) {
            return;
        }
        if (inputValidation.isInputEditText_Email(textInputEditText_Email, textInputLayout_Email, getString(R.string.error_message_email))) {
            return;
        }
        if (inputValidation.isInputEditText_Filled(textInputEditText_Password, textInputLayout_Password, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditText_Matches(textInputEditText_Password, textInputEditTextConfirm_Password,
                textInputLayoutConfirm_Password, getString(R.string.error_password_match))) {
            return;
        }

        if (!databaseHelper.checkUser(Objects.requireNonNull(textInputEditText_Email.getText()).toString().trim())) {

            user.setName(Objects.requireNonNull(textInputEditText_Name.getText()).toString().trim());
            user.setEmail(textInputEditText_Email.getText().toString().trim());
            user.setPassword(Objects.requireNonNull(textInputEditText_Password.getText()).toString().trim());

            databaseHelper.addUser(user); // insert the database

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditText_Name.setText(null);
        textInputEditText_Email.setText(null);
        textInputEditText_Password.setText(null);
        textInputEditTextConfirm_Password.setText(null);
    }
}
