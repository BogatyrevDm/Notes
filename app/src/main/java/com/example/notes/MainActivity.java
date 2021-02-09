package com.example.notes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.notes.observe.Publisher;
import com.example.notes.ui.AboutFragment;
import com.example.notes.ui.NotesFragment;
import com.example.notes.ui.SettingsFragment;
import com.example.notes.ui.StartFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String AUTH_STATE = "authorized";
    private Publisher publisher = new Publisher();
    private boolean isAuthorized;

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState != null) {
            setAuthorized(savedInstanceState.getBoolean(AUTH_STATE));
        } else {
            setAuthorized(false);
        }
        if (isAuthorized()) {
            FragmentHandler.replaceFragment(MainActivity.this, new NotesFragment(), R.id.notes, false, true, false);
        } else {
            FragmentHandler.replaceFragment(MainActivity.this, StartFragment.newInstance(), R.id.notes, false, false, false);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_STATE, isAuthorized());
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.action_settings:
                        FragmentHandler.replaceFragment(MainActivity.this, new SettingsFragment(), FragmentHandler.getIdFromOrientation(MainActivity.this), true, false, false);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.action_about:
                        FragmentHandler.replaceFragment(MainActivity.this, new AboutFragment(), FragmentHandler.getIdFromOrientation(MainActivity.this), true, false, false);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                }

                return false;
            }
        });
    }

    private void initView() {
        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);
    }

    private Toolbar initToolBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    public Publisher getPublisher() {
        return publisher;
    }


}