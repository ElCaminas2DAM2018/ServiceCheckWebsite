package org.ieselcaminas.pmdm.servicecheckwebsite;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MyService extends Service {
    TaskCheck taskCheck = null;

    public class TaskCheck extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            int seconds = Integer.parseInt(strings[1]);
            String website = strings[0];
            while (!isCancelled()) {
                if (!openHttpConnection(website)) {
                    publishProgress(website);
                } else {
                    publishProgress(null);
                }
                try {
                    Thread.sleep(seconds * 1000);
                } catch (InterruptedException ex) {
                    //
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... website) {
            if (website == null) { // everything ok
                Log.d("Check", "connected Ok");
            } else {
                Toast.makeText(getApplicationContext(), "Not connected to " + website[0],
                        Toast.LENGTH_LONG).show();
            }
        }

        private boolean openHttpConnection(String urlString) {
            int response;

            try {
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                if (!(conn instanceof HttpURLConnection))
                    throw new IOException("Not an HTTP connection");
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setInstanceFollowRedirects(true);
                httpConn.connect();
                response = httpConn.getResponseCode();
                httpConn.disconnect();
                if (response == HttpURLConnection.HTTP_OK) {
                    return true;
                }
            } catch (Exception ex) {

            }
            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Bundle bundle = intent.getExtras();
        String website = bundle.getString("website");
        String seconds = bundle.getString("seconds");
        taskCheck = new TaskCheck();
        // If we want the app can start service on several web sites (several startService calls)
        //taskCheck.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, website, seconds);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
        // If we want it to work when the app is swiped out
        //return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskCheck.cancel(true);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}