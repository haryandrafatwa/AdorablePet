package com.example.adorablepet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

    private RelativeLayout shelter,layout_menu;
    private int numOfNotif;

    private DatabaseReference userRefs,shelterRefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOfApplication = getApplicationContext();


        //inisiasi objek pada halaman ini
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        bottomNavigationView.setItemIconTintList(null);
        nestedScrollView = findViewById(R.id.main_view);
        layout_menu = findViewById(R.id.layout_menu);
        nestedScrollView.setVisibility(View.VISIBLE);

        //inisiasi halaman yang ada pada bottom navigation view
        final LostFragment lostFragment = new LostFragment();
        final AdoptFragment adoptFragment = new AdoptFragment();
        final CareFragment careFragment = new CareFragment();
        final DonateFragment donateFragment = new DonateFragment();
        final ShelterFragment shelterFragment = new ShelterFragment();

        //proses inisiasi bottom navigation view, jika icon diklik, maka akan ke halaman tersebut
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

        //inisiasi objek image button setting dan menginisiasi ketika button di klik, yaitu pindah ke halaman setting
        ib_setting = findViewById(R.id.ib_button_setting);
        ib_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                setFragment(settingFragment);
                nestedScrollView.setVisibility(View.GONE);
            }
        });

        //inisiasi objek image button notif dan menginisiasi ketika button di klik, yaitu pindah ke halaman notif
        ib_notif = findViewById(R.id.ib_button_notification);
        mBadge = findViewById(R.id.notif_badge_main);
        ib_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
                setFragment(notifikasiFragment);
            }
        });

        //code untuk mengambil status notifikasi, yg berguna untuk menghitung banyaknya notif yang terbaca / yg belum terbaca
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

        //inisiasi button shelter, dan inisiasi ketika button di klik, yaitu pindah ke halaman shelter
        shelter = findViewById(R.id.btnShelter);
        shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(shelterFragment);
                nestedScrollView.setVisibility(View.GONE);
            }
        });

        //mengambil informasi dari firebase, apakah user menyalakan notifnya atau tidak, dan apakah user menggunakan tema malam atau tidak
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("notif").getValue().toString().equalsIgnoreCase("on")){
                    mBadge.setVisibility(View.VISIBLE);
                }else{
                    mBadge.setVisibility(View.GONE);
                }

                View view = getWindow().getDecorView();
                if(dataSnapshot.child("theme").getValue().toString().equalsIgnoreCase("on")){
                    view.setBackgroundColor(getResources().getColor(R.color.colorDark));
                    layout_menu.setBackground(getDrawable(R.drawable.bg_btn_solid_black_stroke));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.colorLight));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //method untuk pindah - pindah ke halaman lain
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
