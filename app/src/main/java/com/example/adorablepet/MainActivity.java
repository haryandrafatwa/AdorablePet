package com.example.adorablepet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public static Context contextOfApplication;
    private NestedScrollView nestedScrollView;
    private ImageButton ib_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOfApplication = getApplicationContext();

        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        bottomNavigationView.setItemIconTintList(null);
        nestedScrollView = findViewById(R.id.main_view);
        nestedScrollView.setVisibility(View.VISIBLE);

        final LostFragment lostFragment = new LostFragment();
        final AdoptFragment adoptFragment = new AdoptFragment();
        final CareFragment careFragment = new CareFragment();
        final DonateFragment donateFragment = new DonateFragment();
        final ShelterFragment shelterFragment = new ShelterFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {

                int id = menuItem.getItemId();
                if (id == R.id.menuLost) {
                    setFragment(lostFragment);
                    nestedScrollView.setVisibility(View.GONE);
                    return true;
                } else if (id == R.id.menuAdopt) {
                    setFragment(adoptFragment);
                    nestedScrollView.setVisibility(View.GONE);
                    return true;
                } else if (id == R.id.menuCare) {
                    setFragment(careFragment);
                    nestedScrollView.setVisibility(View.GONE);
                    return true;
                } else if (id == R.id.menuDonate) {
                    setFragment(donateFragment);
                    nestedScrollView.setVisibility(View.GONE);
                    return true;
                } else if (id == R.id.menuShelter) {
                    setFragment(shelterFragment);
                    nestedScrollView.setVisibility(View.GONE);
                    return true;
                }
                return  false;
            }

        });

        ib_setting = findViewById(R.id.ib_button_setting);
        ib_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                setFragment(settingFragment);
            }
        });

    }
    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment);
        fragmentTransaction.commit();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
