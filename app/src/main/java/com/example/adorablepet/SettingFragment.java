package com.example.adorablepet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ImageButton ib_back;
    private SwitchCompat sw_notif, sw_night;
    private View view;
    private Button button;
    private TextView notifikasi,nightmode;
    private RelativeLayout rel1,rel2;

    private DatabaseReference userRefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }else{
                    setActivity(MainActivity.class);
                }
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){

        view = getView().getRootView();
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);

        sw_notif = getActivity().findViewById(R.id.switch_notif_setting);
        sw_night = getActivity().findViewById(R.id.switch_night_setting);
        ib_back = getActivity().findViewById(R.id.ib_back);
        button = getActivity().findViewById(R.id.btnLogout);
        notifikasi = getActivity().findViewById(R.id.notifikasi);
        nightmode = getActivity().findViewById(R.id.nightmode);
        rel1 = getActivity().findViewById(R.id.layout_notf);
        rel2 = getActivity().findViewById(R.id.layout_night);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }else{
                    setActivity(MainActivity.class);
                }
            }
        });

        sw_notif.setTextOff("OFF");
        sw_notif.setTextOn("ON");
        sw_night.setTextOff("OFF");
        sw_night.setTextOn("ON");

        //mengambil informasi dari database terhadap tema dan juga notifikasi user
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("notif").getValue().toString().equalsIgnoreCase("on")){
                    sw_notif.setChecked(true);
                }else{
                    sw_notif.setChecked(false);
                }

                if (dataSnapshot.child("theme").getValue().toString().equalsIgnoreCase("on")){
                    sw_night.setChecked(true);
                    if (getActivity()!= null){
                        view.setBackgroundColor(getActivity().getColor(R.color.colorDark));
                    }
                }else{
                    sw_night.setChecked(false);
                    if (getActivity()!= null){
                        view.setBackgroundColor(getActivity().getColor(R.color.colorLight));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //proses pengubahan pada database sesuai dengan switch pada notifikasi
        sw_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    userRefs.child("notif").setValue("on");
                }else{
                    userRefs.child("notif").setValue("off");
                }
            }
        });

        //proses pengubahan pada database sesuai dengan switch pada night theme
        sw_night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    userRefs.child("theme").setValue("on");
                }else{
                    userRefs.child("theme").setValue("off");
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertsignout();
            }
        });

    }

    //method untuk menampilkan pesan dialog untuk konfirmasi pada saat log out
    public void alertsignout(){ // fungsi untuk membuat alert dialog ketika ingin logout
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog2.setTitle("Exit Confirmation");

        alertDialog2.setCancelable(false);

        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure want to log out?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog
                FirebaseAuth.getInstance().signOut();
                setActivity(LoginActivity.class);
            }
        });

        alertDialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog2.show();

    }

    private void setActivity(Class activity) { // fungsi untuk kelarin activity terakhir, dan diganti ke activity baru trus dikirim ke halaman login
        Intent mainIntent = new Intent(getActivity(), activity);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
