package ch.hsr_heroes.gadgeothek;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public abstract class BaseAppStartActivity extends AppCompatActivity implements CustomServerPartialFragment.CustomServerListener{
    protected CustomServerPartialFragment customServerFragment;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof CustomServerPartialFragment) {
            customServerFragment = (CustomServerPartialFragment) fragment;
        }
    }

    protected void doLogIn(final String email, String password, final String serverUrl) {
        LibraryService.setServerAddress(serverUrl);

        LibraryService.login(email, password, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean loggedIn) {
                if (loggedIn) {
                    getPreferences(Context.MODE_PRIVATE).edit()
                            .putString(Prefs.LAST_EMAIL, email)
                            .putString(Prefs.LAST_SERVER, customServerFragment.getServer())
                            .putBoolean(Prefs.CUSTOM_SERVER, customServerFragment.isCustomServerUsed())
                            .commit();
                    startActivity(new Intent(BaseAppStartActivity.this, GadgetListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME));
                    Toast.makeText(BaseAppStartActivity.this, R.string.login_successful, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BaseAppStartActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(BaseAppStartActivity.this, getString(R.string.no_server_connection, message), Toast.LENGTH_LONG).show();
            }
        });
    }
}
