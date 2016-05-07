package com.asiri.f1companion.Services;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.asiri.f1companion.Commons.Defaults;
import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.Models.Leaderboard;
import com.asiri.f1companion.Services.Interfaces.Fan1ServerInterface;
import com.asiri.f1companion.Services.Interfaces.LoadDataListener;
import com.asiri.f1companion.Services.Models.AllStatusesModel;
import com.asiri.f1companion.Services.Models.AllTimeStatisticsModel;
import com.asiri.f1companion.Services.Models.CurrentSeasonModel;
import com.asiri.f1companion.Services.Models.LapTimesModel;
import com.asiri.f1companion.Services.Models.LeaderboardsModel;
import com.asiri.f1companion.Services.Models.PitStopsModel;
import com.asiri.f1companion.Services.Models.QualifyingResultsModel;
import com.asiri.f1companion.Services.Models.RaceResultsModel;
import com.asiri.f1companion.UI.Activities.DriverInformationActivity;
import com.asiri.f1companion.UI.Activities.ExtendedResultsActivity;
import com.asiri.f1companion.UI.Activities.RaceResultActivity;
import com.asiri.f1companion.UI.Activities.SplashActivity;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asiri on 3/14/2016.
 */
public class ExtendedDetailsService
{
    final OkHttpClient client=new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    final Retrofit adapter = new Retrofit.Builder()
            .baseUrl(Defaults.BASEURL_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    final Fan1ServerInterface serviceInstance;

    Realm realm;

    public ExtendedDetailsService()
    {
        serviceInstance=adapter.create(Fan1ServerInterface.class);
    }

    public void getRaceResults(final String season, final String round, final LoadDataListener loadDataListener)
    {
        Call<RaceResultsModel> call=serviceInstance.getRaceResultsAsync("/raceresults?season=" + season + "&round=" + round);
        call.enqueue(new Callback<RaceResultsModel>() {
            @Override
            public void onResponse(Call<RaceResultsModel> call, Response<RaceResultsModel> response) {

                loadDataListener.finishedLoadingDataWith(response.body(),Defaults.RequestType.RaceResults);
            }

            @Override
            public void onFailure(Call<RaceResultsModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error loading Race Results");
                t.printStackTrace();
            }
        });
    }

    public void getQualifyingResults(String season, String round, final LoadDataListener loadDataListener)
    {
        Call<QualifyingResultsModel> call=serviceInstance.getQualifyingResutlsAsync("/qualifyingresults?season=" + season + "&round=" + round);
        call.enqueue(new Callback<QualifyingResultsModel>() {
            @Override
            public void onResponse(Call<QualifyingResultsModel> call, Response<QualifyingResultsModel> response) {
                loadDataListener.finishedLoadingDataWith(response.body(),Defaults.RequestType.QualifyingResults);
            }

            @Override
            public void onFailure(Call<QualifyingResultsModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error loading qualifying results");
                t.printStackTrace();
            }
        });
    }

    public void loadLeaderboard(Context context,final LoadDataListener loadDataListener)
    {
        realm=Realm.getInstance(context);

        Call<LeaderboardsModel> call=serviceInstance.getLeaderboardAsync();

        call.enqueue(new Callback<LeaderboardsModel>()
        {
            @Override
            public void onResponse(Call<LeaderboardsModel> call, Response<LeaderboardsModel> response) {

                realm.beginTransaction();
                realm.clear(Leaderboard.class);
                realm.commitTransaction();
                realm.beginTransaction();

                for(int i=0;i<response.body().getLeaderboard().length ; i++)
                {
                    Leaderboard leaderboard=realm.createObject(Leaderboard.class);
                    leaderboard.setObject(response.body().getLeaderboard()[i]);
                    Driver driver=realm.where(Driver.class).equalTo("driverId",response.body().getLeaderboard()[i].getDriverId()).findFirst();
                    leaderboard.setDriver(driver);
                }

                realm.commitTransaction();
                loadDataListener.finishedLoadingData();
            }

            public void checkRealm()
            {
                RealmResults<Leaderboard> leaderboards=realm.where(Leaderboard.class).findAll();
                System.out.println("Leaderboards # : " + leaderboards.size());
                for (Leaderboard c:leaderboards)
                {
                    System.out.println(c);
                }
            }

            @Override
            public void onFailure(Call<LeaderboardsModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error updating leaderboard");
            }
        });
    }

    public void updateLeaderboard(Context context, final LoadDataListener loadDataListener)
    {
        realm=Realm.getInstance(context);
        Call<LeaderboardsModel> call=serviceInstance.getLeaderboardAsync();

        call.enqueue(new Callback<LeaderboardsModel>()
        {
            @Override
            public void onResponse(Call<LeaderboardsModel> call, Response<LeaderboardsModel> response) {

                realm.beginTransaction();
                realm.clear(Leaderboard.class);
                realm.commitTransaction();
                realm.beginTransaction();

                for(int i=0;i<response.body().getLeaderboard().length ; i++)
                {
                    Leaderboard leaderboard=realm.createObject(Leaderboard.class);
                    leaderboard.setObject(response.body().getLeaderboard()[i]);
                    Driver driver=realm.where(Driver.class).equalTo("driverId",response.body().getLeaderboard()[i].getDriverId()).findFirst();
                    leaderboard.setDriver(driver);
                }

                realm.commitTransaction();
            }

            public void checkRealm()
            {
                RealmResults<Leaderboard> leaderboards=realm.where(Leaderboard.class).findAll();
                System.out.println("Leaderboards # : " + leaderboards.size());
                for (Leaderboard c:leaderboards)
                {
                    System.out.println(c);
                }
            }

            @Override
            public void onFailure(Call<LeaderboardsModel> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    public void getLapTimes(String season, String round, String driverId,final LoadDataListener loadDataListener)
    {
        Call<LapTimesModel> call=serviceInstance.getLapTimesAsync("/laptimes?id=" + driverId + "&season=" + season + "&round=" + round);
        call.enqueue(new Callback<LapTimesModel>()
        {
            @Override
            public void onResponse(Call<LapTimesModel> call, Response<LapTimesModel> response) {
                loadDataListener.finishedLoadingDataWith(response.body().getLapTimes(),Defaults.RequestType.LapTimes);
            }

            @Override
            public void onFailure(Call<LapTimesModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error loading laptimes");
            }
        });
    }

    public void getPitStops(String season, String round, String driverId,final LoadDataListener loadDataListener)
    {
        Call<PitStopsModel> call=serviceInstance.getPitStopsAsync("/pitstops?id=" + driverId + "&season=" + season + "&round=" + round);
        call.enqueue(new Callback<PitStopsModel>()
        {
            @Override
            public void onResponse(Call<PitStopsModel> call, Response<PitStopsModel> response) {
                loadDataListener.finishedLoadingDataWith(response.body().getPitStops(),Defaults.RequestType.PitStops);
            }

            @Override
            public void onFailure(Call<PitStopsModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error loading pitstops");
            }
        });
    }

    public void getAllStatuses(String driverId, final LoadDataListener loadDataListener)
    {
        Call<AllStatusesModel> call=serviceInstance.getAllStatuses("/alltimestatuses?id=" + driverId);
        call.enqueue(new Callback<AllStatusesModel>()
        {
            @Override
            public void onResponse(Call<AllStatusesModel> call, Response<AllStatusesModel> response) {
                loadDataListener.finishedLoadingDataWith(response.body(),Defaults.RequestType.AllStatuses);
            }

            @Override
            public void onFailure(Call<AllStatusesModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error loading time statuses");
            }
        });
    }

    public void getAllSeasonsStatistics(String driverId, final LoadDataListener loadDataListener)
    {
        Call<AllTimeStatisticsModel> call=serviceInstance.getAllTimeStatistics("/allseasons?id=" + driverId);
        call.enqueue(new Callback<AllTimeStatisticsModel>()
        {
            @Override
            public void onResponse(Call<AllTimeStatisticsModel> call, Response<AllTimeStatisticsModel> response) {
                loadDataListener.finishedLoadingDataWith(response.body(),Defaults.RequestType.AllSeasonsStatistics);
            }

            @Override
            public void onFailure(Call<AllTimeStatisticsModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error loading all seasons information");
            }
        });
    }

    public void getCurrentSeason(String driverId, final LoadDataListener loadDataListener)
    {
        Call<CurrentSeasonModel> call=serviceInstance.getCurrentSeason("/current?id=" + driverId);
        call.enqueue(new Callback<CurrentSeasonModel>()
        {
            @Override
            public void onResponse(Call<CurrentSeasonModel> call, Response<CurrentSeasonModel> response) {
                loadDataListener.finishedLoadingDataWith(response.body(),Defaults.RequestType.CurrentSeason);
            }

            @Override
            public void onFailure(Call<CurrentSeasonModel> call, Throwable t) {
                loadDataListener.finishedLoadingDataWithError("Error loading current season information");
            }
        });
    }
}
