package com.asiri.f1companion.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.asiri.f1companion.Commons.Defaults;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.InitialLoaderService;
import com.asiri.f1companion.Services.Interfaces.LoadDataListener;

import io.realm.Realm;

/**
 * Created by asiri on 3/21/2016.
 */
public class SplashActivity extends Activity implements LoadDataListener{

    TextView tMessage;
    public ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadUI();
    }

    public void loadUI()
    {
        Realm realm= Realm.getInstance(this.getBaseContext());
        if(realm.isEmpty()) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("Loading...");
            mDialog.setCancelable(false);
            mDialog.show();
            InitialLoaderService loader = new InitialLoaderService(this);
            loader.loadDrivers();
        }else
        {
            this.finishedLoadingData();
        }
    }

    @Override
    public void finishedLoadingData() {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void finishedLoadingDataWith(Object object, Defaults.RequestType requestType) {

    }

    @Override
    public void finishedLoadingDataWithError(String error) {

    }
}
