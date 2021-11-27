package com.cpasnoor.weatherfeed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cpasnoor.weatherfeed.http.HttpApi;
import com.cpasnoor.weatherfeed.model.PinnedCities;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class ShowWeather extends AppCompatActivity {
    private final String API_KEY = ""; // Keep Your Open Weather API Key
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String BASE_IMAGE_URL = "https://openweathermap.org/img/wn/%s.png";

    String currentCity;
    TextView cityHeading;
    TextView cityDescription;
    TextView cityTemp;
    TextView cityCoord;
    TextView cityTempsValues;
    TextView citySunrise;
    TextView citySunset;

    ImageView cityWeatherIcon;

    HttpApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        Intent intent = getIntent();
        api = HttpApi.getInstance(getApplicationContext());
        currentCity = intent.getStringExtra("city");
        if (currentCity == null || currentCity.trim().isEmpty() || currentCity.trim().length() <= 3) {
            Toast.makeText(this, "Error: Invalid City Entered, go back", Toast.LENGTH_SHORT).show();
            return;
        }

        cityHeading = findViewById(R.id.text_view_details_city_name);
        cityDescription = findViewById(R.id.text_view_weather_desc);
        cityTemp = findViewById(R.id.text_view_weather_temp);
        cityCoord = findViewById(R.id.text_view_weather_coord);
        cityWeatherIcon = findViewById(R.id.image_view_weather_icon);
        cityTempsValues = findViewById(R.id.text_view_weather_temps);
        citySunrise = findViewById(R.id.text_view_weather_sunrise);
        citySunset = findViewById(R.id.text_view_weather_sunset);

        cityHeading.setText(currentCity);

        String url = getRequestURL(getQueryParams(currentCity));
        addRequestToQueue(getWeatherRequest(url));
    }

    public void goBack(View v) {
        finish();
    }

    private JsonObjectRequest getWeatherRequest(String url) {
        return new JsonObjectRequest(Request.Method.GET, url, null,
                response -> onWeatherResponseHandler(response),
                (error) -> Log.d("Error", error.toString())
        );
    }

    private String getQueryParams(String cityName) {
        return String.format("?q=%s&appid=%s&units=metric", cityName, API_KEY);
    }

    private String getIconUrl(String iconName) {
        return String.format(BASE_IMAGE_URL, iconName);
    }

    private String getRequestURL(String options) {
        return BASE_URL + options;
    }

    private void onWeatherResponseHandler(JSONObject response) {
        try {
            if (response.isNull("weather")) {
                Log.d("Response", "Response is null");
                return;
            }
            JSONObject weatherDesc = (JSONObject) response.getJSONArray("weather").get(0);
            JSONObject weatherMain = response.getJSONObject("main");
            JSONObject weatherCoords = (JSONObject)response.getJSONObject("coord");
            JSONObject weatherSystem = (JSONObject) response.getJSONObject("sys");
            setWeatherView(weatherDesc, weatherMain, weatherCoords, weatherSystem);
        } catch (JSONException e) {
            Log.d("ERROR", "Cannot Parse Request");
        }

    }

    private void setWeatherView(JSONObject weatherView, JSONObject weatherMain, JSONObject weatherCoords, JSONObject weatherSys) {
        try {
            String weatherDesc = weatherView.getString("description");
            String icon = weatherView.getString("icon");
            String weatherTemp = weatherMain.getString("temp");
            String Latitude = weatherCoords.getString("lat");
            String Longitude = weatherCoords.getString("lon");
            String MaxTemp = weatherMain.getString("temp_max");
            String MinTemp = weatherMain.getString("temp_min");
            int sunrise = weatherSys.getInt("sunrise");
            int sunset = weatherSys.getInt("sunset");
            Date sunRiseDate = new Date(sunrise * 1000);
            Date sunSetDate = new Date(sunset * 1000);

            loadWeatherIcon(getIconUrl(icon)); // Load Image wrt to current description

            cityDescription.setText(weatherDesc.substring(0, 1).toUpperCase() + weatherDesc.substring(1)); // Capitalize First Letter
            cityTemp.setText(String.format(getString(R.string.text_view_weather_temp_format), weatherTemp));
            cityCoord.setText(String.format(getString(R.string.text_view_weather_cords_format), Latitude, Longitude));
            cityTempsValues.setText(String.format(getString(R.string.text_view_weather_temps_format), MaxTemp, MinTemp));
            citySunrise.setText(String.format("%d:%d", sunRiseDate.getHours(), sunRiseDate.getMinutes()));
            citySunset.setText(String.format("%d:%d", sunSetDate.getHours(), sunSetDate.getMinutes()));
        } catch (JSONException NotHandlingForNow) { }
    }

    private void addRequestToQueue(JsonObjectRequest req) {
        api.addToRequestQueue(req);
    }

    private void loadWeatherIcon(String url) {
        Picasso.get()
                .load(url)
                .into(cityWeatherIcon);
    }

    public void pinLocation(View v) {
        PinnedCities.addToPin(currentCity);
        Snackbar.make(v, "City Pinned Successfully", Snackbar.LENGTH_SHORT).show();
    }
}