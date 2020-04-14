package com.example.adorablepet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public static Context contextOfApplication;
    private NestedScrollView nestedScrollView;
    private ImageButton ib_setting,ib_notif;
    private NotificationBadge mBadge;

    private RelativeLayout shelter;
    private int numOfNotif;

    private DatabaseReference userRefs,shelterRefs;

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

        ib_notif = findViewById(R.id.ib_button_notification);
        mBadge = findViewById(R.id.notif_badge_main);
        ib_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
                setFragment(notifikasiFragment);
            }
        });

        shelterRefs = FirebaseDatabase.getInstance().getReference().child("Shelter").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        shelterRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    numOfNotif=0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (!snapshot.child("status").getValue().toString().equals("Pending")){
                            if(snapshot.hasChild("read") && snapshot.hasChild("date")){
                                if (snapshot.child("read").getValue().toString().equals("false")){
                                    numOfNotif++;
                                }
                            }
                        }
                    }
                }
                mBadge.setNumber(numOfNotif);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        shelter = findViewById(R.id.btnShelter);
        shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(shelterFragment);
                nestedScrollView.setVisibility(View.GONE);
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
