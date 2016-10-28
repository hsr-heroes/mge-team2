package ch.hsr_heroes.gadgeothek;

import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.util.Patterns;

import okhttp3.HttpUrl;

public class ValidationHelper {

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidEmail(TextInputEditText editText) {
        return isValidEmail(extractString(editText));
    }

    public static boolean isValidPassword(String password) {
        return !password.isEmpty();
    }

    public static boolean isValidPassword(TextInputEditText editText) {
        return isValidPassword(extractString(editText));
    }

    public static boolean isValidCustomServer(String customServer) {
        HttpUrl uri = HttpUrl.parse(customServer);

        return !(uri == null);
    }

    public static boolean isValidCustomServer(TextInputEditText editText) {
        return isValidCustomServer(extractString(editText));
    }

    public static boolean isEmpty(TextInputEditText editText) {
        return extractString(editText).isEmpty();
    }

    private static String extractString(TextInputEditText editText) {
        return editText.getText().toString().trim();
    }

    public static boolean isValidName(TextInputEditText editText) {
        return !extractString(editText).isEmpty();
    }

    public static boolean isValidStudentNumber(TextInputEditText editText) {
        try {
            return 0 < Integer.valueOf(extractString(editText));
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
