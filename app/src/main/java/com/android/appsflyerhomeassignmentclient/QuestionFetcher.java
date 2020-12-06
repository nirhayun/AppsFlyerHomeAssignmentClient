package com.android.appsflyerhomeassignmentclient;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class QuestionFetcher extends AsyncTask<Void, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(Void... params) {

        String str = "http://10.0.2.2:5000/api/v1/resources/question";
        HttpURLConnection urlConn;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setReadTimeout(2000 /* milliseconds */);
            urlConn.setConnectTimeout(2500 /* milliseconds */);
            urlConn.setRequestMethod("GET");
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return new JSONObject(stringBuffer.toString());
        } catch (Exception ex) {
            Log.e("AppsFlyer", "Fetching question failed: ", ex);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        if (response != null) {
            try {
                Log.e("AppsFlyer", "Successfully got question: " + response.getString("question"));
            } catch (JSONException ex) {
                Log.e("AppsFlyer", "Failure", ex);
            }
        }
    }
}