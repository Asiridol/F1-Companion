package com.asiri.f1companion.UI.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.asiri.f1companion.Models.Race;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.ExtendedDetailsService;
import com.asiri.f1companion.Services.Models.QualifyingResultsModel;
import com.asiri.f1companion.Services.Models.RaceResultsModel;
import com.asiri.f1companion.UI.Fragments.DriversFragment;
import com.asiri.f1companion.UI.Fragments.HomeFragment;
import com.asiri.f1companion.UI.Fragments.NewsFragment;
import com.asiri.f1companion.UI.Fragments.RaceResultsInfo.QualifyingResultsFragment;
import com.asiri.f1companion.UI.Fragments.RaceResultsInfo.RaceResultsFragment;
import com.asiri.f1companion.UI.Fragments.RacesFragment;
import com.asiri.f1companion.UI.Support.Animation.DepthPageTransformer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RaceResultActivity extends AppCompatActivity{

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.raceResultsTab_layout)TabLayout tabLayout;
    @Bind(R.id.raceResultsPager)ViewPager pager;

    Realm realm;
    public RaceResultsModel raceResultsData;
    public QualifyingResultsModel qualifyingResultsData;
    public String round;

    ArrayList<Fragment> fragments=new ArrayList<Fragment>();

    CharSequence[] titles={"Race Results","Qualifying Results"};

    ExtendedDetailsService service;

    RaceResultsPagerAdapter adapter;

    public ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_result);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        round=getIntent().getStringExtra("round");
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        realm=Realm.getInstance(getBaseContext());
        getSupportActionBar().setTitle(realm.where(Race.class).equalTo("round", getIntent().getStringExtra("round")).findFirst().getRaceName() + " - Results");

        if(service==null) {
            service = new ExtendedDetailsService();
        }

        if(raceResultsData==null || qualifyingResultsData==null) {
            service.getRaceResults("current", getIntent().getStringExtra("round"), this);
            mDialog = new ProgressDialog(this);
            mDialog.setTitle("Loading...");
            mDialog.setMessage("Loading Race Results");
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void finishedLoadingRaceResults(RaceResultsModel data)
    {
        this.raceResultsData=data;
        mDialog.setMessage("Loading Qualifying Results");
        service.getQualifyingResults("current", getIntent().getStringExtra("round"), this);
    }

    public void finishedLoadingQualifyingResults(QualifyingResultsModel data)
    {
        this.qualifyingResultsData=data;
        mDialog.dismiss();

        tabLayout.setTabTextColors(Color.LTGRAY, Color.WHITE);

        if(fragments.size()==0) {
            RaceResultsFragment tab1 = RaceResultsFragment.newInstance();
            fragments.add(tab1);

            QualifyingResultsFragment tab2 = QualifyingResultsFragment.newInstance();
            fragments.add(tab2);

            adapter = new RaceResultsPagerAdapter(getSupportFragmentManager(), titles, titles.length, fragments);
            pager.setAdapter(adapter);
            pager.setPageTransformer(true, new DepthPageTransformer());
            pager.clearAnimation();
            pager.setOffscreenPageLimit(2);
            tabLayout.setupWithViewPager(pager);
        }
    }
}

class RaceResultsPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    ArrayList<Fragment> tabs;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public RaceResultsPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, ArrayList<Fragment> tabs) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.tabs=tabs;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}