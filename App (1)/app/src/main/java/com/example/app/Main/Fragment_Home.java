package com.example.app.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app.R;
import com.example.app.Value;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_Home extends Fragment {
    private View fm_home;
    private TextView timeUpdate;
    private ArcProgress Arc_Speed, Arc_Total;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private String device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fm_home = inflater.inflate(R.layout.fragment_home, container, false);

        anhXa();
        read_Name();
        realTimeData();
        return fm_home;
    }

    // Ham anh xa
    private void anhXa() {
        timeUpdate = fm_home.findViewById(R.id.timeUpdate);
        Arc_Speed = fm_home.findViewById(R.id.Arc_Speed);
        Arc_Total = fm_home.findViewById(R.id.Arc_Total);
    }

    // Đọc dữ liệu
    private void realTimeData() {
        mData.child(device + "/Present").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Value val = snapshot.getValue(Value.class);
                if(val != null) {
                    Arc_Speed.setProgress(val.speed);
                    Arc_Total.setProgress(val.total);
                    timeUpdate.setText(val.time);
                }
                else {
                    Value val_1 = new Value(0, 0, "Cập nhật lần cuối: 11:20 20/11/2022");
                    mData.child(device + "/Present").setValue(val_1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Lấy tên thiết bị
    private void read_Name() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                String name_1 = profile.getDisplayName();

                if (name_1 != null) {
                    device = name_1;
                }
            }
        }
    }
}
