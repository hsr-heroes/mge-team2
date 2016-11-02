package ch.hsr_heroes.gadgeothek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;

    protected abstract void onCreateMainContent(ViewGroup contentView);

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onCreateMainContent((ViewGroup) findViewById(R.id.content_main));

        TextView textUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);

        if (textUserEmail != null) {
            textUserEmail.setText(LibraryService.getEmail());
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_my_gadgets) {
            navigateTo(MyLoansListActivity.class);
        } else if (id == R.id.nav_my_reservations) {
            navigateTo(MyReservationsActivity.class);
        } else if (id == R.id.nav_all_gadgets) {
            navigateTo(GadgetListActivity.class);
        } else if (id == R.id.nav_logout) {
            LibraryService.logout(new Callback<Boolean>() {
                @Override
                public void onCompletion(Boolean input) {
                    navigateTo(LoginActivity.class);
                    showToastMessage(R.string.sucessful_logout);
                }

                @Override
                public void onError(String message) {
                    showToastMessage(getString(R.string.logout_error, message));
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateTo(Class<? extends Activity> targetActivity) {
        startActivity(new Intent(BaseActivity.this, targetActivity).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
    }


    protected void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    protected void showToastMessage(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_LONG).show();
    }

    protected void setActiveMenuItem(int itemId) {
        navigationView.setCheckedItem(itemId);
    }
}
