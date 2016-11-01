package ch.hsr_heroes.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends BaseAppStartActivity {
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (TextInputEditText) findViewById(R.id.input_email);
        inputPassword = (TextInputEditText) findViewById(R.id.input_password);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        inputEmail.setText(getPreferences(Context.MODE_PRIVATE).getString(Prefs.LAST_EMAIL, ""));

        buttonLogin = (Button) findViewById(R.id.button_login);

        inputEmail.addTextChangedListener(new LocalTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new LocalTextWatcher(inputPassword));

        Button buttonSignUp = (Button) findViewById(R.id.create_an_account);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogIn(inputEmail.getText().toString(), inputPassword.getText().toString(), LoginActivity.this.customServerFragment.getServer());
            }
        });

    }

    @Override
    public void onServerChanged(String newServer, boolean valid) {
        buttonLogin.setEnabled(isValidForm());
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
                case R.id.input_email:
                    ValidationHelper.validateEmail(LoginActivity.this, inputLayoutEmail, inputEmail);
                    break;
                case R.id.input_password:
                    ValidationHelper.validatePassword(LoginActivity.this, inputLayoutPassword, inputPassword);
                    break;
            }

            // Disable submit button if an error is present or field is empty
            // https://material.google.com/patterns/errors.html#errors-user-input-errors
            buttonLogin.setEnabled(isValidForm());
        }
    }

    private boolean isValidForm() {
        return (ValidationHelper.isValidEmail(inputEmail) &&
                ValidationHelper.isValidPassword(inputPassword) &&
                (!customServerFragment.isChecked() || (customServerFragment.isChecked() && customServerFragment.isValid())));
    }

}
