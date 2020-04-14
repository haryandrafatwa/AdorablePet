package com.example.adorablepet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder>{
    private List<NotifikasiModel> mLists = new ArrayList<>();
    private DatabaseReference transaksiRefs;
    private Context mContext;
    private FragmentActivity mActivity;


    public NotifikasiAdapter(List<NotifikasiModel> mLists, Context mContext, FragmentActivity mActivity) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notifikasi,viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final int position = i;
        final NotifikasiModel model = mLists.get(i);

        if (model.getRead()){
            viewHolder.badge.setVisibility(View.GONE);
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.textView.setText(model.getDate());
        }else{
            viewHolder.badge.setVisibility(View.VISIBLE);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("read",true);
                transaksiRefs = FirebaseDatabase.getInstance().getReference().child("Shelter").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(model.getId());
                transaksiRefs.updateChildren(hashMap);
                NotifikasiDetail notifikasiDetail = new NotifikasiDetail();
                notifikasiDetail.setDate(model.getDate());
                notifikasiDetail.setAdditional(model.getAdditionalInformation());
                notifikasiDetail.setDuration(model.getDuration());
                notifikasiDetail.setImageURL(model.getImageURL());
                notifikasiDetail.setPackages(model.getPackages());
                notifikasiDetail.setQuantity(model.getQuantity());

                setFragment(notifikasiDetail);

            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView badge;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cvItemListTransaksi);
            textView = itemView.findViewById(R.id.date_transaksi);
            badge = itemView.findViewById(R.id.transaksi_badge);
        }
    }

}