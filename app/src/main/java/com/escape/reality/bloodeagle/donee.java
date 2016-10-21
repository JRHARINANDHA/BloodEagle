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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import static android.os.SystemClock.sleep;

public class donee extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Button button;
    private static String temp;
    public String[] nameArray=null,numberArray=null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    List<String> dynNameList = new ArrayList<String>();
    List<String> dynNumberList = new ArrayList<String>();
    ListView lv;
    String nnumber;
    EditText blood_n,mEditInit;
    String blood1;
    int flag=1;
    String distric;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public double currentLatitude;
    public double currentLongitude;
    AlertDialog levelDialog;
    Location location;
    SharedPreferences loc,need;
    private static final int REQUEST_CODE_LOCATION = 2;
    retriever retrieved;
    public static final String register = "regi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donee_screen);
        Firebase.setAndroidContext(this);
        button=(Button)findViewById(R.id.retrieve_cloud);
        //findViewById(R.id.loading).setVisibility(View.GONE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();
        //============================================================================================================================================
        loc = getSharedPreferences("district",MODE_PRIVATE);
        need = getSharedPreferences(register,MODE_PRIVATE);
        //==============================================================================================================================================================
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        blood_n =(EditText)findViewById(R.id.blood_n);
        mEditInit = (EditText)findViewById(R.id.blood_n);
        mEditInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_bloodlist();

            }
        });

        nnumber = need.getString("number","");
        System.out.println("the number is"+nnumber);
        blood_n.setError(null);


    }

    public void show_bloodlist() {
        if (flag == 1) {
            flag = 0;

            final CharSequence[] items = {" A+ ", " A- ", " B+ ", " B- ", " O+ ", " O- ", " AB+ ", " AB- "," A1+ "," A1- "," B1+ "," A1B+ "," A1B- "," A2+ "," A2- "," A2B+ "," A2B- "};

            // Creating and Building the Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(donee.this);
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


    public void display(View view) {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        final Boolean isconnect = activeNetworkInfo != null && activeNetworkInfo.isConnected();


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        if (gps_enabled) {
            blood1 = blood_n.getText().toString();
            SharedPreferences.Editor editor = loc.edit();
            editor.putString("blood", blood1);
            editor.commit();

            if (TextUtils.isEmpty(blood1)) {
                blood_n.setError("Blood is required");
            } else {
                if (distric != null && distric != "hahaha") {


                    //findViewById(R.id.loading).setVisibility(View.VISIBLE);
                   retrieved = new retriever(this);
                   retrieved.execute("");
                    DatabaseReference myRef = database.getReference("Need").child(distric).child(nnumber);
                    myRef.setValue("1");
                   // button.setEnabled(false);
                } else {
                    button.setEnabled(false);
                    new AlertDialog.Builder(this).setTitle("Location Error").setMessage("Please ensure your location is turned on and try again").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Intent intent = new Intent(donee.this, home_java.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }).show();
                }
                //startActivity(intent);
            }

        }
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Turn On Your Location");
            dialog.setPositiveButton(("Turn On"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
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
        }
    }

    @TargetApi(23)
    @Override
    public void onConnected(Bundle bundle) {

        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                String lat = String.valueOf(currentLatitude);
                String lon = String.valueOf(currentLongitude);

                // Toast.makeText(this, lat + " WORKS " + lon + "", Toast.LENGTH_LONG).show();
                CityAsyncTask cst = new CityAsyncTask(this,
                        currentLatitude, currentLongitude);
                try {
                    distric = (cst.execute()).get();
                    distric=distric.replaceAll("\\s+","");
                    SharedPreferences.Editor editor = loc.edit();
                    editor.putString("locate",distric);
                    editor.commit();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

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
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        // Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grandResults.length == 1 && grandResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Location location=LocationServices
            }
        }
    }
    class CityAsyncTask extends AsyncTask<String, String, String> {
        Activity act;
        double latitude;
        double longitude;
        public String city="hahaha";
        private ProgressDialog progressDialog = new ProgressDialog(donee.this);

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

        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();


        }

    }
    class retriever extends AsyncTask<String,String, String> {

        String none="0";
        ProgressDialog progressDialog = new ProgressDialog(donee.this);
        DatabaseReference myRefe = database.getReference("message");
        Activity activity;
        public retriever(Activity activity){
            this.activity = activity;
        }
        retriever(){};

        @Override
        protected String doInBackground(String... params) {
            myRefe.setValue("qwe");
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    none ="1";
                    String str = String.valueOf(dataSnapshot.getValue());
                    System.out.println("hahahaaaa"+none);
                    String withoutBraces = (String) str.subSequence(1, str.length() - 1);
                    StringTokenizer st = new StringTokenizer(withoutBraces);
                    while (st.hasMoreElements()) {
                        temp = st.nextToken();

                        StringTokenizer st1 = new StringTokenizer(temp);

                        String name = st1.nextToken("=");
                        //System.out.println(name);
                        String tempNumber = st1.nextToken(",");
                        String number = tempNumber.substring(1);
                        //System.out.println(number);

                        dynNameList.add(number);
                        dynNumberList.add(name);

                    }
                    //  ListIterator nameIterator = dynNameList.listIterator();
                    // ListIterator numberIterator = dynNumberList.listIterator();
			/*
			while(nameIterator.hasNext())
				System.out.println(nameIterator.next());
			while(numberIterator.hasNext())
				System.out.println(numberIterator.next());
		*/

                    //String[] nameArray = (String[]) dynNameList.toArray();
                    nameArray = dynNameList.toArray(new String[dynNameList.size()]);
                    // for (int i = 0; i < nameArray.length; i++)
                    //   System.out.println(nameArray[i]);

                    numberArray = dynNumberList.toArray(new String[dynNumberList.size()]);
                    //for (int i = 0; i < numberArray.length; i++)
                    //  System.out.println(numberArray[i]);

                    //dynNameList.add();


                    // if(loader)
                    //   spinner.setVisibility(View.GONE);

                    setList(nameArray, numberArray);


                }


                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    System.out.println("onChildcAdded:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    //  Comment newComment = dataSnapshot.getValue(Comment.class);
                    // String commentKey = dataSnapshot.getKey();
                    // Log.e("child",commentKey);
                    // ...
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d("TAG", "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String commentKey = dataSnapshot.getKey();
                    Log.e("child", commentKey);
                    // ...
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d("TAG", "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();
                    Log.e("child", commentKey);
                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG", "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(getApplicationContext(), "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            myRefe=database.getReference(blood1).child(distric);
            myRefe.addChildEventListener(childEventListener);
            sleep(7000);
            return none;
        }



        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            none="0";
            this.progressDialog.setMessage("Please Wait");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();

            // progressDialog.show(donee.this,"","Please Wait",false);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result=="0")
            {
                new AlertDialog.Builder(donee.this).setTitle("Please try again").setMessage("No matches found or Your internet connection might be slow ").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        progressDialog.dismiss();
                        finish();
                    }
                }).show();
               // button.setEnabled(true);
            }
            // findViewById(R.id.loading).setVisibility(View.GONE);
            if(progressDialog.isShowing())
                progressDialog.dismiss();



        }
        protected void setList(String a[],String b[]){

            if(progressDialog.isShowing())
                progressDialog.dismiss();
            setContentView(R.layout.display);
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(new CustomAdapter(donee.this, a, b));

        }

    }
}



