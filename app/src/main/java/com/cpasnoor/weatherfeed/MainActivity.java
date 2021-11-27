package com.cpasnoor.weatherfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cpasnoor.weatherfeed.model.Cities;
import com.cpasnoor.weatherfeed.model.PinnedCities;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    public AutoCompleteTextView autoCompleteTextView;
    TextView defaultNoPin;
    ListView pinnedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultNoPin = findViewById(R.id.text_view_deafult_no_pin);
        pinnedList = findViewById(R.id.list_view_pinned_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, Cities.getCities());

        autoCompleteTextView =  (AutoCompleteTextView)findViewById(R.id.auto_suggestion_countries_view);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
    }

    public void onSearchCity(View v) {
        String city = autoCompleteTextView.getText().toString();
        if(city == null || city.trim().isEmpty() || city.trim().length() <=3) {
            Snackbar.make(v, "Please Enter Valid City", Snackbar.LENGTH_SHORT).show();
            return;
        }
        autoCompleteTextView.setText("");
        showDetailsIntent(city);
    }

    public void showDetailsIntent(String city) {
        Intent viewWeather = new Intent(getApplicationContext(), ShowWeather.class);
        viewWeather.putExtra("city", city);
        startActivity(viewWeather);
    }

    public void checkPinnedList() {
        if(PinnedCities.getPinnedCities().length == 0) {
            defaultNoPin.setVisibility(View.VISIBLE);
            pinnedList.setVisibility(View.GONE);
        } else {
            final ArrayAdapter pinAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, PinnedCities.getPinnedCities()) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);
                    TextView view1 = (TextView) view;
                    view1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    return view;
                }
            };
            pinnedList.setAdapter(pinAdapter);
            pinnedList.setOnItemClickListener((myAdapter, myView, pos, mylng) -> {
                String pinnedCity =(pinnedList.getItemAtPosition(pos).toString());
                showDetailsIntent(pinnedCity);
            });
            defaultNoPin.setVisibility(View.GONE);
            pinnedList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPinnedList();
    }
}