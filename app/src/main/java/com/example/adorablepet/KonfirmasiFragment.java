package com.example.adorablepet;

import android.app.Dialog;
import android.graphics.Color;
import android.icu.text.NumberFormat;
import android.icu.util.Currency;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

public class KonfirmasiFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ImageButton ib_back, ib_setting,ib_notif;
    private Button button;
    private TextView rekening, bank, price,silahkan,norekening,atasnama,asrekening,tvbank,tvtotal;

    private String method;
    private RelativeLayout relativeLayout,relativeLayout2;

    private DatabaseReference shelterRefs,userRefs;
    private String key;

    private NotificationBadge mBadge;
    private int numOfNotif;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_konfirmasi, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){

        final View view = getView().getRootView();
        toolbar = getActivity().findViewById(R.id.toolbar_shelter);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle("");

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);

        rekening = getActivity().findViewById(R.id.rekening);
        bank = getActivity().findViewById(R.id.bank);
        price = getActivity().findViewById(R.id.totalpembayaran);
        ib_back = getActivity().findViewById(R.id.ib_back);
        ib_setting = getActivity().findViewById(R.id.ib_button_setting_shelter);
        ib_notif = getActivity().findViewById(R.id.ib_button_notification_shelter);
        mBadge = getActivity().findViewById(R.id.notif_badge);
        relativeLayout = getActivity().findViewById(R.id.layout_title);
        relativeLayout2 = getActivity().findViewById(R.id.layout_payment);
        silahkan = getActivity().findViewById(R.id.silahkan);
        norekening = getActivity().findViewById(R.id.norekening);
        atasnama = getActivity().findViewById(R.id.atasnama);
        asrekening = getActivity().findViewById(R.id.as_rekening);
        tvbank = getActivity().findViewById(R.id.tvbank);
        tvtotal = getActivity().findViewById(R.id.tvtotalpembayaran);

        ib_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
                setFragment(notifikasiFragment);
            }
        });

        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("notif").getValue().toString().equalsIgnoreCase("on")){
                    mBadge.setVisibility(View.VISIBLE);
                }else{
                    mBadge.setVisibility(View.GONE);
                }

                if(dataSnapshot.child("theme").getValue().toString().equalsIgnoreCase("on")){
                    if (getActivity() != null){
                        view.setBackgroundColor(getActivity().getResources().getColor(R.color.colorDark));
                        relativeLayout.setBackground(getActivity().getDrawable(R.drawable.bg_btn_solid_black_stroke));
                        relativeLayout2.setBackground(getActivity().getDrawable(R.drawable.bg_btn_solid_black_stroke));
                        bank.setTextColor(Color.WHITE);
                        price.setTextColor(Color.WHITE);
                        rekening.setTextColor(Color.WHITE);
                        silahkan.setTextColor(Color.WHITE);
                        norekening.setTextColor(Color.WHITE);
                        asrekening.setTextColor(Color.WHITE);
                        tvbank.setTextColor(Color.WHITE);
                        tvtotal.setTextColor(Color.WHITE);
                    }
                }else{
                    if (getActivity() != null){
                        view.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLight));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        ib_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                setFragment(settingFragment);
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        button = getActivity().findViewById(R.id.btnUploadBukti);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFragment uploadFragment = new UploadFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",key);
                uploadFragment.setArguments(bundle);
                setFragment(uploadFragment);
            }
        });

        shelterRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("status").getValue().toString().equals("Pending")){
                        key = snapshot.getKey();
                        method = snapshot.child("payMethod").getValue().toString();
                        bank.setText(method.toUpperCase());

                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        format.setMaximumFractionDigits(0);
                        format.setCurrency(Currency.getInstance("IDR"));
                        String formattedNumber = format.format(Integer.valueOf(snapshot.child("totalPrice").getValue().toString()));
                        String rupiah = formattedNumber.replaceAll("IDR","Rp");
                        String titik = rupiah.replaceAll("[,]",".");
                        price.setText(titik);

                        if(method.equals("Mandiri")){
                            rekening.setText("012-00-0184334-2");
                        }else if(method.equals("BCA")){
                            rekening.setText("6860 1774 28");
                        }else if(method.equals("BNI")){
                            rekening.setText("009 821 1328");
                        }else if(method.equals("BNI")){
                            bank.setText(method);
                            rekening.setText("009 821 1328");
                        }else if(method.equals("BRI")){
                            bank.setText(method);
                            rekening.setText("0504.02.000566.12.0");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFragment(Fragment fragment) // fungsi buat pindah - pindah fragment
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
