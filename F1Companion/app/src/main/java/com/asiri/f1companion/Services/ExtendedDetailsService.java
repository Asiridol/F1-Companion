package com.asiri.f1companion.Services;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.asiri.f1companion.Commons.Defaults;
import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.Models.Leaderboard;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.Interfaces.Fan1ServerInterface;
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

    public void getRaceResults(final String season, final String round, final RaceResultActivity activity)
    {
        Call<RaceResultsModel> call=serviceInstance.getRaceResultsAsync("/raceresults?season=" + season + "&round=" + round);
        call.enqueue(new Callback<RaceResultsModel>() {
            @Override
            public void onResponse(Call<RaceResultsModel> call, Response<RaceResultsModel> response) {

                activity.finishedLoadingRaceResults(response.body());
            }

            @Override
            public void onFailure(Call<RaceResultsModel> call, Throwable t) {
                activity.mDialog.dismiss();
                Snackbar.make(activity.getCurrentFocus(), "Error loading Race Results", Snackbar.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void getQualifyingResults(String season, String round, final RaceResultActivity activity)
    {
        Call<QualifyingResultsModel> call=serviceInstance.getQualifyingResutlsAsync("/qualifyingresults?season=" + season + "&round=" + round);
        call.enqueue(new Callback<QualifyingResultsModel>() {
            @Override
            public void onResponse(Call<QualifyingResultsModel> call, Response<QualifyingResultsModel> response) {
                activity.finishedLoadingQualifyingResults(response.body());
            }

            @Override
            public void onFailure(Call<QualifyingResultsModel> call, Throwable t) {
                activity.mDialog.dismiss();
                Snackbar.make(activity.getCurrentFocus(), "Error loading Qualifying Results", Snackbar.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void loadLeaderboard(final SplashActivity activity)
    {
        realm=Realm.getInstance(activity.getBaseContext());
        activity.mDialog.setMessage("Updating Leaderboard");

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
                activity.mDialog.dismiss();

                activity.loadFinished();
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

    public void updateLeaderboard(Context context)
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

    public void getLapTimes(String season, String round, String driverId,final ExtendedResultsActivity dialog)
    {
        Call<LapTimesModel> call=serviceInstance.getLapTimesAsync("/laptimes?id=" + driverId + "&season=" + season + "&round=" + round);
        call.enqueue(new Callback<LapTimesModel>()
        {
            @Override
            public void onResponse(Call<LapTimesModel> call, Response<LapTimesModel> response) {
                dialog.finishedLoadingLapTimes(response.body().getLapTimes());
            }

            @Override
            public void onFailure(Call<LapTimesModel> call, Throwable t) {
                dialog.finishedLoadingLapTimes(null);
            }
        });
    }

    public void getPitStops(String season, String round, String driverId,final ExtendedResultsActivity dialog)
    {
        Call<PitStopsModel> call=serviceInstance.getPitStopsAsync("/pitstops?id=" + driverId + "&season=" + season + "&round=" + round);
        call.enqueue(new Callback<PitStopsModel>()
        {
            @Override
            public void onResponse(Call<PitStopsModel> call, Response<PitStopsModel> response) {
                dialog.finishedLoadingPitStops(response.body().getPitStops());
            }

            @Override
            public void onFailure(Call<PitStopsModel> call, Throwable t) {
                dialog.finishedLoadingPitStops(null);
            }
        });
    }

    public void getAllStatuses(String driverId, final DriverInformationActivity dialog)
    {
        Call<AllStatusesModel> call=serviceInstance.getAllStatuses("/alltimestatuses?id=" + driverId);
        call.enqueue(new Callback<AllStatusesModel>()
        {
            @Override
            public void onResponse(Call<AllStatusesModel> call, Response<AllStatusesModel> response) {
                dialog.finishedLoadingStatuses(response.body());
            }

            @Override
            public void onFailure(Call<AllStatusesModel> call, Throwable t) {
                dialog.finishedLoadingStatuses(null);
            }
        });
    }

    public void getAllSeasonsStatistics(String driverId, final DriverInformationActivity dialog)
    {
        Call<AllTimeStatisticsModel> call=serviceInstance.getAllTimeStatistics("/allseasons?id=" + driverId);
        call.enqueue(new Callback<AllTimeStatisticsModel>()
        {
            @Override
            public void onResponse(Call<AllTimeStatisticsModel> call, Response<AllTimeStatisticsModel> response) {
                dialog.finishedLoadingAllSeasonsStatistics(response.body());
            }

            @Override
            public void onFailure(Call<AllTimeStatisticsModel> call, Throwable t) {
                dialog.finishedLoadingAllSeasonsStatistics(null);
            }
        });
    }

    public void getCurrentSeason(String driverId, final DriverInformationActivity dialog)
    {
        Call<CurrentSeasonModel> call=serviceInstance.getCurrentSeason("/current?id=" + driverId);
        call.enqueue(new Callback<CurrentSeasonModel>()
        {
            @Override
            public void onResponse(Call<CurrentSeasonModel> call, Response<CurrentSeasonModel> response) {
                dialog.finishedLoadingCurrentSeason(response.body());
            }

            @Override
            public void onFailure(Call<CurrentSeasonModel> call, Throwable t) {
                dialog.finishedLoadingCurrentSeason(null);
            }
        });
    }
}
