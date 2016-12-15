package com.example.korba.gdziejestmojpromotor2.endpoints;

import com.example.korba.gdziejestmojpromotor2.model.LoginBody;
import com.example.korba.gdziejestmojpromotor2.model.RegisterBody;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by korba on 15.12.16.
 */

public interface UserEndpoints {
    @POST("/api/user/login")
    Call<ResponseBody> loginUser(@Body LoginBody Body);

    @PUT("/api/user/update")
    Call<ResponseBody> UpdateUser(@Body RegisterBody Body);

    @POST("/api/user")
    Call<ResponseBody> registerUser(@Body RegisterBody Body);

    @GET("/api/user/{email}/")
    Call<RegisterBody> GetUser(@Path("email") String email);

    @DELETE("/api/user/delete/{email}/")
    Call<ResponseBody> DeleteUser(@Path("email") String email );

}

