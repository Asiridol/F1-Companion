package com.asiri.f1companion.Services;

import android.content.Context;
import android.app.AlertDialog;

import com.asiri.f1companion.Commons.Defaults;
import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.Models.Race;
import com.asiri.f1companion.Services.Interfaces.Fan1ServerInterface;
import com.asiri.f1companion.Services.Models.ConstructorsModel;
import com.asiri.f1companion.Services.Models.DriversModel;
import com.asiri.f1companion.Services.Models.RacesModel;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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
public class InitialLoaderService {

    final OkHttpClient client=new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60,TimeUnit.SECONDS)
            .build();

    final Retrofit adapter = new Retrofit.Builder()
            .baseUrl(Defaults.BASEURL_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    final Fan1ServerInterface serviceInstance;

    final Realm realm;

    final AlertDialog dialog;
    final Context context;

    public InitialLoaderService(AlertDialog dialog,Context c)
    {
        serviceInstance = adapter.create(Fan1ServerInterface.class);
        realm=Realm.getInstance(c);
        this.dialog=dialog;
        this.context=c;
    }

    public void clearRealm()
    {
        realm.beginTransaction();
        realm.clear(Driver.class);
        realm.clear(Constructor.class);
        realm.clear(Race.class);
        realm.commitTransaction();
    }

    public void loadDrivers(){

        dialog.setMessage("Loading Drivers");
        clearRealm();

        Call<DriversModel> call=serviceInstance.getAllDriversAsync();
        call.enqueue(new Callback<DriversModel>()
        {

            @Override
            public void onResponse(Call<DriversModel> call, Response<DriversModel> response) {

                realm.beginTransaction();

                for(int i=0;i<response.body().getDrivers().length ; i++)
                {
                    Driver driver=realm.createObject(Driver.class);
                    driver.setObject(response.body().getDrivers()[i]);
                }

                realm.commitTransaction();

                checkRealm();
                loadConstructors();
            }

            public void checkRealm()
            {
                RealmResults<Driver> drivers=realm.where(Driver.class).findAll();
                System.out.println("Drivers # : " + drivers.size());
                for (Driver d:drivers)
                {
                 System.out.println(d.toString());
                }
            }

            @Override
            public void onFailure(Call<DriversModel> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    public void loadConstructors()
    {
        dialog.setMessage("Loading Constructors");

        Call<ConstructorsModel> call=serviceInstance.getAllConstructorsAsync();
        call.enqueue(new Callback<ConstructorsModel>()
        {

            @Override
            public void onResponse(Call<ConstructorsModel> call, Response<ConstructorsModel> response) {

                realm.beginTransaction();

                for(int i=0;i<response.body().getConstructors().length ; i++)
                {
                    Constructor constructor=realm.createObject(Constructor.class);
                    constructor.setObject(response.body().getConstructors()[i]);
                    RealmResults<Driver> drivers=realm.where(Driver.class).equalTo("constructorId",constructor.getConstructorId()).findAll();
                    for (Driver d:drivers) {
                        constructor.getDrivers().add(d);
                    }
                }

                realm.commitTransaction();

                checkRealm();

                loadRaces();
            }

            public void checkRealm()
            {
                RealmResults<Constructor> constructors=realm.where(Constructor.class).findAll();
                System.out.println("Constructors # : " + constructors.size());
                for (Constructor c:constructors)
                {
                    System.out.println(c);
                }
            }

            @Override
            public void onFailure(Call<ConstructorsModel> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    public void loadRaces()
    {
        dialog.setMessage("Loading Race Schedule");

        Call<RacesModel> call=serviceInstance.getAllRacesScheduledAsync();
        call.enqueue(new Callback<RacesModel>()
        {

            @Override
            public void onResponse(Call<RacesModel> call, Response<RacesModel> response) {
                dialog.dismiss();

                realm.beginTransaction();

                for(int i=0;i<response.body().getRaces().length ; i++)
                {
                    Race race=realm.createObject(Race.class);
                    race.setObject(response.body().getRaces()[i]);
                }

                realm.commitTransaction();

                checkRealm();

                ExtendedDetailsService ex=new ExtendedDetailsService(dialog,context);
                ex.loadLeaderboard();
            }

            public void checkRealm()
            {
                RealmResults<Race> races=realm.where(Race.class).findAll();
                System.out.println("Races # : " + races.size());
                for (Race c:races)
                {
                    System.out.println(c);
                }
            }

            @Override
            public void onFailure(Call<RacesModel> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}
