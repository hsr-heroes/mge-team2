package ch.hsr_heroes.gadgeothek;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import okhttp3.HttpUrl;

class ValidationHelper {

    private static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    static boolean isValidEmail(TextInputEditText editText) {
        return isValidEmail(extractString(editText));
    }

    private static boolean isValidPassword(String password) {
        return !password.isEmpty();
    }

    static boolean isValidPassword(TextInputEditText editText) {
        return isValidPassword(extractString(editText));
    }

    static boolean isValidCustomServer(String customServer) {
        HttpUrl uri = HttpUrl.parse(customServer);

        return !(uri == null);
    }

    static boolean isValidCustomServer(TextInputEditText editText) {
        return isValidCustomServer(extractString(editText));
    }

    private static boolean isEmpty(TextInputEditText editText) {
        return extractString(editText).isEmpty();
    }

    private static String extractString(TextInputEditText editText) {
        return editText.getText().toString().trim();
    }

    static boolean isValidName(TextInputEditText editText) {
        return !extractString(editText).isEmpty();
    }

    static boolean isValidStudentNumber(TextInputEditText editText) {
        try {
            return 0 < Integer.valueOf(extractString(editText));
        } catch(NumberFormatException e) {
            return false;
        }
    }

    static void validateEmail(Context context, TextInputLayout inputLayoutEmail, TextInputEditText inputEmail) {
        if (!ValidationHelper.isEmpty(inputEmail)) {
            if (ValidationHelper.isValidEmail(inputEmail)) {
                inputLayoutEmail.setErrorEnabled(false);
            } else {
                inputLayoutEmail.setError(context.getString(R.string.enter_a_valid_email_address));
            }
        } else {
            inputLayoutEmail.setError(context.getString(R.string.email_address_required));
        }
    }

    static void validatePassword(Context context, TextInputLayout inputLayoutPassword, TextInputEditText inputPassword) {
        if (ValidationHelper.isValidPassword(inputPassword)) {
            inputLayoutPassword.setErrorEnabled(false);
        } else {
            inputLayoutPassword.setError(context.getString(R.string.password_required));
        }
    }

    static void validateName(Context context, TextInputLayout inputLayoutName, TextInputEditText inputName) {
        if (ValidationHelper.isValidName(inputName)) {
            inputLayoutName.setErrorEnabled(false);
        } else {
            inputLayoutName.setError(context.getString(R.string.name_required));
        }
    }

    static void validateStudentNumber(Context context, TextInputLayout inputLayoutStudentNumber, TextInputEditText inputStudentNumber) {
        if (ValidationHelper.isValidStudentNumber(inputStudentNumber)) {
            inputLayoutStudentNumber.setErrorEnabled(false);
        } else {
            inputLayoutStudentNumber.setError(context.getString(R.string.student_number_required));
        }
    }
}
