package com.asiri.f1companion.UI;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.InitialLoaderService;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    Toolbar toolbar;
    TabLayout tablayout;
    ViewPager pager;
    ViewPagerAdapter adapter;

    CharSequence[] titles={"Home","Home2","Home3","Home4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Fan1 Companion");

        setupUI();

        AlertDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Loading ...");
        dialog.setMessage("Loading ");
        dialog.setCancelable(false);
     //  dialog.show();

        InitialLoaderService loader=new InitialLoaderService(dialog,getBaseContext());
        //loader.loadDrivers();
    }

    public void setupUI()
    {
        pager=(ViewPager)findViewById(R.id.pager);
        tablayout=(TabLayout)findViewById(R.id.tab_layout);

        adapter=new ViewPagerAdapter(getSupportFragmentManager(),titles,titles.length);
        pager.setAdapter(adapter);
        tablayout.setupWithViewPager(pager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
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
            Fragment tab2=new HomeFragment();
            return tab2;
        }
        else if(position==2)
        {
            Fragment tab3=new HomeFragment();
            return tab3;
        }
        else
        {
            Fragment tab4=new HomeFragment();
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
