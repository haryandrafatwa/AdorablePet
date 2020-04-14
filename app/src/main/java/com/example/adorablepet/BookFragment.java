package com.example.adorablepet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.util.HashMap;

public class BookFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ImageButton ib_back, ib_setting,ib_notif;
    private Spinner packages,quantity,duration;
    private EditText et_package,et_quantity,et_duration;
    private int packPrice, durPrice, quanPrice,totPrice;

    private Button button;

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
        return inflater.inflate(R.layout.fragment_book, container, false);
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

        button = getActivity().findViewById(R.id.btnNext);
        ib_back = getActivity().findViewById(R.id.ib_back);
        packages = getActivity().findViewById(R.id.spin_package);
        et_package = getActivity().findViewById(R.id.et_package);
        quantity = getActivity().findViewById(R.id.spin_quantity);
        et_quantity = getActivity().findViewById(R.id.et_quantity);
        duration = getActivity().findViewById(R.id.spin_duration);
        et_duration = getActivity().findViewById(R.id.et_duration);
        ib_setting = getActivity().findViewById(R.id.ib_button_setting_shelter);
        ib_notif = getActivity().findViewById(R.id.ib_button_notification_shelter);
        mBadge = getActivity().findViewById(R.id.notif_badge);

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_duration.getText().toString().equals("Duration") && !et_package.getText().toString().equals("Package") && !et_quantity.getText().toString().equals("Quantity")){

                    if(quanPrice == 1){
                        totPrice = packPrice+durPrice;
                    }else if(quanPrice == 2){
                        totPrice = (packPrice+durPrice)*2;
                    }else if(quanPrice == 3){
                        totPrice = (packPrice+durPrice)*3;
                    }
                    Bundle bundle = new Bundle();
                    HashMap bookMap = new HashMap();
                    bookMap.put("quantity",et_quantity.getText().toString());
                    bookMap.put("duration",et_duration.getText().toString());
                    bookMap.put("package",et_package.getText().toString());
                    bookMap.put("totalPrice",totPrice);
                    bundle.putSerializable("bookMap",bookMap);
                    BookDetailFragment bookDetailFragment = new BookDetailFragment();
                    bookDetailFragment.setArguments(bundle);
                    setFragment(bookDetailFragment);
                }else{
                    Toast.makeText(getActivity(), "Silahkan isi dengan lengkap form booking terlebih dahulu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        String[] entriesPackages = new String[]{
                "Package", "Package 1", "Package 2", "Package 3"
        };

        String[] quantityPackages = new String[]{
                "Quantity","1 Hewan", "2 Hewan", "3 Hewan"
        };

        String[] durationPackages = new String[]{
                "Duration", "3 Jam", "5 Jam", "12 Jam", "1 Hari","3 Hari","5 Hari","1 Minggu"
        };

        ArrayAdapter<String> spinnerPackage = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, entriesPackages);
        spinnerPackage.setDropDownViewResource(R.layout.item_spinner);
        packages.setAdapter(spinnerPackage);
        packages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipe = (String) parent.getItemAtPosition(position);
                et_package.setText(tipe);

                if(tipe.equals("Package 1")){
                    packPrice = 40000;
                }else if(tipe.equals("Package 2")){
                    packPrice = 25000;
                }else if(tipe.equals("Package 3")){
                    packPrice = 20000;
                }else if(tipe.equals("Package 4")){
                    packPrice = 35000;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> spinnerDuration = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, durationPackages);
        spinnerDuration.setDropDownViewResource(R.layout.item_spinner);
        duration.setAdapter(spinnerDuration);
        duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipe = (String) parent.getItemAtPosition(position);
                et_duration.setText(tipe);

                if(tipe.equals("3 Jam")){
                    durPrice = 20000;
                }else if(tipe.equals("5 Jam")){
                    durPrice = 25000;
                }else if(tipe.equals("12 Jam")){
                    durPrice = 50000;
                }else if(tipe.equals("1 Hari")){
                    durPrice = 70000;
                }else if(tipe.equals("3 Hari")){
                    durPrice = 160000;
                }else if(tipe.equals("5 Hari")){
                    durPrice = 220000;
                }else if(tipe.equals("1 Minggu")){
                    durPrice = 300000;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> spinnerQuantity = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, quantityPackages);
        spinnerQuantity.setDropDownViewResource(R.layout.item_spinner);
        quantity.setAdapter(spinnerQuantity);
        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipe = (String) parent.getItemAtPosition(position);
                et_quantity.setText(tipe);

                if(tipe.equals("1 Hewan")){
                    quanPrice = 1;
                }else if(tipe.equals("2 Hewan")){
                    quanPrice = 2;
                }else if(tipe.equals("3 Hewan")){
                    quanPrice = 3;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
