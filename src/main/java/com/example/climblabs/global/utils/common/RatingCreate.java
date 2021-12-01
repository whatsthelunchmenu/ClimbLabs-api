package com.example.climblabs.global.utils.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RatingCreate {

    public static Map<Integer, String> ratings = new LinkedHashMap<Integer, String>() {
        {
            put(5, "★★★★★");
            put(4, "★★★★");
            put(3, "★★★");
            put(2, "★★");
            put(1, "★");
        }
    };

    public static Map<Integer, String> getRatings(){
        return ratings;
    }

    public RatingCreate() {
    }

}
