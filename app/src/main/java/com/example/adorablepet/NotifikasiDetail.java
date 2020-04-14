package com.example.adorablepet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class NotifikasiDetail extends Fragment {

    private TextView tv_date,tv_package,tv_duration,tv_quantity,tv_additional;
    private ImageView imageView;
    private String date, packages, duration, quantity, additional,imageURL;
    private ImageButton ib_back;

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
        return inflater.inflate(R.layout.fragment_notifikasi_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){

        tv_additional = getActivity().findViewById(R.id.tv_information);
        tv_date = getActivity().findViewById(R.id.notif_date);
        tv_duration = getActivity().findViewById(R.id.tv_date);
        tv_package = getActivity().findViewById(R.id.tv_package);
        tv_quantity = getActivity().findViewById(R.id.tv_quantity);
        imageView = getActivity().findViewById(R.id.iv_proof);
        ib_back = getActivity().findViewById(R.id.ib_back);

        tv_additional.setText(additional);
        tv_quantity.setText(quantity);
        tv_package.setText(packages);
        tv_duration.setText(duration);
        tv_date.setText(date);

        Picasso.get().load(imageURL).into(imageView);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private void setActivity(Class activity) { // fungsi untuk kelarin activity terakhir, dan diganti ke activity baru trus dikirim ke halaman login
        Intent mainIntent = new Intent(getActivity(), activity);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
