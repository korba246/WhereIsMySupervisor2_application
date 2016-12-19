package com.example.korba.gdziejestmojpromotor2.endpoints;

import com.example.korba.gdziejestmojpromotor2.model.LecturersLocalization;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * Created by korba on 15.12.16.
 */

public interface LocalizationEndpoints {

    @PUT("api/localization/update")
    Call<ResponseBody> UpdateLocalization(@Body LecturersLocalization lecturersLocalization);
}