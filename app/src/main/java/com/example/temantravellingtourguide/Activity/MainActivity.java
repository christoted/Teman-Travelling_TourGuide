package com.example.temantravellingtourguide.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.temantravellingtourguide.Fragment.ConfirmCancelationDialog;
import com.example.temantravellingtourguide.Fragment.FillDataDialog;
import com.example.temantravellingtourguide.Fragment.LoadingDialog;
import com.example.temantravellingtourguide.Manager.PreferencesManager;
import com.example.temantravellingtourguide.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    LinearLayout itemTourGuide, itemWallet, itemHistory, itemAccount, noOrder, runningOrder, waitingOrder;
    TextView tvKodeOrder, tvTimeLeft, tvName;
    CardView cvRunningOrder;
    MaterialButton btnStart, btnCancel;
    PreferencesManager preferencesManager;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;
    Double latitude, longitude;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemTourGuide = findViewById(R.id.item_tour_guide);
        itemWallet = findViewById(R.id.item_wallet);
        itemHistory = findViewById(R.id.item_history);
        itemAccount = findViewById(R.id.item_account);
        noOrder = findViewById(R.id.no_order);
        waitingOrder = findViewById(R.id.waiting_order);
        runningOrder = findViewById(R.id.running_order);
        cvRunningOrder = findViewById(R.id.cv_running_order);
        btnStart = findViewById(R.id.btn_start);
        btnCancel = findViewById(R.id.btn_cancel);
        tvKodeOrder = findViewById(R.id.tv_progress_id);
        tvTimeLeft = findViewById(R.id.tv_time_left);
        tvName = findViewById(R.id.tv_name);
        db = FirebaseFirestore.getInstance();
        preferencesManager = new PreferencesManager(this);
        loadingDialog = new LoadingDialog();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this));

        //noinspection deprecation
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        //set tvName
        tvName.setText(preferencesManager.getName());

        if(preferencesManager.getProgressId().isEmpty()){

            //check data in onWaiting Collection
            final DocumentReference docRef = db.collection("onWaiting").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("Error", "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("Success", "DocumentSnapshot data: " + snapshot.getData());
                        //show waiting order
                        noOrder.setVisibility(View.GONE);
                        runningOrder.setVisibility(View.GONE);
                        waitingOrder.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("Error", "Current data: null");
                        //if not found in onWaiting Collection, check in onProgress Collection
                        db.collection("onProgress")
                                .whereEqualTo("partnerUID",FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if(task.getResult().getDocuments().size() == 0){
                                                //show no order
                                                noOrder.setVisibility(View.VISIBLE);
                                                runningOrder.setVisibility(View.GONE);
                                                waitingOrder.setVisibility(View.GONE);
                                            }else{
                                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                preferencesManager.setProgressId(document.getId());
                                                //show running order
                                                noOrder.setVisibility(View.GONE);
                                                runningOrder.setVisibility(View.VISIBLE);
                                                waitingOrder.setVisibility(View.GONE);

                                                tvKodeOrder.setText(preferencesManager.getProgressId());
                                                setTimeLeft(document.getString("dateAndTime"),
                                                        document.getLong("duration").intValue(),
                                                        document.getString("timeType"));
                                            }
                                        } else {
                                            Log.d("Error", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                }
            });

        }else{
            noOrder.setVisibility(View.GONE);
            runningOrder.setVisibility(View.VISIBLE);
            waitingOrder.setVisibility(View.GONE);

            getRunningOrderDataFromFirestore();
        }

        itemTourGuide.setOnClickListener((view) -> {
            if (preferencesManager.getNoWhatsapp().isEmpty() || preferencesManager.getKota().isEmpty()){
                showFillDataDialog();
            }else if(preferencesManager.getProgressId().isEmpty()){
                checkLocationPermission();
            }else{
                startActivity(new Intent(MainActivity.this, TourGuideActivity.class));
            }
        });

        itemWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WalletActivity.class));
            }
        });

        itemHistory.setOnClickListener((view) -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });

        itemAccount.setOnClickListener((view -> {
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
        }));

        cvRunningOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TourGuideActivity.class));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("onWaiting").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Proses pencarian berhasil dibatalkan", Toast.LENGTH_LONG).show();
                                Log.d("Success", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Error", "Error deleting document", e);
                            }
                        });
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferencesManager.getNoWhatsapp().isEmpty() || preferencesManager.getKota().isEmpty()){
                    showFillDataDialog();
                }else if(preferencesManager.getProgressId().isEmpty()){
                    checkLocationPermission();
                }
            }
        });
    }

    private void checkLocationPermission() {
        //check location permission
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            //when permission is not granted
            //request permission
            ActivityCompat.requestPermissions(Objects.requireNonNull(this),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                    },
                    100);
        }
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        loadingDialog.show(fm, "fragment_loading_dialog");
    }

    private void showFillDataDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FillDataDialog fillDataDialog = new FillDataDialog();
        fillDataDialog.show(fm, "fragment_fill_data");
    }

    private void getRunningOrderDataFromFirestore() {
        db.collection("onProgress").document(preferencesManager.getProgressId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Error", "Listen failed.", e);
                    return;
                }

                if (document != null && document.exists()) {
                    Log.d("Success", "DocumentSnapshot data: " + document.getData());
                    //show running order
                    noOrder.setVisibility(View.GONE);
                    runningOrder.setVisibility(View.VISIBLE);

                    tvKodeOrder.setText(preferencesManager.getProgressId());
                    setTimeLeft(document.getString("dateAndTime"),
                            document.getLong("duration").intValue(),
                            document.getString("timeType"));
                } else {
                    Log.d("Error", "No such document");
                    preferencesManager.setProgressId("");
                    //show no order
                    noOrder.setVisibility(View.VISIBLE);
                    runningOrder.setVisibility(View.GONE);
                    waitingOrder.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setTimeLeft(String dateAndTime, int duration, String timeType) {
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateAndTime);

            if(startDate.after(new Date())){
                //timer not started
                tvTimeLeft.setText("Waktu belum berjalan");
            }
            else{
                Calendar c = Calendar.getInstance();
                c.setTime(startDate);
                if (timeType.equals("Jam")) {
                    c.add(Calendar.HOUR, duration);
                } else {
                    c.add(Calendar.DATE, duration);
                }
                Date endDate = c.getTime();

                if(endDate.after(new Date())){
                    //time is still running
                    long durationLeft = endDate.getTime() - (new Date().getTime());
                    new CountDownTimer(durationLeft, 1000) {
                        public void onTick(long m) {
                            long hari = TimeUnit.MILLISECONDS.toDays(m);
                            long jam = TimeUnit.MILLISECONDS.toHours(m) % 24;
                            long menit = TimeUnit.MILLISECONDS.toMinutes(m) % 60;
                            long detik = TimeUnit.MILLISECONDS.toSeconds(m) % 60;

                            tvTimeLeft.setText(
                                    String.format("%d Hari : %d Jam :  %d Menit : %d Detik", hari,jam,menit,detik)
                            );
                        }

                        public void onFinish() {
                            //times out
                            tvTimeLeft.setText("Waktu Berakhir");
                        }
                    }.start();
                }
                else{
                    //times out
                    tvTimeLeft.setText("Waktu Berakhir");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //initialize location manager
        LocationManager locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE
        );

        //check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    if (location != null) {
                        //when location result is not null
                        //set latitude
                        latitude = location.getLatitude();
                        //set longitude
                        longitude = location.getLongitude();

                        //insert data to onWaiting Collection
                        startSearchTourism();
                    } else {
                        //when location is null
                        //initialize location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                //initialize location
                                Location location1 = locationResult.getLastLocation();
                                //set latitude
                                latitude = location1.getLatitude();
                                //set longitude
                                longitude = location1.getLongitude();
                            }
                        };
                        //request location update
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());
                    }
                }
            });

        }else{
            //when location service is not enable
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void startSearchTourism() {
        showDialog();

        Map<String, Object> waitingData = new HashMap<>();
        waitingData.put("city", preferencesManager.getKota());
        waitingData.put("latitude", latitude);
        waitingData.put("longitude", longitude);

        DocumentReference onProgressRef = db.collection("onWaiting").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        onProgressRef
                .set(waitingData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingDialog.dismiss();

                        //show waiting order
                        noOrder.setVisibility(View.GONE);
                        runningOrder.setVisibility(View.GONE);
                        waitingOrder.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();

                        Log.w("Error", "Error writing document", e);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100
                && grantResults.length > 0
                && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }
        else{
            ///permision denied
            Toast.makeText(this, "Aplikasi memerlukan izin lokasi untuk dapat memulai proses pencarian turis. Berikan izin lokasi kepada aplikasi dan silahkan coba kembali.", Toast.LENGTH_LONG).show();
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        }
    }
}