package org.tensorflow.lite.examples;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainApplication extends Application {
    public static FirebaseUser user;
    public static List<Boolean> cerList = new ArrayList<>();
    public static String[] nameArray = {
        "front",
        "nine",
        "north",
        "tech",
        "tower",
        "west"
    };
    public static Map<String, Integer> nameIndexMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        for(int i = 0; i < nameArray.length; i++) {
            nameIndexMap.put(nameArray[i], i);
            cerList.add(false);
        }
    }

    public static void addListener(DatabaseReference ref) {
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                cerList.set(nameIndexMap.get(snapshot.getKey()), snapshot.getValue(Boolean.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                cerList.set(nameIndexMap.get(snapshot.getKey()), snapshot.getValue(Boolean.class));
            }

            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
