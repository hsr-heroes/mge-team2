package ch.hsr_heroes.gadgeothek;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public class LoginActivity extends AppCompatActivity implements CustomServerPartialFragment.CustomServerListener {
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private Button buttonLogin;
    private CustomServerPartialFragment customServerFragment;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof CustomServerPartialFragment) {
            customServerFragment = (CustomServerPartialFragment) fragment;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (TextInputEditText) findViewById(R.id.input_email);
        inputPassword = (TextInputEditText) findViewById(R.id.input_password);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

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
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();


                LibraryService.setServerAddress(customServerFragment.getServer());

                LibraryService.login(email, password, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean loggedIn) {
                        if (loggedIn) {
                            startActivity(new Intent(LoginActivity.this, GadgetListActivity.class));
                            Toast.makeText(LoginActivity.this, R.string.login_successful, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(LoginActivity.this, getString(R.string.no_server_connection, message), Toast.LENGTH_LONG).show();
                    }
                });
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
