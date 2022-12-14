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
        // detect flag, -1=?????????, ????????? ??????????????? ?????????, 1=????????? ?????????????????? ?????? ?????? ??? ??????, 2=????????? ?????????????????? ???????????? ?????? ??????(?????? ?????????)

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
            // ??? ?????? ??? ?????????(Fragment1)
            selectedItemId = R.id.item2;
        }
        else {
            // ????????? ?????????????????? ?????? ??????????????? ?????? ???(?????? ?????? or ????????????)
            if(d_flag == 1) {
                // ?????? ????????? ?????? ?????? + ??????????????? ??????
                Toast.makeText(this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
            loadFragment(new CertificationFragment());
            textView.setText("???????????????");
            selectedItemId = R.id.item5;
            bottomNavigationView.getMenu().findItem(R.id.item5).setChecked(true);
        }

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                //item??? ????????? id?????? ????????? FrameLayout??? fragment.xml?????????
                case R.id.item2:
                    loadFragment(new Fragment1());
                    textView.setText("?????????");
                    bottomMenu = "menu1";
                    selectedItemId = R.id.item2;
                    break;
                case R.id.item4:
                    loadFragment(new Fragment2());
                    textView.setText("????????????");
                    bottomMenu = "menu2";
                    selectedItemId = R.id.item4;
                    break;
                case R.id.item3:
                    bottomNavigationView.getMenu().findItem(selectedItemId).setChecked(true);
                    cameraIconToCertification();
                    break;
                case R.id.item1:
                    loadFragment(new Fragment4());
                    textView.setText("?????????");
                    bottomMenu = "menu4";
                    break;
                case R.id.item5:
                    loadFragment(new CertificationFragment());
                    textView.setText("???????????????");
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
                toast = Toast.makeText(this, "?????? ?????? ????????? ??? ??? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(() -> toast.cancel(), 500);
            } else if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
                finishAffinity();
            }
        } else {
            bottomMenu = "home";
            loadFragment(new Fragment1());
            textView.setText("?????????");
            bottomNavigationView.getMenu().findItem(R.id.item2).setChecked(true);
        }
    }

    private void cameraIconToCertification(){
        Log.d("test current GPS","????????? " + lat2 + " ,????????? " + lon2);

        for(int i = 0; i < CertificationFragment.listLat.size(); i++){
            double lat1 = CertificationFragment.listLat.get(i);
            double lon1 = CertificationFragment.listLong.get(i);

            // ?????? ??????????????? ??????????????? ???????????? ??????(????????????) ->
            // ?????? 100m ???????????? ??? ??????????????? ???????????? ????????? ->
            // DetectorActivity ?????? (????????? ??????????????? (getAdapterPosition()) ??? ?????????)
            // GPS ?????? ??? ?????? ?????? ??????, ?????? : meter (??????????????? gps??? ?????? 20m ??????)
            int DISTANCE_ERROR_RANGE = 50000000;
            if (Integer.parseInt(getDistance(lat1, lon1, lat2, lon2)) < DISTANCE_ERROR_RANGE) {
                if (MainApplication.cerList.get(i)) {
                    Toast.makeText(getApplicationContext(), "?????? ?????? ????????? ???????????????", Toast.LENGTH_SHORT).show();
                    loadFragment(new failedFragment());
                    textView.setText("????????????");
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

        Toast.makeText(getApplicationContext(), "????????? ???????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
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
