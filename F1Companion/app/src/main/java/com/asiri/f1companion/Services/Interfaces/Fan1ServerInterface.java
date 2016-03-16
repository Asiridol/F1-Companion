package com.asiri.f1companion.Services.Interfaces;

import com.asiri.f1companion.Services.Models.ConstructorsModel;
import com.asiri.f1companion.Services.Models.DriversModel;
import com.asiri.f1companion.Services.Models.LeaderboardsModel;
import com.asiri.f1companion.Services.Models.RacesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by asiri on 3/16/2016.
 */
public interface Fan1ServerInterface {

    @GET("/alldrivers")
    Call<DriversModel> getAllDriversAsync();

    @GET("/allconstructors")
    Call<ConstructorsModel> getAllConstructorsAsync();

    @GET("/raceschedule")
    Call<RacesModel> getAllRacesScheduledAsync();

    @GET("/leaderboard")
    Call<LeaderboardsModel> getLeaderboardAsync();
}
