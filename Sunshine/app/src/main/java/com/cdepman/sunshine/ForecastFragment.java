package com.cdepman.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by charlie on 2/27/15.
 */
public class ForecastFragment extends Fragment {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    List<String> forecastData = new ArrayList<>();
    ArrayAdapter<String> adapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Log.v(LOG_TAG, "ITEM SELECTED");
        if (id == R.id.action_refresh){
            new FetchForecastTask().execute("94103");
            Log.v(LOG_TAG, "REFRESH SELECTED");
            return true;
        };
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView forecastList = (ListView) rootView.findViewById(R.id.listView_Layout);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textView, forecastData);

        forecastList.setAdapter(adapter);

        return rootView;
    }

    private class FetchForecastTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            Log.v(LOG_TAG, "Downloaded: " + result);
            WeatherDataParser parser = new WeatherDataParser();
            try {
                forecastData = new ArrayList<>(Arrays.asList(parser.getWeatherDataFromJson(result, 7)));
                adapter.clear();
                for (String forecast : forecastData){
                    adapter.add(forecast);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                forecastData = null;
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String postalCode = params[0];
            String country = "USA";
            final String SCHEME = "http";
            final String BASE = "api.openweathermap.org";

            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(SCHEME)
                    .authority(BASE)
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("forecast")
                    .appendPath("daily")
                    .appendQueryParameter("q", postalCode + "," + country)
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("cnt", "7");


            String myUrl = uriBuilder.build().toString();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr;

            try {
                URL url = new URL(myUrl);

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

                Log.v(LOG_TAG, "Forecast JSON String:" + forecastJsonStr);

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
            return forecastJsonStr;
        }
    }
}
