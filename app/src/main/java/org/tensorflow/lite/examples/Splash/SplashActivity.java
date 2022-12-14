package org.tensorflow.lite.examples.Splash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.Main.MainActivity;
import org.tensorflow.lite.examples.MainApplication;
import org.tensorflow.lite.examples.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private DatabaseReference myRef;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                MainApplication.user = auth.getCurrentUser();
                myRef = FirebaseDatabase.getInstance().getReference(MainApplication.user.getUid());
                myRef.get().addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        if(task1.getResult().getValue() == null) {
                            Map<String, Boolean> value = new HashMap<>();
                            for(String i : MainApplication.nameArray) {
                                value.put(i, false);
                            }
                            myRef.setValue(value).addOnCompleteListener(task2 -> {
                                if(task2.isSuccessful()) {
                                    checkPermission();
                                }
                            });
                        } else {
                            checkPermission();
                        }
                    }
                });
            }
        });
    }

    private void checkPermission() {
        MainApplication.addListener(myRef);
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("?????? ?????? ????????? ???????????????.")
                .setDeniedMessage("?????? ??????????????? ???????????? ?????? ???????????????.\n[??????] > " + getResources().getString(R.string.app_name) + " > [??????] ?????? ????????? ????????? ??? ????????????.")
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
