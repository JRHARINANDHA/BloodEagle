package com.escape.reality.bloodeagle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class LoginActivity extends AppCompatActivity {
        SharedPreferences sp;
        String prefs = signup_java.mypref;
        int reg;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                sp = getSharedPreferences(prefs, MODE_PRIVATE);
                reg = sp.getInt("myint", 0);
                if (reg == 1) {
                        startActivity(new Intent(this, home_java.class));
                        finish();
                }
                setContentView(R.layout.register_button);
        }


        public void register_page(View view)

        {
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
                if (gps_enabled) {


                        if (!isconnect) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                                dialog.setMessage("Internet Connection Not Available");
                                dialog.setPositiveButton(("Turn On"), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                // TODO Auto-generated method stub

                                                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                                startActivity(intent);

                                        }
                                });
                                dialog.setNegativeButton(("Cancel"), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                // TODO Auto-generated method stub

                                        }
                                });
                                dialog.show();


                        } else {
                            //findViewById(R.id.loading).setVisibility(View.VISIBLE);
                                Intent intent = new Intent(this, signup_java.class);
                               startActivity(intent);
                                finish();
                        }
                }
        }
}



