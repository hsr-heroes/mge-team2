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

public class SignUpActivity extends BaseAppStartActivity {
    private TextInputEditText inputName;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private TextInputEditText inputStudentNumber;
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private TextInputLayout inputLayoutStudentNumber;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        customServerFragment.setDescription(getString(R.string.register_on_custom_server));

        inputName = (TextInputEditText) findViewById(R.id.input_name);
        inputEmail = (TextInputEditText) findViewById(R.id.input_email);
        inputPassword = (TextInputEditText) findViewById(R.id.input_password);
        inputStudentNumber = (TextInputEditText) findViewById(R.id.input_student_number);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutStudentNumber = (TextInputLayout) findViewById(R.id.input_layout_student_number);

        inputName.addTextChangedListener(new SignUpActivity.LocalTextWatcher(inputName));
        inputEmail.addTextChangedListener(new SignUpActivity.LocalTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new SignUpActivity.LocalTextWatcher(inputPassword));
        inputStudentNumber.addTextChangedListener(new SignUpActivity.LocalTextWatcher(inputStudentNumber));

        buttonSignUp = (Button) findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = inputName.getText().toString();
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String studentNumber = inputStudentNumber.getText().toString();

                LibraryService.setServerAddress(customServerFragment.getServer());

                LibraryService.register(email, password, name, studentNumber, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean registered) {
                        if (registered) {
                            doLogIn(email, password, customServerFragment.getServer());
                        } else {
                            Toast.makeText(SignUpActivity.this, R.string.registration_failed, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(SignUpActivity.this, getString(R.string.no_server_connection, message), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onServerChanged(String newServer, boolean valid) {
        buttonSignUp.setEnabled(isValidForm());
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
                case R.id.input_name:
                    ValidationHelper.validateName(SignUpActivity.this, inputLayoutName, inputName);
                    break;
                case R.id.input_email:
                    ValidationHelper.validateEmail(SignUpActivity.this, inputLayoutEmail, inputEmail);
                    break;
                case R.id.input_password:
                    ValidationHelper.validatePassword(SignUpActivity.this, inputLayoutPassword, inputPassword);
                    break;
                case R.id.input_student_number:
                    ValidationHelper.validateStudentNumber(SignUpActivity.this, inputLayoutStudentNumber, inputStudentNumber);
                    break;
            }

            // Disable submit button if an error is present or field is empty
            // https://material.google.com/patterns/errors.html#errors-user-input-errors
            buttonSignUp.setEnabled(isValidForm());
        }
    }

    private boolean isValidForm() {
        return (ValidationHelper.isValidName(inputName) &&
                ValidationHelper.isValidEmail(inputEmail) &&
                ValidationHelper.isValidPassword(inputPassword) &&
                ValidationHelper.isValidStudentNumber(inputStudentNumber) &&
                (!customServerFragment.isChecked() || (customServerFragment.isChecked() && customServerFragment.isValid())));
    }
}
