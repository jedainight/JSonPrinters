package com.example.jsonparserexample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
 
public class ServiceHandler {
	
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
 
    public ServiceHandler() {
 
    }
 
    
    //η παράμετρος url δηλώνει το url στο οποίο θα εκτελεστεί το αίτημα
    //η παράμετρος method δηλώνει τη μέθοδο που θα χρησιμοποιήσουμε για την επικοινωνία με το 
    //webservice (POST ή GET).
    

    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
 
    //η παράμετρος params αναφέρεται στα στοιχεία τα οποία θέλουμε να δώσουμε (parse) στο service
    //ο τρόπος αποστολής των παραμέτρων αλλάζει ανάλογα με τη μέθοδο που χρησιμοποιούμε για να επικοινωνήσουμε
    //με την υπηρεσία.Συνεπώς θα πρέπει πρώτα να γίνει έλεγχος της μεθοδου.
    

	public String makeServiceCall(String url, int method,
            List<NameValuePair> params) {
        try {
        	
            // Δημιουργούμε ένα http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Αναγνώριση μεθόδου
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // προσθήκη παραμέτρων με τη μέθοδο post
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
 
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == GET) {
                // η αποστολή παραμέτρων με τη μέθοδο get γίνεται μέσω του url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
 
                httpResponse = httpClient.execute(httpGet);
 
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;
 
    }
}
