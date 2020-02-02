package com.universl.ryan.babymonitoring.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.universl.ryan.babymonitoring.MainActivity;
import com.universl.ryan.babymonitoring.R;
import com.universl.ryan.babymonitoring.helper.InputValidation;
import com.universl.ryan.babymonitoring.sql.DatabaseHelper;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayout_Email;
    private TextInputLayout textInputLayout_Password;

    private TextInputEditText textInputEditText_Email;
    private TextInputEditText textInputEditText_Password;

    private AppCompatButton appCompatButton_Login;

    private AppCompatTextView textViewLink_Register;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        init_Views();
        init_Listeners();
        init_Objects();
    }

    /**
     * This method is to initialize views
     */
    private void init_Views() {

        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayout_Email = findViewById(R.id.textInputLayoutEmail);
        textInputLayout_Password = findViewById(R.id.textInputLayoutPassword);

        textInputEditText_Email = findViewById(R.id.textInputEditTextEmail);
        textInputEditText_Password = findViewById(R.id.textInputEditTextPassword);

        appCompatButton_Login = findViewById(R.id.appCompatButtonLogin);

        textViewLink_Register = findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void init_Listeners() {
        appCompatButton_Login.setOnClickListener(this);
        textViewLink_Register.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void init_Objects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /**
     * Validate the input text fields and verify login credentials from SQLite
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void verifyFromSQLite() {
        if (inputValidation.isInputEditText_Filled(textInputEditText_Email, textInputLayout_Email, getString(R.string.error_message_email))) {
            return;
        }
        if (inputValidation.isInputEditText_Email(textInputEditText_Email, textInputLayout_Email, getString(R.string.error_message_email))) {
            return;
        }
        if (inputValidation.isInputEditText_Filled(textInputEditText_Password, textInputLayout_Password, getString(R.string.error_message_email))) {
            return;
        }

        if (databaseHelper.checkUser(Objects.requireNonNull(textInputEditText_Email.getText()).toString().trim()
                , Objects.requireNonNull(textInputEditText_Password.getText()).toString().trim())) {


            Intent accountsIntent = new Intent(activity, MainActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditText_Email.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);


        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    //Empty all input edit text

    private void emptyInputEditText() {
        textInputEditText_Email.setText(null);
        textInputEditText_Password.setText(null);
    }
}

