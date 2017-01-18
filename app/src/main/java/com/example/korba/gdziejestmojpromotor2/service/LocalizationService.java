package com.example.korba.gdziejestmojpromotor2.service;

import com.example.korba.gdziejestmojpromotor2.endpoints.LocalizationEndpoints;
import com.example.korba.gdziejestmojpromotor2.endpoints.UserEndpoints;
import com.example.korba.gdziejestmojpromotor2.model.LecturersLocalization;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by korba on 15.12.16.
 */

public class LocalizationService {

    //public static String URL = "http://172.16.23.149:8080/";
    public static String URL = "http://192.168.1.2:8080/";

    public static ResponseBody UpdateLocalization(LecturersLocalization lecturersLocalization) {
        ResponseBody responseBody = new ResponseBody();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LocalizationEndpoints endpoint = retrofit.create(LocalizationEndpoints.class);
        try {
            Call<ResponseBody> call = endpoint.UpdateLocalization(lecturersLocalization);
            responseBody = call.execute().body();
            return responseBody;
        } catch (IOException e) {
            responseBody.setStatus("Error");
            return responseBody;
        }
    }
}

