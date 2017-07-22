package com.tobiasandre.goestetica.sync;

import com.tobiasandre.goestetica.database.model.Treatment;
import com.tobiasandre.goestetica.database.model.TreatmentType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by TobiasAndre on 22/07/2017.
 */

public interface InfoService {

    @GET("/TreatmentType")
    Call<List<TreatmentType>> discoverTreatmentTypes();

    @GET("/Treatment")
    Call<List<Treatment>> discoverTreatments();

}
