package com.cpasnoor.weatherfeed.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cities {
    private static ArrayList<String> citiesList = new ArrayList(
            Arrays.asList(
                    "Toronto",
                    "Hyderabad",
                    "Vancouver",
                    "London",
                    "Dallas",
                    "Paris",
                    "Hong Kong",
                    "Rome",
                    "Amsterdam",
                    "Bangkok",
                    "Cairo",
                    "Delhi",
                    "Dubai",
                    "Honolulu"
            )
    );

    public static ArrayList<String> getCities() {
        return citiesList;
    }
}
