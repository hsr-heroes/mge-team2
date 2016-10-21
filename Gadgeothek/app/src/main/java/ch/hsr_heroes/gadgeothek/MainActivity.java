package ch.hsr_heroes.gadgeothek;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import layout.LoginFragment;
import layout.SignUpFormFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnSignUpFragmentRequestListener {
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private SignUpFormFragment signUpFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String authToken = preferences.getString("auth_token", null);

        this.loginFragment = new LoginFragment();

        fragmentTransaction.add(R.id.fragment_container, this.loginFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSignUpFragmentRequest() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        this.signUpFormFragment = new SignUpFormFragment();

        fragmentTransaction.add(R.id.fragment_container, this.signUpFormFragment).addToBackStack("signUpFormFragment");
        fragmentTransaction.hide(this.loginFragment);
        fragmentTransaction.commit();
    }
}
