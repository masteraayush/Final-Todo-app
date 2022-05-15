package com.aayush.journeyjournal.dashboard;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.aayush.journeyjournal.R;
import com.aayush.journeyjournal.fragments.AddFragment;
import com.aayush.journeyjournal.fragments.HomeFragment;
import com.aayush.journeyjournal.fragments.ProfileFragment;

@SuppressWarnings("ALL")
public class DashboardMain extends AppCompatActivity {

    // get elements
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // default initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);

        // fragment navigation on click from the bottom navigation bar
        navigationView = findViewById(R.id.btm_nav_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.nav_add_entry:
                        fragment = new AddFragment();
                        break;

                    case R.id.nav_profile:;
                        fragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                return true;
            }
        });
    }

}