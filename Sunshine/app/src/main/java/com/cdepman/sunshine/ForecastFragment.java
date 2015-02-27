package com.cdepman.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by charlie on 2/27/15.
 */
public class ForecastFragment extends Fragment {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView forecastList = (ListView) rootView.findViewById(R.id.listView_Layout);

        String[] exampleData = {
                "Today - Sunny - 88/63",
                "Tomorrow - Cloudy - 81/67",
                "Thursday - Rainy - 81/67",
                "Friday - Meatballs - 81/67",
                "Friday - Meatballs - 81/67",
                "Friday - Meatballs - 81/67",
                "Friday - Meatballs - 81/67",
                "Friday - Meatballs - 81/67",
                "Friday - Meatballs - 81/67",
                "Friday - Meatballs - 81/67",
                "Friday - Meatballs - 81/67"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textView, exampleData);

        forecastList.setAdapter(adapter);

        return rootView;
    }

    private class FetchForecastTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94103,USA&units=metric&cnt=7");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    builder.append(line + "\n");
                }

                if (builder.length() == 0){
                    return null;
                }
                forecastJsonStr = builder.toString();
            } catch (IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e){
                        Log.e(LOG_TAG, "Error Closing Stream", e);
                    }
                }
            }
        }
    }
}
