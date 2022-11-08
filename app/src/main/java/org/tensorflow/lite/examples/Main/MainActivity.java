package org.tensorflow.lite.examples.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import org.tensorflow.lite.examples.MainApplication;
import org.tensorflow.lite.examples.R;
import org.tensorflow.lite.examples.detection.CertificationFragment;
import org.tensorflow.lite.examples.detection.DetectorActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private BottomNavigationView bottomNavigationView;
    private long backKeyPressedTime = 0;
    private String bottomMenu = "home";
    private Toast toast;
    private double lat2;
    private double lon2;
    private int selectedItemId;
    private TextView textView;
    private ImageView imageView;
    private Toolbar toolbar;

    @SuppressLint({"NonConstantResourceId", "MissingInflatedId"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_forWooSung);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavi);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        textView = findViewById(R.id.toolbar_text);
        imageView = findViewById(R.id.toolbar_image);
        toolbar = findViewById(R.id.toolbar);

        imageView.setOnClickListener(view -> {
            loadFragment(new MainFragment());
            toolbar.setVisibility(View.GONE);
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                lat2 = location.getLatitude();
                lon2 = location.getLongitude();
            }
        });

        int d_flag = getIntent().getIntExtra("d_flag", -1);
        // detect flag, -1=디폴트, 카메라 액티비티와 무관함, 1=카메라 액티비티에서 인식 완료 후 복귀, 2=카메라 액티비티에서 뒤로가기 키로 복귀(인식 미완료)

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            if (i == 1) {
                final View iconView = menuView.getChildAt(1).findViewById(com.google.android.material.R.id.navigation_bar_item_icon_view);
                final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
                final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, displayMetrics);
                iconView.setLayoutParams(layoutParams);
            }
        }

        if(d_flag == -1) {
            loadFragment(new MainFragment());
            toolbar.setVisibility(View.GONE);
            // 앱 실행 시 홈화면(Fragment1)
            selectedItemId = R.id.item2;
        }
        else {
            // 카메라 액티비티에서 메인 액티비티로 복귀 시(인증 완료 or 뒤로가기)
            if(d_flag == 1) {
                // 인증 완료로 인한 복귀 + 인증메시지 발생
                Toast.makeText(this, "인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
            loadFragment(new CertificationFragment());
            textView.setText("인증리스트");
            selectedItemId = R.id.item5;
            bottomNavigationView.getMenu().findItem(R.id.item5).setChecked(true);
        }

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                case R.id.item2:
                    loadFragment(new Fragment1());
                    textView.setText("인사말");
                    bottomMenu = "menu1";
                    selectedItemId = R.id.item2;
                    break;
                case R.id.item4:
                    loadFragment(new Fragment2());
                    textView.setText("코스안내");
                    bottomMenu = "menu2";
                    selectedItemId = R.id.item4;
                    break;
                case R.id.item3:
                    bottomNavigationView.getMenu().findItem(selectedItemId).setChecked(true);
                    cameraIconToCertification();
                    break;
                case R.id.item1:
                    loadFragment(new Fragment4());
                    textView.setText("인증샷");
                    bottomMenu = "menu4";
                    break;
                case R.id.item5:
                    loadFragment(new CertificationFragment());
                    textView.setText("인증리스트");
                    bottomMenu = "menu5";
                    selectedItemId = R.id.item5;
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().findItem(selectedItemId).setChecked(true);
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).commit();
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //startLocationService();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if ("home".equals(bottomMenu)) {
            if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
                backKeyPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(() -> toast.cancel(), 500);
            } else if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
                finishAffinity();
            }
        } else {
            bottomMenu = "home";
            loadFragment(new Fragment1());
            textView.setText("인사말");
            bottomNavigationView.getMenu().findItem(R.id.item2).setChecked(true);
        }
    }

    private void cameraIconToCertification(){
        Log.d("test current GPS","경도는 " + lat2 + " ,위도는 " + lon2);

        for(int i = 0; i < CertificationFragment.listLat.size(); i++){
            double lat1 = CertificationFragment.listLat.get(i);
            double lon1 = CertificationFragment.listLong.get(i);

            // 현재 위치정보와 인증대상의 위치정보 비교(거리계산) ->
            // 거리 100m 이내일시 그 인증대상을 타겟으로 설정후 ->
            // DetectorActivity 호출 (타겟의 인덱스정보 (getAdapterPosition()) 를 넘겨줌)
            // GPS 계산 시 인증 가능 범위, 단위 : meter (안드로이드 gps는 기본 20m 오차)
            int DISTANCE_ERROR_RANGE = 50;
            if (Integer.parseInt(getDistance(lat1, lon1, lat2, lon2)) < DISTANCE_ERROR_RANGE) {
                if (MainApplication.cerList.get(i)) {
                    Toast.makeText(getApplicationContext(), "이미 인증 완료된 스팟입니다", Toast.LENGTH_SHORT).show();
                    loadFragment(new failedFragment());
                    textView.setText("인증하기");
                    return;
                }
                else{
                    Intent intent = new Intent(this, DetectorActivity.class);
                    intent.putExtra("targetI", i);
                    startActivity(intent);
                    return;
                }
            }
        }

        Toast.makeText(getApplicationContext(), "올바른 장소에서 인증을 시도해주세요", Toast.LENGTH_SHORT).show();
        loadFragment(new failedFragment());
    }

    public String getDistance(double lat1, double lng1, double lat2, double lng2) {
        double distance;
        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);
        distance = locationA.distanceTo(locationB);
        return String.format(Locale.KOREA, "%.0f", distance);
    }
}
