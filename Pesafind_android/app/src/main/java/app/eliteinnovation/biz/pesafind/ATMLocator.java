package app.eliteinnovation.biz.pesafind;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.eliteinnovation.biz.pesafind.facilitates.ClearableAutoCompleteTextView;

public class ATMLocator extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    ClearableAutoCompleteTextView searchBox;
    private ProgressDialog pDialog;
    // JSON Response node names
    private static String KEY_SUCCESS = "status";
    public static final String TAG_BANKS = "banks", TAG_NAME = "bankname",TAG_URL="bankurl",
            TAG_ID = "bankid";
    JSONArray bank = null;
    // Hashmap for ListView
    public static ArrayList<HashMap<String, String>> bankDetails;
    private static String url = "http://eliteinnovations.biz/pesafind/retrieve.php?sid=2";
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    private static int selectedBankID = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_atmlocator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init ImageLoader
        ImageLoader loader = ImageLoader.getInstance();
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("ATM Locator");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        bankDetails  = new ArrayList<HashMap<String,String>>();
        new GetBanks().execute();



        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new BankList(), "Banks");
        adapter.addFragment(new ATMFragment().newInstance(), "ATMs Map");
//        adapter.addFragment(new FragmentHospitalList(), "Category 3");
        viewPager.setAdapter(adapter);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_mpesaloc:
                                getSupportActionBar().setTitle(menuItem.getTitle());
                                Intent mpesaintent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mpesaintent);
                                return true;
                            case R.id.nav_atmloc:
                                getSupportActionBar().setTitle(menuItem.getTitle());
                                return true;
                            case R.id.nav_mpesacalc:
                                Intent mpesacaintent = new Intent(getApplicationContext(), MpesaCalculator.class);
                                startActivity(mpesacaintent );
                                return true;
                            case R.id.nav_atmcalc:
                                Intent atmcaintent = new Intent(getApplicationContext(), ATMCalculator.class);
                                startActivity(atmcaintent);
                                return true;
                        }
                        return true;
                    }
                });
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
    public void captureScreen()
    {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback()
        {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot)
            {
                // TODO Auto-generated method stub
                bitmap = snapshot;

                OutputStream fout = null;

                String filePath = System.currentTimeMillis() + ".jpeg";

                try
                {
                    fout = openFileOutput(filePath,
                            1);

                    // Write the string to the file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                }

                openShareImageDialog(filePath);
            }
        };

        ATMFragment.aMap.snapshot(callback);
    }
    public void openShareImageDialog(String filePath)
    {
        File file = getFileStreamPath(filePath);

        if(!filePath.equals(""))
        {
            final ContentValues values = new ContentValues(2);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            final Uri contentUriFile = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(android.content.Intent.EXTRA_STREAM, contentUriFile);
            startActivity(Intent.createChooser(intent, "Share Map Via"));
        }
        else
        {
            //This is a custom class I use to show dialogs...simply replace this with whatever you want to show an error message, Toast, etc.
            Toast.makeText(this, "Error sharing map", Toast.LENGTH_LONG);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_item_share:
                if(ATMFragment.aMap != null) {
                    captureScreen();
                }else{
                    Toast.makeText(this, "Nothing to share!", Toast.LENGTH_LONG);
                }
        }
        return super.onOptionsItemSelected(item);
    }
    public class GetBanks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ATMLocator.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String id, name, imgurl;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    final JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node for the object we called student
                    bank = jsonObj.getJSONArray(TAG_BANKS);

                    if(Integer.parseInt(jsonObj.getString(KEY_SUCCESS)) != 0) {
                        // looping through All Contacts
                        for (int i = 0; i < bank.length(); i++) {
                            JSONObject s = bank.getJSONObject(i);

                            id = s.getString(TAG_ID);
                            name = s.getString(TAG_NAME);
                            imgurl=s.getString(TAG_URL);

                            // tmp hashmap for single county
                            HashMap<String, String> banks = new HashMap<String, String>();
                            // adding each child node to HashMap key => value
                            banks.put(TAG_ID, id);
                            banks.put(TAG_NAME, name);
                            banks.put(TAG_URL,imgurl);

                            // adding county to county list
                            bankDetails.add(banks);
                        }
                    }else{
                        //no malls found
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                try {
                                    alert.showAlertDialog(
                                            ATMLocator.this,
                                            "Service response",
                                            jsonObj.getString("message"),
                                            false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pDialog.dismiss();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //no malls found
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        alert.showAlertDialog(
                                ATMLocator.this,
                                "Service response",
                                "No records found",
                                false);
                        pDialog.dismiss();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //searchBox.setAdapter(new DropdownAdapter(ATMLocator.this, bankDetails));
            pDialog.dismiss();
        }

    }
}
