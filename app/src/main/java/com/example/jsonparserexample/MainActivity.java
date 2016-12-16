package com.example.jsonparserexample;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {

	private ProgressDialog pDialog;
	 
    // URL όπου βρίσκεται το json αρχείο
    private static String url = "http://www.ct.aegean.gr/people/alexxx/ShowPrinters.php";
 
    // JSON αντικείμενα
    private static final String TAG_PRINTERS = "printers";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_STATUS = "status";
    private static final String TAG_SPECS = "specs";
    private static final String TAG_TYPE = "type";
    private static final String TAG_CONNECTION = "connection";
    private static final String TAG_CARDREADER = "Card Reader";
    
 
    // πίνακας προϊόντων Json
    JSONArray printers = null;
 
    // Hashmap για δημιουργία ListView
    ArrayList<HashMap<String, String>> printerList;
    
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		printerList = new ArrayList<HashMap<String, String>>();
		 
        
		new GetJsonContent().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class GetJsonContent extends AsyncTask<Void, Void, Void> {	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Εμφάνιση παραθύρου διαλόγου
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Παρακαλώ περιμένετε...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Δημιουργία αντικειμένου ServiceHandler
            ServiceHandler handler = new ServiceHandler();
 
            // Αποστολή αιτήματος στο service μέσω της makeServiceCall() με τη μέθοδο GET
            String jsonStr = handler.makeServiceCall(url, ServiceHandler.GET);
 
            Log.d("Response: ", "> " + jsonStr);
            
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Ανάγνωση πίνακα
                    printers = jsonObj.getJSONArray(TAG_PRINTERS);
 
                    // Ανάγνωση αντικειμένων πίνακα
                    for (int i = 0; i < printers.length(); i++) {
                        JSONObject pr = printers.getJSONObject(i);
                         
                        String id = pr.getString(TAG_ID);
                        String name = pr.getString(TAG_NAME);
                        String price = pr.getString(TAG_PRICE);
                        String status = pr.getString(TAG_STATUS);
                        
 
                        // Ανάγνωση εσωτερικού πίνακα
                        JSONObject specs = pr.getJSONObject(TAG_SPECS);
                        String type = specs.getString(TAG_TYPE);
                        String output = specs.getString(TAG_CONNECTION);
                        String resolution = specs.getString(TAG_CARDREADER);
 
                        // Δημιουργία hashmap για κάθε προϊόν
                        HashMap<String, String> product = new HashMap<String, String>();
 
                        // προσθήκη χαρακτηριστικών προϊόντων 
                        product.put(TAG_ID, id);
                        product.put(TAG_NAME, name);
                        product.put(TAG_PRICE, price);
                        product.put(TAG_STATUS, status);
                        product.put(TAG_TYPE, type);
                        product.put(TAG_CONNECTION, output);
                        product.put(TAG_CARDREADER, resolution);
 
                        // προσθήκη προϊόντος στη λίστα
                        printerList.add(product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Σφάλμα!", "Δεν είναι δυνατή η εξαγωγή δεδομένων από το συγκεκριμένο url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Απόκρυψη παραθύρου διαλόγου
            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, printerList,
                    R.layout.product, new String[] { TAG_NAME, TAG_PRICE,
                            TAG_STATUS,TAG_TYPE,TAG_CONNECTION, TAG_CARDREADER }, new int[] { R.id.name,
                            R.id.price, R.id.status, R.id.type, R.id.output, R.id.resolution });
 
            setListAdapter(adapter);
        }
 
    }
}
