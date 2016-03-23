package com.asiri.f1companion.UI.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.asiri.f1companion.R;
import com.asiri.f1companion.UI.Support.Animation.DepthPageTransformer;
import com.asiri.f1companion.UI.Fragments.DriversFragment;
import com.asiri.f1companion.UI.Fragments.HomeFragment;
import com.asiri.f1companion.UI.Fragments.NewsFragment;
import com.asiri.f1companion.UI.Fragments.RacesFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, DriversFragment.OnFragmentInteractionListener, NewsFragment.OnFragmentInteractionListener,RacesFragment.OnFragmentInteractionListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab_layout)TabLayout tablayout;
    @Bind(R.id.pager)ViewPager pager;
    HomePagerAdapter adapter;

    CharSequence[] titles={"Home","Drivers","News","Races"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupUI();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void setupUI() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Fan1 Companion");
        toolbar.setTitleTextColor(Color.WHITE);
        tablayout.setTabTextColors(Color.LTGRAY,Color.WHITE);

        AlertDialog dialog=new ProgressDialog(this);

        adapter=new HomePagerAdapter(getSupportFragmentManager(),titles,titles.length);
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.clearAnimation();
        pager.setOffscreenPageLimit(4);
        tablayout.setupWithViewPager(pager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

class HomePagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public HomePagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Fragment tab1=new HomeFragment();

            return tab1;
        }
        else if(position==1)
        {
            Fragment tab2= new DriversFragment();
            return tab2;
        }
        else if(position==2)
        {
            Fragment tab3=new NewsFragment();
            return tab3;
        }
        else
        {
            Fragment tab4=new RacesFragment();
            return tab4;
        }
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
