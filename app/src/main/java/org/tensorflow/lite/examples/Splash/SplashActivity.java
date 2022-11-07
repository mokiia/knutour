package org.tensorflow.lite.examples.Splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.Main.MainActivity;
import org.tensorflow.lite.examples.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("기기 접근 권한이 필요합니다.")
                .setDeniedMessage("기기 접근권한을 허용하지 않아 종료됩니다.\n[설정] > " + getResources().getString(R.string.app_name) + " > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(SplashActivity.this, deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    };
}
