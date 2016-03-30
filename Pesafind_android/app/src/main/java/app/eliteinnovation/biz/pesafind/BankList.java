package app.eliteinnovation.biz.pesafind;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.internal.zzid.runOnUiThread;


/**
 * A simple {@link Fragment} subclass.
 */
public class BankList extends Fragment {
    ListView lstBanks;
    private ProgressDialog pDialog;
    // JSON Response node names
    private static String KEY_SUCCESS = "status";
    public static final String TAG_BANKS = "banks", TAG_NAME = "bankname",TAG_URL="bankurl",
            TAG_ID = "bankid",TAG_ABOUT="about",TAG_BRANCHES="branches",TAG_IMURL="imageurl";
    JSONArray bank = null;
    // Hashmap for ListView
    public static ArrayList<HashMap<String, String>> bankDetails;
    ArrayList<Bank> blist= new ArrayList<Bank>();
    Bank bankitem;
    private static String url = "http://eliteinnovations.biz/pesafind/retrieve.php?sid=2";
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    BankAdapter adapter;
    View rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rv = inflater.inflate(
                R.layout.fragment_bank_list, container, false);
        //init ImageLoader
        ImageLoader loader = ImageLoader.getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        lstBanks= (ListView) rv.findViewById(R.id.lst_banks);
        bankDetails  = new ArrayList<HashMap<String,String>>();
        new GetBanks().execute();
        return rv;
    }
    public class GetBanks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String id, name, imgurl, imageurl,about,branches;
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
                            branches=s.getString(TAG_BRANCHES);
                            about=s.getString(TAG_ABOUT);
                            imageurl=s.getString(TAG_IMURL);

                            // tmp hashmap for single county
                            HashMap<String, String> banks = new HashMap<String, String>();
                            bankitem= new Bank();
                            bankitem.setId(id);
                            bankitem.setName(name);
                            bankitem.setImgurl(imgurl);
                            bankitem.setAbout(about);
                            bankitem.setBranches(branches);
                            bankitem.setImageurl(imageurl);
                            blist.add(bankitem);

                            // adding each child node to HashMap key => value
                            banks.put(TAG_ID, id);
                            banks.put(TAG_NAME, name);
                            banks.put(TAG_URL,imgurl);
                            banks.put(TAG_ABOUT,about);
                            banks.put(TAG_BRANCHES,branches);
                            banks.put(TAG_IMURL,imageurl);

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
                                            getContext(),
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
                                getContext(),
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
            adapter = new BankAdapter(getActivity(),blist);
            lstBanks.setAdapter(adapter);
            pDialog.dismiss();
        }

    }
}
