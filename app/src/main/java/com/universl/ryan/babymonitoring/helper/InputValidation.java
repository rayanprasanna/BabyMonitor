package com.universl.ryan.babymonitoring.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

public class InputValidation { // check the errors and validate it.
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isInputEditText_Filled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = Objects.requireNonNull(textInputEditText.getText()).toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return true;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isInputEditText_Email(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = Objects.requireNonNull(textInputEditText.getText()).toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return true;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT) // conform the login password
    public boolean isInputEditText_Matches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = Objects.requireNonNull(textInputEditText1.getText()).toString().trim();
        String value2 = Objects.requireNonNull(textInputEditText2.getText()).toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void hideKeyboardFrom(View view) { // get an error after hiding the keyboard.
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}