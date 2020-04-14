package com.example.adorablepet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotifikasiFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ImageButton ib_back;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<NotifikasiModel> mLists = new ArrayList<>();

    private DatabaseReference transaksiRefs;

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
        return inflater.inflate(R.layout.fragment_notifikasi, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);

        initRecyclerView();

        transaksiRefs = FirebaseDatabase.getInstance().getReference().child("Shelter").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        transaksiRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    mLists.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if (snapshot.hasChild("read") && snapshot.hasChild("date")){
                            Log.d("CHECKINGMETHOD",snapshot.child("date").getValue().toString());
                            mLists.add(new NotifikasiModel(snapshot.getKey(),snapshot.child("additional").getValue().toString(),snapshot.child("buktiTransaksi").getValue().toString(),snapshot.child("date").getValue().toString(),
                                    snapshot.child("duration").getValue().toString(),snapshot.child("package").getValue().toString(), snapshot.child("payMethod").getValue().toString(), snapshot.child("quantity").getValue().toString(),
                                    Boolean.valueOf(snapshot.child("read").getValue().toString()),Integer.valueOf(snapshot.child("totalPrice").getValue().toString())));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASEERROR",databaseError.getMessage());
            }
        });

        ib_back = getActivity().findViewById(R.id.ib_back);
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

    }

    public void initRecyclerView() {
        mRecyclerView = getActivity().findViewById(R.id.rvListNotifikasi);
        mAdapter = new NotifikasiAdapter(mLists,getActivity().getApplicationContext(),getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setActivity(Class activity) { // fungsi untuk kelarin activity terakhir, dan diganti ke activity baru trus dikirim ke halaman login
        Intent mainIntent = new Intent(getActivity(), activity);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
