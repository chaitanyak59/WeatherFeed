package com.cpasnoor.weatherfeed.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PinnedCities {
    public static Set<String> pinnedCitiesSet = new HashSet<String>();

    public static String[] getPinnedCities() {
        String[] citiesArray = new String[pinnedCitiesSet.size()];
        pinnedCitiesSet.toArray(citiesArray);
        return citiesArray;
    }

    public static void addToPin(String city) {
        pinnedCitiesSet.add(city);
    }
}
