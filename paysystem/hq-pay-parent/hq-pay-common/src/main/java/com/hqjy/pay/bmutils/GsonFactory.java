package com.hqjy.pay.bmutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonFactory {

    private static class GsonHolder {

        private static Gson gson = new GsonBuilder().serializeNulls().create();
    }

    public static Gson getGson() {
        return GsonHolder.gson;
    }
}
