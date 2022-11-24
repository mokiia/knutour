package org.tensorflow.lite.examples.detection;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tensorflow.lite.examples.MainApplication;
import org.tensorflow.lite.examples.R;

public class CertificationFragment extends Fragment {

    // 인증대상의 이름 리스트
    static List<String> listTitle = Arrays.asList("일청담", "정문", "3층석탑", "북문");

    // 인증대상의 인증여부 리스트
    public List<String> listCertification = new ArrayList<>();

    // 인증대상의 사진 리스트
    static List<Integer> listResId = Arrays.asList(
            R.drawable.caret,
            R.drawable.caret,
            R.drawable.caret,
            R.drawable.caret
//            R.drawable.tower,
//            R.drawable.west
    );

    // 인증대상의 위도정보 리스트
    public static List<Double> listLat = Arrays.asList(
            35.888686,
            35.885445,
            35.889116,
            35.892475
//            35.889417,
//            35.888488
    );

    // 인증대상의 경도정보 리스트
    public static List<Double> listLong = Arrays.asList(
            128.612123,
            128.614509,
            128.612493,
            128.609428
//            128.612461,
//            128.604296
    );

    // 인증대상의 상세내용
    static List<String> listInformation = Arrays.asList(
            "내용"
            ,"내용"
            ,"내용"
            ,"내용"
//            ,"내용"
//            ,"내용"
            );

    // RecyclerView와 연결할 어댑터
    public RecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for(boolean i : MainApplication.cerList) {
            listCertification.add(i ? "인증 완료" : "인증 미완료");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_certification, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        Button button = getView().findViewById(R.id.resetButton);

        button.setOnClickListener(view -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(MainApplication.user.getUid());
            Map<String, Boolean> value = new HashMap<>();
            for(String i : MainApplication.nameArray) {
                value.put(i, false);
            }
            ref.setValue(value);
        });

        int permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){ //위치 권한 확인
            //위치 권한 요청
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        int checked_target = requireActivity().getIntent().getIntExtra("checked_target", -1);
        if(checked_target != -1){
            listCertification.set(checked_target, "인증 완료");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        setData();
    }

    private void setData() {
        for (int i = 0; i < listResId.size() ; i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setCertification(listCertification.get(i));
            data.setResId(listResId.get(i));
            data.setLat(listLat.get(i));
            data.setLon(listLong.get(i));
            data.setInformation(listInformation.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}


