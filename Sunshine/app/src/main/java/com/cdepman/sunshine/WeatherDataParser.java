package com.cdepman.sunshine;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataParser {

    /**
     * Given a string of the form returned by the api call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * retrieve the maximum temperature for the day indicated by dayIndex
     * (Note: 0-indexed, so 0 would refer to the first day).
     */
    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws JSONException {

        double max = new JSONObject(weatherJsonStr)
                .getJSONArray("list")
                .getJSONObject(dayIndex)
                .getJSONObject("temp")
                .getDouble("max");

        return max;
    }

    public static double getMinTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws JSONException {

        double min = new JSONObject(weatherJsonStr)
                .getJSONArray("list")
                .getJSONObject(dayIndex)
                .getJSONObject("temp")
                .getDouble("max");

        return min;
    }

    public static String getDescription(String weatherJsonStr, int dayIndex){
        throws JSONException {

            String description = new JSONObject(weatherJsonStr)
                    .getJSONArray("list")
                    .getJSONObject(dayIndex)
                    .getJSONObject("temp")
                    .getString("")
        }
    }

}