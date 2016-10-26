package ch.hsr_heroes.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.inputEmail = (TextInputEditText) findViewById(R.id.inputEmail);
        this.inputPassword = (TextInputEditText) findViewById(R.id.inputPassword);

        this.inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayoutEmail);
        this.inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayoutPassword);

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

        Button buttonLogin = (Button) findViewById(R.id.login);

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
