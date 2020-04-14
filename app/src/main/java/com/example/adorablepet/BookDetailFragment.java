package com.example.adorablepet;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Currency;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BookDetailFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ImageButton ib_back, ib_setting,ib_notif;
    private TextView tv_package, tv_quantity, tv_duration, tv_totprice;
    private EditText et_additional;

    private Button button;
    private Bundle bundle;


    private NotificationBadge mBadge;
    private int numOfNotif;

    private DatabaseReference shelterRefs,userRefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){

        toolbar = getActivity().findViewById(R.id.toolbar_shelter);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle("");

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);

        button = getActivity().findViewById(R.id.btnSelectMethod);
        ib_back = getActivity().findViewById(R.id.ib_back);
        tv_package  =getActivity().findViewById(R.id.tv_package);
        tv_quantity = getActivity().findViewById(R.id.tv_quantity);
        tv_duration = getActivity().findViewById(R.id.tv_duration);
        tv_totprice = getActivity().findViewById(R.id.tv_total_payment);
        et_additional  =getActivity().findViewById(R.id.et_information);
        ib_notif = getActivity().findViewById(R.id.ib_button_notification_shelter);
        mBadge = getActivity().findViewById(R.id.notif_badge);

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

        ib_setting = getActivity().findViewById(R.id.ib_button_setting_shelter);
        ib_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                setFragment(settingFragment);
            }
        });

        bundle = this.getArguments();
        final HashMap hashMap = (HashMap) bundle.getSerializable("bookMap");
        tv_package.setText(hashMap.get("package").toString());
        tv_quantity.setText(hashMap.get("quantity").toString());

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("IDR"));
        String formattedNumber = format.format(Integer.valueOf(hashMap.get("totalPrice").toString()));
        String rupiah = formattedNumber.replaceAll("IDR","Rp");
        String titik = rupiah.replaceAll("[,]",".");
        tv_totprice.setText(titik);

        if (hashMap.get("duration").toString().equals("3 Jam"))   {
            Date c = Calendar.getInstance().getTime();
            Date cTemp = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            Locale local = new Locale("id", "ID");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", local);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", local);

            c.setHours(c.getHours()+3);
            String formattedDate;
            if(c.getDate() != cTemp.getDate()){
                formattedDate = date.format(cTemp)+" - "+date.format(c);
                tv_duration.setText(formattedDate);
            }else{
                formattedDate = df.format(cTemp)+" - "+df.format(c);
                tv_duration.setText(formattedDate);
            }

        }else if (hashMap.get("duration").toString().equals("5 Jam")){
            Date c = Calendar.getInstance().getTime();
            Date cTemp = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            Locale local = new Locale("id", "ID");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", local);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", local);

            c.setHours(c.getHours()+5);
            String formattedDate;
            if(c.getDate() != cTemp.getDate()){
                formattedDate = date.format(cTemp)+" - "+date.format(c);
                tv_duration.setText(formattedDate);
            }else{
                formattedDate = df.format(cTemp)+" - "+df.format(c);
                tv_duration.setText(formattedDate);
            }
        }else if (hashMap.get("duration").toString().equals("12 Jam")){
            Date c = Calendar.getInstance().getTime();
            Date cTemp = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            Locale local = new Locale("id", "ID");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", local);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", local);

            c.setHours(c.getHours()+12);
            String formattedDate;
            if(c.getDate() != cTemp.getDate()){
                formattedDate = date.format(cTemp)+" - "+date.format(c);
                tv_duration.setText(formattedDate);
            }else{
                formattedDate = df.format(cTemp)+" - "+df.format(c);
                tv_duration.setText(formattedDate);
            }
        }else if (hashMap.get("duration").toString().equals("1 Hari")){
            Date c = Calendar.getInstance().getTime();
            Date cTemp = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            Locale local = new Locale("id", "ID");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", local);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", local);

            c.setHours(c.getHours()+24);
            String formattedDate;
            if(c.getDate() != cTemp.getDate()){
                formattedDate = date.format(cTemp)+" - "+date.format(c);
                tv_duration.setText(formattedDate);
            }else{
                formattedDate = df.format(cTemp)+" - "+df.format(c);
                tv_duration.setText(formattedDate);
            }
        }else if (hashMap.get("duration").toString().equals("3 Hari")){
            Date c = Calendar.getInstance().getTime();
            Date cTemp = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            Locale local = new Locale("id", "ID");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", local);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", local);

            c.setDate(c.getDate()+3);
            String formattedDate;
            if(c.getDate() != cTemp.getDate()){
                formattedDate = date.format(cTemp)+" - "+date.format(c);
                tv_duration.setText(formattedDate);
            }else{
                formattedDate = df.format(cTemp)+" - "+df.format(c);
                tv_duration.setText(formattedDate);
            }
        }else if (hashMap.get("duration").toString().equals("5 Hari")){
            Date c = Calendar.getInstance().getTime();
            Date cTemp = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            Locale local = new Locale("id", "ID");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", local);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", local);

            c.setDate(c.getDate()+5);
            String formattedDate;
            if(c.getDate() != cTemp.getDate()){
                formattedDate = date.format(cTemp)+" - "+date.format(c);
                tv_duration.setText(formattedDate);
            }else{
                formattedDate = df.format(cTemp)+" - "+df.format(c);
                tv_duration.setText(formattedDate);
            }
        }else if (hashMap.get("duration").toString().equals("1 Minggu")){
            Date c = Calendar.getInstance().getTime();
            Date cTemp = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            Locale local = new Locale("id", "ID");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm", local);
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy", local);

            c.setDate(c.getDate()+7);
            String formattedDate;
            if(c.getDate() != cTemp.getDate()){
                formattedDate = date.format(cTemp)+" - "+date.format(c);
                tv_duration.setText(formattedDate);
            }else{
                formattedDate = df.format(cTemp)+" - "+df.format(c);
                tv_duration.setText(formattedDate);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayMethodFragment payMethodFragment = new PayMethodFragment();
                if (TextUtils.isEmpty(et_additional.getText().toString())){
                    hashMap.put("additional","-");
                }else{
                    hashMap.put("additional",et_additional.getText().toString());
                }
                bundle.putSerializable("bookMap",hashMap);
                payMethodFragment.setArguments(bundle);
                setFragment(payMethodFragment);
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
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
