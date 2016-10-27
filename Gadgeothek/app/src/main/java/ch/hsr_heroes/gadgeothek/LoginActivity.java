package ch.hsr_heroes.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText inputEmail, inputPassword, inputCustomURI;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword, inputLayoutCustomServerURI;
    private SwitchCompat switchCustomServer;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.inputEmail = (TextInputEditText) findViewById(R.id.inputEmail);
        this.inputPassword = (TextInputEditText) findViewById(R.id.inputPassword);
        this.inputCustomURI = (TextInputEditText) findViewById(R.id.input_custom_server_uri);

        this.inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayoutEmail);
        this.inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayoutPassword);
        this.inputLayoutCustomServerURI = (TextInputLayout) findViewById(R.id.input_layout_custom_server_uri);

        this.switchCustomServer = (SwitchCompat) findViewById(R.id.switch_custom_server);

        this.buttonLogin = (Button) findViewById(R.id.login);

        inputEmail.addTextChangedListener(new LocalTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new LocalTextWatcher(inputPassword));
        inputCustomURI.addTextChangedListener(new LocalTextWatcher(inputCustomURI));

        Button buttonSignUp = (Button) findViewById(R.id.create_an_account);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });


        final SwitchCompat switchCustomServer = this.switchCustomServer;
        final TextInputLayout customServer = this.inputLayoutCustomServerURI;

        switchCustomServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCustomServer.isChecked()) {
                    customServer.setVisibility(View.VISIBLE);
                } else {
                    customServer.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String authToken = preferences.getString("auth_token", null);
    }

    private void validateURI() {
        String uriString = inputCustomURI.getText().toString().trim();

        Uri uri = Uri.parse(uriString);

        if (uri.toString().length() < 5) {
            inputLayoutCustomServerURI.setError("Please enter a valid URI");
        } else {
            inputLayoutCustomServerURI.setErrorEnabled(false);
        }

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
                case R.id.input_custom_server_uri:
                    validateURI();
                    break;
            }

            // Disable submit button if an error is present
            // https://material.google.com/patterns/errors.html#errors-user-input-errors
            if (LoginActivity.this.inputLayoutEmail.isErrorEnabled() ||
                    LoginActivity.this.inputLayoutPassword.isErrorEnabled() ||
                    (LoginActivity.this.switchCustomServer.isChecked() && LoginActivity.this.inputLayoutCustomServerURI.isErrorEnabled())) {
                LoginActivity.this.buttonLogin.setEnabled(false);
            } else {
                LoginActivity.this.buttonLogin.setEnabled(true);
            }
        }
    }

}
