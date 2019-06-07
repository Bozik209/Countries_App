package com.example.countries_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Border_Activity extends AppCompatActivity {


    private String TAG = Border_Activity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private ListView listView;
    // JSON data url
    private static String Jsonurl = "https://restcountries.eu/rest/v2/all";
    ArrayList<HashMap<String, String>> contactJsonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onPostExecute: here1");

        setContentView(R.layout.activity_border_);
        // Get the transferred data from source activity.
//        Intent intent = getIntent();
//
//        String message = intent.getStringExtra("selectedItem");
//        TextView textView = (TextView)findViewById(R.id.borderList);
////        textView.setText(message);

    }



    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(Border_Activity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler();

            // request to json data url and getting response
            String jsonString = httpHandler.makeServiceCall(Jsonurl);
            Log.e(TAG, "Response from url: " + jsonString);
            if (jsonString != null) {
                try {
                    // Getting JSON Array node
                    JSONArray jsonArray = new JSONArray(jsonString);
                    //iterate loop
                    for (int i = 0; i < jsonArray.length(); i++) {

                        //get the JSON Object
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String name = obj.getString("name");
                        String borders = obj.getString("borders");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("name", name);
                        contact.put("borders", borders);


                        // adding contact to contact list
                        contactJsonList.add(contact);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Could not get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Could not get json from server.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            Log.d(TAG, "onPostExecute: here2");
            ListAdapter adapter = new SimpleAdapter(
                    Border_Activity.this, contactJsonList,
                    R.layout.list_item_border, new String[]{"borders"}, new int[]{R.id.borders});

            listView.setAdapter(adapter);
        }

    }
}
