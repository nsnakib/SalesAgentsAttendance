package com.sakib.salesagentsattendance;

import java.util.UUID;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import im.delight.android.location.SimpleLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Details extends AppCompatActivity {
    private EditText etname,etuserid;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;
    private Button submit;
    private SimpleLocation location;
    public double longitude;
    public double latitude ;
    ProgressDialog dialog;
    private String random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String id = i.getStringExtra("id");

        checkPermission();

        etname.setText(name);
        etuserid.setText(id);

        random = UUID.randomUUID().toString();

        location = new SimpleLocation(this);
        if (!location.hasLocationEnabled())
        {
            SimpleLocation.openSettings(this);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUser();
            }
        });

    }

    public void iamhere()
    {
        location.beginUpdates();
        latitude = location.getLatitude();
        longitude= location.getLongitude();

        Log.i("getLatitude:",latitude+"getLongitude"+ longitude);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    iamhere();
                } else {

                    dialog.dismiss();
                }
                return;
            }

        }
    }

    public void AddUser() {

        final String name = etname.getText().toString().trim();
        final String userid =etuserid.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etname.setError( "Name is required!" );
        }
        else if( TextUtils.isEmpty(userid))
        {
            etuserid.setError( "User id is required!" );
        }
        else
        {
            dialog = ProgressDialog.show(Details.this, "Loading", "Please wait...", true);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AddUserInterface.URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            AddUserInterface api = retrofit.create(AddUserInterface.class);

            Call<String> call = api.addUser(name,userid,String.valueOf(latitude),String.valueOf(longitude),random);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("Responsesaddactivity:", response.body().toString());
                    dialog.dismiss();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.i("onFailure","onFailure");

                }
            });

            onBackPressed();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
     void initView()
     {
         etname = findViewById(R.id.etname);
         etuserid = findViewById(R.id.etuserid);
         submit= findViewById(R.id.submit);
     }
    void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(Details.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(Details.this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {

                ActivityCompat.requestPermissions(Details.this
                        ,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        } else {

            iamhere();
        }
    }
}