package com.example.korba.gdziejestmojpromotor2.endpoints;

import com.example.korba.gdziejestmojpromotor2.model.Router;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by korba on 19.12.16.
 */

public interface RoutersEndpoints {
    @GET("/api/routers/{addres}/")
    Call<Router> GetRouter(@Path("addres") String addres);
}
