package com.escape.reality.bloodeagle;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class home_java extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    SharedPreferences reg;
    String named = signup_java.register;
    String name,email;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //callAsynchronousTask();
        reg = getSharedPreferences(named, MODE_PRIVATE);
        name = reg.getString("name", "UserName");
        email = reg.getString("email", "Email");
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        ((TextView) nav_header.findViewById(R.id.name)).setText(name);
        ((TextView) nav_header.findViewById(R.id.email)).setText(email);
        navigationView.addHeaderView(nav_header);

    }
  /*  public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            notifier notified = new notifier(home_java.this);
                            notified.execute("");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 50000 ms
    }*/





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }


        // The action bar home/up action should open or close the drawer.




        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @SuppressWarnings("StatementWithEmptyBody")


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        if(id == R.id.about)
                        {
                            startActivity(new Intent(home_java.this,about.class));
                        }
                        if(id==R.id.feedback)
                        {
                            startActivity(new Intent(home_java.this,feedback.class));
                        }
                        if(id==R.id.nav_share)
                        {
                            String shareBody = "Use Blood Eagle to donate blood and save lives http://play.google.com/store/apps/details?id=com.escape.reality.bloodeagle";
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Blood Bank");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        }


                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }
    /* public void selectDrawerItem(MenuItem menuItem) {
         // Create a new fragment and specify the fragment to show based on nav item clicked
         Fragment fragment = null;
         Class fragmentClass;
         switch(menuItem.getItemId()) {
             case R.id.about:
                 fragmentClass = about.class;
                 break;
             case R.id.feedback:
                 fragmentClass = feedback.class;
                 break;
         }

         try {
             fragment = (Fragment) fragmentClass.newInstance();
         } catch (Exception e) {
             e.printStackTrace();
         }

         // Insert the fragment by replacing any existing fragment
         FragmentManager fragmentManager = getSupportFragmentManager();
         fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

         // Highlight the selected item has been done by NavigationView
         menuItem.setChecked(true);
         // Set action bar title
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         // Find our drawer view
         mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawerToggle = setupDrawerToggle();

         // Tie DrawerLayout events to the ActionBarToggle
         mDrawer.addDrawerListener(drawerToggle);
 // Inflate the header view at runtime

 // We can now look up items within the header if needed

         // Close the navigation drawer
         mDrawer.closeDrawers();

     }*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void be(View view) {
        Intent intent = new Intent(this, donor.class);
        startActivity(intent);
    }

    public void need(View view) {
       Intent intent = new Intent(this, donee.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_java Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.escape.reality.bloodeagle/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "home_java Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.escape.reality.bloodeagle/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
/*    class notifier extends AsyncTask<String,String,String>{
        public final String register = "regi";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference notiref = database.getReference("need");
        private String temp,phone,name,blood;
        public String[] needArray=null,numberArray=null;
        public List<String> dynNeedList = new ArrayList<String>();
        public List<String> dynNumberList = new ArrayList<String>();
        NotificationCompat.Builder mBuilder;
        SharedPreferences need = getSharedPreferences(register,MODE_PRIVATE);
        public notifier(home_java home_java) {

        }

        protected String doInBackground(String... params) {
            phone = need.getString("number","");
            name = need.getString("name","");
            blood = need.getString("blood","");
            DatabaseReference uref = database.getReference("message");
            uref.setValue("qwe");
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                    String str = String.valueOf(dataSnapshot.getValue());
                    System.out.println("hahahaaaa"+str);
                   String withoutBraces;
                    withoutBraces = (String) str.subSequence(1, str.length() - 1);
                    StringTokenizer st = new StringTokenizer(withoutBraces);
                    while (st.hasMoreElements()) {
                        temp = st.nextToken();

                        StringTokenizer st1 = new StringTokenizer(temp);

                        String number = st1.nextToken("=");
                        //System.out.println(name);
                        String tempNumber = st1.nextToken(",");
                        String needed = tempNumber.substring(1);
                        //System.out.println(number);

                        dynNeedList.add(needed);
                        dynNumberList.add(number);

                    }
                    //  ListIterator nameIterator = dynNameList.listIterator();
                    // ListIterator numberIterator = dynNumberList.listIterator();

			/*while(nameIterator.hasNext())
				System.out.println(nameIterator.next());
			while(numberIterator.hasNext())
				System.out.println(numberIterator.next());
----------------

                    //String[] nameArray = (String[]) dynNameList.toArray();
                    needArray = dynNeedList.toArray(new String[dynNeedList.size()]);
                    // for (int i = 0; i < nameArray.length; i++)
                    //   System.out.println(nameArray[i]);

                    numberArray = dynNumberList.toArray(new String[dynNumberList.size()]);
                    //for (int i = 0; i < numberArray.length; i++)
                    //  System.out.println(numberArray[i]);

                    //dynNameList.add();


                    // if(loader)
                    //   spinner.setVisibility(View.GONE);


                    for(int i=0;i<needArray.length;i++)
                    {
                        if(needArray[i].equalsIgnoreCase("1"))
                        {
                            mBuilder = new NotificationCompat.Builder(home_java.this);
                            mBuilder.setSmallIcon(R.drawable.drop);
                            mBuilder.setContentTitle("Blood Request");
                            mBuilder.setContentText(name + "needs blood of group" + blood + "Please contact him as soon as possible" + phone);
// Creates an explicit intent for an Activity in your app
                            Intent resultIntent = new Intent(home_java.this, donee.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(home_java.this);
// Adds the back stack for the Intent (but not the Intent itself)
                            stackBuilder.addParentStack(donee.class);
// Adds the Intent that starts the Activity to the top of the stack
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                            int mId=1;
                            mNotificationManager.notify(mId, mBuilder.build());
                        }

                    }

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
            notiref=database.getReference("Need");
            notiref.addChildEventListener(childEventListener);
            // sleep(7000);
            return null;
        }

    }*/


}

