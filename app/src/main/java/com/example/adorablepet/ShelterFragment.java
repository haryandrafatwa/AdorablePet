package com.example.adorablepet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

public class ShelterFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ImageButton ib_back, ib_setting,ib_notif;

    private Button button;
    private NotificationBadge mBadge;
    private int numOfNotif;

    private View view;

    private DatabaseReference shelterRefs,userRefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setActivity(MainActivity.class);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shelter, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    //method untuk menginisiasi semua objek yang ada pada halaman ini beserta dengan ketika ada button yang diclick
    private void initialize(){

        view = getView().getRootView();

        toolbar = getActivity().findViewById(R.id.toolbar_shelter);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle("");

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);

        button = getActivity().findViewById(R.id.btnBookNow);
        ib_back = getActivity().findViewById(R.id.ib_back);
        ib_setting = getActivity().findViewById(R.id.ib_button_setting_shelter);
        ib_notif = getActivity().findViewById(R.id.ib_button_notification_shelter);
        mBadge = getActivity().findViewById(R.id.notif_badge);
        view = getActivity().getWindow().getDecorView();

        ib_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
                setFragment(notifikasiFragment);
            }
        });

        ib_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                setFragment(settingFragment);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookFragment bookFragment = new BookFragment();
                setFragment(bookFragment);
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActivity(MainActivity.class);
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
                        //kondisi untuk mengecek apakah ada transaksi yang sedang berlansung atau tidak, jika iyaa ketika button book ditekan, maka sistem akan menampilkan konfirmasi pembayaran
                        if (snapshot.child("status").getValue().toString().equals("Pending")){
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    KonfirmasiFragment konfirmasiFragment = new KonfirmasiFragment();
                                    setFragment(konfirmasiFragment);
                                }
                            });
                        }else{
                            if (snapshot.hasChild("read") && snapshot.hasChild("date")){
                                if (snapshot.child("read").getValue().toString().equals("false")){
                                    numOfNotif++;
                                }
                            }
                        }
                        button.setVisibility(View.VISIBLE);
                    }
                }else{ //jika tidak, maka sistem akan menampilkan form book
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BookFragment bookFragment = new BookFragment();
                            setFragment(bookFragment);
                        }
                    });
                    button.setVisibility(View.VISIBLE);
                }
                mBadge.setNumber(numOfNotif);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //mengambil informasi dari firebase, apakah user menyalakan notifnya atau tidak, dan apakah user menggunakan tema malam atau tidak
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("notif").getValue().toString().equalsIgnoreCase("on")){
                    mBadge.setVisibility(View.VISIBLE); //menyalakan notif
                }else{
                    mBadge.setVisibility(View.GONE); //mematikan notif
                }

                if(dataSnapshot.child("theme").getValue().toString().equalsIgnoreCase("on")){
                    if (getActivity() != null) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorDark)); //menyalakan tema malam
                    }
                }else{
                    if (getActivity() != null) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorLight)); //mematikan tema malam
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // sama seperti setfragment, yg membedakan method ini untuk activity, bukan untuk fragment
    private void setActivity(Class activity) { // fungsi untuk kelarin activity terakhir, dan diganti ke activity baru trus dikirim ke halaman login
        Intent mainIntent = new Intent(getActivity(), activity);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        getActivity().finish();
    }

    private void setFragment(Fragment fragment) // fungsi buat pindah - pindah fragment
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
