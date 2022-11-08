package org.tensorflow.lite.examples.detection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.tensorflow.lite.examples.MainApplication;
import org.tensorflow.lite.examples.R;

public class DetailActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private double lat2;
    private double lon2;

    private final Integer DISTANCE_ERROR_RANGE = 50; // GPS 계산 시 인증 가능 범위, 단위 : meter (안드로이드 gps는 기본 20m 오차)
    ImageView image;
    TextView name;
    TextView information;
    Button certiButton;
    public int target;

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        setInit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_detail);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
        getData();
    }

    private void getData(){
        target = getIntent().getIntExtra("targetI", -1);
    }

    private void setInit(){
        image = findViewById(R.id.imageView);
        name = findViewById(R.id.text_name);
        information = findViewById(R.id.text_information);
        certiButton = findViewById(R.id.certi_button);

        image.setImageResource(CertificationFragment.listResId.get(target));
        name.setText(CertificationFragment.listTitle.get(target));
        information.setText(CertificationFragment.listInformation.get(target));

        certiButton.setOnClickListener(view -> {
            // 클릭한 데이터의 인증여부 체크
            if (MainApplication.cerList.get(target)) {
                Toast.makeText(getApplicationContext(), "이미 인증 완료된 스팟입니다", Toast.LENGTH_SHORT).show();
            }
            else {
                // 인증대상의 위치정보 받아옴 (lat1, lon1)
                Log.d("test current GPS","경도는 " + lat2 + " ,위도는 " + lon2);
                double lat1 = CertificationFragment.listLat.get(target);
                double lon1 = CertificationFragment.listLong.get(target);

                // 현재 위치정보와 인증대상의 위치정보 비교(거리계산) ->
                // 거리 100m 이내일시 그 인증대상을 타겟으로 설정후 ->
                // DetectorActivity 호출 (타겟의 인덱스정보 (getAdapterPosition()) 를 넘겨줌)
                if (Integer.parseInt(getDistance(lat1, lon1, lat2, lon2)) < DISTANCE_ERROR_RANGE) {
                    Intent intent = new Intent(getApplicationContext(), DetectorActivity.class);
                    intent.putExtra("targetI", target);
                    startActivity(intent);
//                        ((MainActivity)MainActivity.context_certi).startActivityForResult(intent,REQUEST_CODE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "올바른 장소에서 인증을 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 두 위치정보간의 거리 계산하는 함수
    public String getDistance(double lat1, double lng1, double lat2, double lng2) {
        double distance;
        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);
        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);
        distance = locationA.distanceTo(locationB);
        String num = String.format("%.0f", distance);
        return num;
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onBackPressed(){
        supportFinishAfterTransition();
    }

}


