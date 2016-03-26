package com.asiri.f1companion.Services.Interfaces;

import com.asiri.f1companion.Services.Models.AllStatusesModel;
import com.asiri.f1companion.Services.Models.AllTimeStatisticsModel;
import com.asiri.f1companion.Services.Models.ConstructorsModel;
import com.asiri.f1companion.Services.Models.CurrentSeasonModel;
import com.asiri.f1companion.Services.Models.DriversModel;
import com.asiri.f1companion.Services.Models.LapTimesModel;
import com.asiri.f1companion.Services.Models.LeaderboardsModel;
import com.asiri.f1companion.Services.Models.PitStopsModel;
import com.asiri.f1companion.Services.Models.QualifyingResultsModel;
import com.asiri.f1companion.Services.Models.RaceResultsModel;
import com.asiri.f1companion.Services.Models.RacesModel;

import java.util.List;

import retrofit.http.Query;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

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

    @GET("")
    Call<RaceResultsModel> getRaceResultsAsync(@Url String path);

    @GET("")
    Call<QualifyingResultsModel> getQualifyingResutlsAsync(@Url String path);

    @GET("")
    Call<LapTimesModel> getLapTimesAsync(@Url String path);

    @GET("")
    Call<PitStopsModel> getPitStopsAsync(@Url String path);

    @GET("")
    Call<AllStatusesModel> getAllStatuses(@Url String path);

    @GET("")
    Call<AllTimeStatisticsModel> getAllTimeStatistics(@Url String path);

    @GET("")
    Call<CurrentSeasonModel> getCurrentSeason(@Url String path);
}
