package ch.hsr_heroes.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public class LoginActivity extends AppCompatActivity implements CustomServerPartialFragment.CustomServerListener {
    private TextInputEditText inputEmail, inputPassword, inputCustomURI;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword, inputLayoutCustomServerURI;
    private SwitchCompat switchCustomServer;
    private Button buttonLogin;
    private CustomServerPartialFragment customServerFragment;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof CustomServerPartialFragment) {
            this.customServerFragment = (CustomServerPartialFragment) fragment;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (TextInputEditText) findViewById(R.id.inputEmail);
        inputPassword = (TextInputEditText) findViewById(R.id.inputPassword);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayoutPassword);

        switchCustomServer = (SwitchCompat) findViewById(R.id.switch_custom_server);

        buttonLogin = (Button) findViewById(R.id.login);

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
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();


                LibraryService.setServerAddress(customServerFragment.getServer());

                LibraryService.login(email, password, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean loggedIn) {
                        if(loggedIn) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "Login succeeded", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(LoginActivity.this, "Could not connect to server\n" + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String authToken = preferences.getString("auth_token", null);
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

    @Override
    public void onServerChanged(String newServer, boolean valid) {
        buttonLogin.setEnabled(valid);
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

            // Disable submit button if an error is present or field is empty
            // https://material.google.com/patterns/errors.html#errors-user-input-errors
            if (inputLayoutEmail.isErrorEnabled() ||
                    inputLayoutPassword.isErrorEnabled() ||
                    (customServerFragment.isChecked() && customServerFragment.hasError())) {
                buttonLogin.setEnabled(false);
            } else {
                buttonLogin.setEnabled(true);
            }
        }
    }

}
