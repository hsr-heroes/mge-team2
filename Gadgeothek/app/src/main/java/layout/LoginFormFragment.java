package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ch.hsr_heroes.gadgeothek.R;

public class LoginFormFragment extends Fragment {
    private View view;
    private EditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.view = getView();

        this.inputEmail = (EditText) view.findViewById(R.id.inputEmail);
        this.inputPassword = (EditText) view.findViewById(R.id.inputPassword);

        this.inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.inputLayoutEmail);
        this.inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.inputLayoutPassword);

        inputEmail.addTextChangedListener(new LocalTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new LocalTextWatcher(inputPassword));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_form, container, false);
    }

    private void validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            this.inputLayoutEmail.setError("Please enter a valid email address");
        } else {
            this.inputLayoutEmail.setErrorEnabled(false);
        }
    }

    private void validatePassword() {
        String password = inputPassword.getText().toString().trim();

        if (password.isEmpty()) {
            this.inputLayoutPassword.setError("Please enter a valid password");
        } else {
            this.inputLayoutPassword.setErrorEnabled(false);
        }
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class LocalTextWatcher implements TextWatcher {

        private View view;

        public LocalTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.inputEmail:
                    validateEmail();
                    break;
                case R.id.inputPassword:
                    validatePassword();
                    break;
            }
        }
    }
}
