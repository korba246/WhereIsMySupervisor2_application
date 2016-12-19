package com.example.korba.gdziejestmojpromotor2.endpoints;

import com.example.korba.gdziejestmojpromotor2.model.Router;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by korba on 19.12.16.
 */

public interface RoutersEndpoints {
    @GET("/api/router")
    Call<Router> GetRouter(String addres);
}
