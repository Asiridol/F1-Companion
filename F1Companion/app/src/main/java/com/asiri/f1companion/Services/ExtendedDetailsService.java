package com.asiri.f1companion.Services;

import android.app.AlertDialog;
import android.content.Context;

import com.asiri.f1companion.Commons.Defaults;
import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.Models.Leaderboard;
import com.asiri.f1companion.Services.Interfaces.Fan1ServerInterface;
import com.asiri.f1companion.Services.Models.ConstructorsModel;
import com.asiri.f1companion.Services.Models.LeaderboardsModel;
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

    final Realm realm;

    final SplashActivity activity;

    public ExtendedDetailsService(SplashActivity activity)
    {
        serviceInstance=adapter.create(Fan1ServerInterface.class);
        this.activity=activity;
        realm=Realm.getInstance(activity.getBaseContext());
    }

    public ExtendedDetailsService(Context c)
    {
        this.realm=Realm.getInstance(c);

        activity=null;
        serviceInstance=adapter.create(Fan1ServerInterface.class);
    }

    public void getCurrentSeason(){}

    public void getAllStatuses(){}

    public void loadLeaderboard()
    {
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

    public void updateLeaderboard()
    {
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
}
