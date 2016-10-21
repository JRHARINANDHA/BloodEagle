package com.escape.reality.bloodeagle;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

//import com.firebase.client.Firebase;



public class signup_java extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

   public boolean gps_enabled = false;
    public boolean network_enabled = false;
    Button button;
    public String distric = "";
    SharedPreferences reg;
    public EditText name_m;
    private EditText email_m;
    private EditText phone_m;
    private EditText blood_m;
    private int email_flag=0;
    private int name_flag=0;
    private int blood_flag=0;
    private int phone_flag=0;
    private String email;
    public String name="hello";
    private String phone;
    private String blood;
    AlertDialog levelDialog;
    EditText mEditInit;
    private int flag=1;
    public static final String register = "regi";
    public static final String mypref="myprefs";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SharedPreferences sp;
    Firebase postRef,posteRef;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public double currentLatitude;
    public double currentLongitude;
    Location location;
    private static final int REQUEST_CODE_LOCATION=2;
    ProgressDialog progressDialog;
ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        reg=getSharedPreferences(register,MODE_PRIVATE);
        sp=getSharedPreferences(mypref,MODE_PRIVATE);
        progressDialog=new ProgressDialog(signup_java.this);
        loader=(ProgressBar) findViewById(R.id.loading);
        if (loader != null) {
            loader.setVisibility(View.GONE);
        }
        button=(Button)findViewById(R.id.register_cloud);

        //------------------------------------------------------------------------------------------


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Intent intent=new Intent(signup_java.this,home_java.class);
               //startActivity(intent);
                button.setEnabled(false);
                signup_java.this.progressDialog.setMessage("Please Wait");
                signup_java.this.progressDialog.setCancelable(false);
                signup_java.this.progressDialog.show();
               // loader.setVisibility(View.VISIBLE);
                attempt_register();
                confirm_register();
                //successfull_registration();

            }
        });


        //------------------------------------------------------------------------------------------
        mEditInit = (EditText) findViewById(R.id.blood_m);
        mEditInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_bloodlist();
            }


        });


        //------------------------------------------------------------------------------------------



        //------------------------------------------------------------------------------------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    public void successfull_registration(){

        posteRef = new Firebase("https://life-drops-8450b.firebaseio.com/user/AB-/Coimbatore");
        //posteRef= posteRef.child(phone).child(name);
        posteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    String neame = postsnap.toString();
                    Log.e("name", neame);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public void attempt_register() {
        //--------------------------------------------
        name_m = (EditText) findViewById(R.id.name_m);
        email_m = (EditText) findViewById(R.id.email_m);
        phone_m = (EditText) findViewById(R.id.phone_m);
        blood_m = (EditText) findViewById(R.id.blood_m);
        //---------------------------------------------

        name_m.setError(null);
        email_m.setError(null);
        phone_m.setError(null);
        blood_m.setError(null);


        email = email_m.getText().toString();
        name = name_m.getText().toString();
        name=name.replaceAll("\\s+","");
        phone = phone_m.getText().toString();
        blood =blood_m.getText().toString();
        SharedPreferences.Editor edito = reg.edit();
        edito.putString("name",name);
        edito.putString("number",phone);
        edito.putString("blood",blood);
        edito.commit();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            email_m.setError(getString(R.string.email_required));
            focusView = email_m;
            cancel = true;


            email_flag=1;
        }
        else
        {
            email_flag=2;
            if(email.contains("@")&&((email.contains(".com"))||(email.contains(".in"))))
            {}
            else
            {
                email_m.setError(getString(R.string.email_incorrect));
                focusView = email_m;
                cancel = true;
                email_flag=1;
            }
        }
        if (TextUtils.isEmpty(name)) {
            name_m.setError(getString(R.string.name_required));
            focusView = name_m;
            cancel = true;
            name_flag=1;
        }
        else
        {
            name_flag=2;
            if(name.length()<4)
            {
                name_flag=1;
                name_m.setError("Name should contain at least 4 characters");
            }

        }

        if (TextUtils.isEmpty(phone)) {
            phone_m.setError(getString(R.string.phone_required));
            focusView = phone_m;
            cancel = true;
            phone_flag=1;
        }
        else
        {
            phone_flag=2;
            if(phone.length()==10)
            {}
            else
            {
                phone_m.setError(getString(R.string.phone_incorrect));
                focusView = phone_m;
                cancel = true;
                phone_flag=1;
            }

        }
        if (TextUtils.isEmpty(blood)) {
            blood_m.setError(getString(R.string.blood_required));
            focusView = blood_m;
            cancel = true;
            blood_flag=1;
        }
        else
        {
            blood_flag=2;
        }



    }

    public void confirm_register() {
        Toast.makeText(getApplicationContext(), "Your location is found to be in " + distric, Toast.LENGTH_SHORT).show();
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Turn On Your Location");
            dialog.setPositiveButton(("Turn On"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    signup_java.this.progressDialog.dismiss();
                    loader.setVisibility(View.GONE);
                    //get gps
                }
            });
            dialog.setNegativeButton(("Cancel"), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
            button.setEnabled(true);
            signup_java.this.progressDialog.dismiss();
            loader.setVisibility(View.GONE);


        }
        if (gps_enabled) {
            if (distric == "" || distric == null || !network_enabled) {
                new AlertDialog.Builder(signup_java.this).setTitle("Location Error").setMessage("Please ensure your location and internet is turned on and try again").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Intent intent = new Intent(signup_java.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                }).show();
            }
            else if (email_flag == 2 && name_flag == 2 && phone_flag == 2 && blood_flag == 2) {
                DatabaseReference myRefe = database.getReference(blood).child(distric).child("users").child(phone);
                //myRefe = myRefe.child("distric");
                // myRefe.child("name").setValue(name);
                //myRefe.child("phone").setValue(phone);
                //successfull_registration();
                // postRef = new Firebase("https://life-drops-8450b.firebaseio.com/user");
                //postRef = postRef.child(blood);
                //Firebase zumaka = postRef.child(distric);
                //DatabaseReference myRef = myRefe.child("distric");
                //User user = new User(name,phone);
                //Map<String, User> users = new HashMap<String, User>();
                //users.put("distric",user);
                //myRefe.setValue(name);
                myRefe.setValue(name, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(getApplicationContext(), "Could not connect to our Database.Please Try Again Later" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            button.setEnabled(true);
                            signup_java.this.progressDialog.dismiss();
                            loader.setVisibility(View.GONE);
                        } else {

                            button.setEnabled(true);
                            SharedPreferences.Editor regeditor = sp.edit();
                            regeditor.putInt("myint", 1);
                            regeditor.commit();
                            signup_java.this.progressDialog.dismiss();
                            loader.setVisibility(View.GONE);
                            Intent intent1 = new Intent(signup_java.this, home_java.class);
                            startActivity(intent1);
                            finish();



                            }
                        }
                });


               /* myRefe.child("phone").setValue(phone, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError!=null)
                        {
                            Toast.makeText(getApplicationContext(), "Could not connect to our Database.Please Try Again Later" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            SharedPreferences.Editor regeditor = sp.edit();
                            regeditor.putInt("myint", 1);
                            regeditor.commit();
                            Intent intent = new Intent(signup_java.this, home_java.class);
                            startActivity(intent);
                            finish();


                        }
                    }
                });*/
            }
            else{
                signup_java.this.progressDialog.dismiss();
                button.setEnabled(true);
            }
        }
    }

    //-------------------------------------------------------------------------------------------------



    public void district_splitter(Firebase post)

    {

        post.child("name").setValue(name);
        post.child("email").setValue(email);



    }

    public void show_bloodlist(){
        if (flag == 1) {
            flag = 0;

            final CharSequence[] items = {" A+ ", " A- ", " B+ ", " B- ", " O+ ", " O- ", " AB+ ", " AB- "," A1+ "," A1- "," B1+ "," A1B+ "," A1B- "," A2+ "," A2- "," A2B+ "," A2B- "};

            // Creating and Building the Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(signup_java.this);
            builder.setTitle("Select Your Blood Group");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    mEditInit.setText(items[item]);
                    flag = 1;
                    levelDialog.dismiss();


                }
            });

            levelDialog = builder.create();
            levelDialog.show();
        }

    }
    @TargetApi(23)
    @Override
    public void onConnected(Bundle bundle) {

        if (Build.VERSION.SDK_INT>=23&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE_LOCATION);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                String lat = String.valueOf(currentLatitude);
                String lon = String.valueOf(currentLongitude);

                //Toast.makeText(this, lat + " WORKS " + lon + "", Toast.LENGTH_LONG).show();
                CityAsyncTask cst = new CityAsyncTask(this,
                        currentLatitude, currentLongitude);
                try {
                     distric = (cst.execute()).get();
                    distric=distric.replaceAll("\\s+","");
                    System.out.println(distric);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grandResults){
        if(requestCode==REQUEST_CODE_LOCATION){
            if(grandResults.length==1&&grandResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //Location location=LocationServices
            }
        }
    }
    class CityAsyncTask extends AsyncTask<String, String, String> {
        Activity act;
        double latitude;
        double longitude;
        public String city="";
        private ProgressDialog progressDialog = new ProgressDialog(signup_java.this);

        public CityAsyncTask(Activity act, double latitude, double longitude) {
            // TODO Auto-generated constructor stub
            this.act = act;
            this.latitude = latitude;
            this.longitude = longitude;

        }
        public CityAsyncTask(){}

        @Override
        protected String doInBackground(String... params) {
            String result = "a";
            Geocoder geocoder = new Geocoder(act, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (addresses.size() > 0)
                    city = addresses.get(0).getSubAdminArea().toString();
                Log.e("Addresses", "-->" + addresses);
                result = addresses.get(0).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            city=city.replaceAll("\\s+","");
            return city;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            this.progressDialog.setMessage("Please Wait");
            this.progressDialog.show();
            this.progressDialog.setCancelable(false);
            //progressDialog.show(signup_java.this,"","Please Wait",false);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(progressDialog.isShowing())
                progressDialog.dismiss();

        }

    }

}



//-----------------------------------------------------------------------------------------------------------
