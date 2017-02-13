package com.example.sushilverma.mavync;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import java.io.FileOutputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fmanager;
    private FragmentTransaction transaction;
    private CreateShipment createShipment;
    private DrawerLayout drawer;
    private  Toolbar toolbar;
    private  NavigationView navigationView;
    private boolean flag=true;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  ArrayList<String> citylist;
    private  ArrayList<String> citylist2;
    private Open_Catrgy_Dbhlper open_ctrgy_db;

    private City_state_Dbhlper city_list;
    private City_state_Dbhlper city_db;


    void showDialog() {
        DialogFragment newFragment1 = Logoutfreg.newInstance(R.string.alert_dialog_two_buttons_title);
        newFragment1.show(getSupportFragmentManager(), "dialog");
    }



    void showDialog_if_exit_app() {
        DialogFragment newFragment2 = App_exit_alert_freg.newInstance(R.string.alert_dialog_two_buttons_title_if_exit_app);
        newFragment2.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");

        finish();
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fmanager=getSupportFragmentManager();

      /*   city_db=new City_state_Dbhlper(HomeActivity.this);
        open_ctrgy_db=new Open_Catrgy_Dbhlper(HomeActivity.this);
       open_ctrgy_vehicle_db=new Open_Catrgy_vehicle_for_Dbhlper(HomeActivity.this);
        closed_ctrgy_db=new Closed_Catrgy_Dbhlper(HomeActivity.this);
        close_ctrgy_vehicle_db=new Closed_Catrgy_vehicle_for_Dbhlper(HomeActivity.this);*/


       // citylist=new ArrayList<String>(100);

        //citylist=(ArrayList<String>) getIntent().getSerializableExtra("city");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupDrawerContent(navigationView);

        if (savedInstanceState == null) {

            selectDrawerItem(navigationView.getMenu().getItem(0));
        }


        if(!isOnline())
        {
            toolbar.setNavigationIcon(null);
        }

    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass=null;
        switch(menuItem.getItemId())
        {
            case R.id.newship:
            //    if(flag==true) {

                    fragmentClass = CreateShipment.class;
                    flag=false;

             //   }
                break;
            case R.id.Shipment:
                fragmentClass = TabFragment.class;

                flag=true;
                break;


            case R.id.shiprating:
                fragmentClass = Ratingfreg.class;
                break;

            case R.id.shippayment:
                fragmentClass = Paymentfreg.class;
                flag=true;
                break;



            case R.id.tracking:
                fragmentClass = Track.class;
                flag=true;
                break;

            case R.id.jrnytracking:
                fragmentClass = TrackJourney.class;
                flag=true;
                break;

            case R.id.profile:
                fragmentClass = Myprofile.class;
                flag=true;
                break;

            case R.id.term:
                fragmentClass = T_C.class;
                flag=true;
                break;


            case R.id.contact:
                fragmentClass = Contact_Us.class;
                flag=true;
                break;

            case R.id.logout:
                // fragmentClass = ThirdFragment.class;
               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // alertDialogBuilder.setTitle("Exit Application?");
                alertDialogBuilder
                        .setMessage("Are You Sure To Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);


                                        writestateToInternalStorage();
                                        dialog.cancel();
                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                        finish();
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/

                showDialog();

                //fragmentClass = Logoutfreg.class;
                flag=true;
                break;
            default:
               // fragmentClass = FirstFragment.class;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment

        try {
            fmanager.beginTransaction().replace(R.id.container, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Highlight the selected item has been done by NavigationView

        menuItem.setChecked(true);


           // Set action bar title
        if(!menuItem.getTitle().equals("Logout"))
        setTitle(menuItem.getTitle());
           // Close the navigation drawer
        drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();

            showDialog_if_exit_app();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.newship) {
            // Handle the camera action

            transaction.replace(R.id.container,createShipment);
            transaction.commit();


        } else if (id == R.id.confirmship) {

        } else if (id == R.id.deliveredship) {

        } else if (id == R.id.shiprating) {

        } else if (id == R.id.shippayment) {

        } else if (id == R.id.contact) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/


    private void writestateToInternalStorage()
    {
        byte b=0;
        try {

            FileOutputStream fos = openFileOutput("loginstatus.txt", Context.MODE_PRIVATE);

            fos.write(b);
            fos.close();

        }
        catch (Exception e)
        {
            Log.i("Error_In_login", e.getMessage());

        }
    }

    boolean isOnline()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
public void onDestroy()
    {
        super.onDestroy();


        /*if(city_db.numberOfRows()>0)
            city_db.deletedb();


        if(open_ctrgy_db.numberOfRows()>0)
            open_ctrgy_db.deletedb();



        if(open_ctrgy_vehicle_db.numberOfRows()>0)
            open_ctrgy_vehicle_db.deletedb();


        if(closed_ctrgy_db.numberOfRows()>0)
            closed_ctrgy_db.deletedb();


        if(close_ctrgy_vehicle_db.numberOfRows()>0)
            close_ctrgy_vehicle_db.deletedb();*/




    }

}
