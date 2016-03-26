package com.asiri.f1companion.UI.Fragments.DriverInfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.Models.AllTimeStatisticsModel;
import com.asiri.f1companion.Services.Models.LapTimesModel;
import com.asiri.f1companion.Services.Models.PitStopsModel;
import com.asiri.f1companion.UI.Activities.DriverInformationActivity;
import com.asiri.f1companion.UI.Activities.RaceResultActivity;
import com.hrules.charter.CharterLine;
import com.hrules.charter.CharterXLabels;
import com.hrules.charter.CharterYLabels;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AllSeasonsFragment extends Fragment {

    AllTimeStatisticsModel data;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.allSeasonsPager)ViewPager pager;
    @Bind(R.id.allSeasonsRecycler)RecyclerView recyclerView;
    @Bind(R.id.bottomNavigation)BottomNavigationView navigationView;

    @Bind(R.id.charter_lineAllSeasons)CharterLine lineChart;
    @Bind(R.id.charter_line_YMarkerAllSeasons)CharterYLabels Ylabels;
    @Bind(R.id.xLabelsAllSeasons)CharterXLabels Xlabels;

    public AllSeasonsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AllSeasonsFragment newInstance() {
        AllSeasonsFragment fragment = new AllSeasonsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        data=((DriverInformationActivity)getActivity()).allTimeStatisticsModel;

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        if(data!=null){
            recyclerView.setAdapter(new AllSeasonsAdapter(data.getSeasons(),getContext() ));
        }
        else
        {
            ((DriverInformationActivity)getActivity()).recreate();
        }

        pager.setAdapter(new AllSeasonsPagerAdapter(getActivity()));

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pager.setCurrentItem(pager.getCurrentItem());
                return true;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                lineChart.replayAnim();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                pager.setCurrentItem(index);
            }
        });

        String[] xLabels=new String[data.getSeasons().length];
        String[] yLabels=new String[3];
        float[] values=new float[data.getSeasons().length];

        float min=Float.parseFloat(data.getSeasons()[0].getPosition());
        float max=Float.parseFloat(data.getSeasons()[0].getPosition());

        for (int i=0;i<data.getSeasons().length;i++)
        {
            values[i]=Float.parseFloat(data.getSeasons()[i].getPosition());

            if(min>values[i])
            {
                min=values[i];
            }

            if(max< values[i])
            {
                max=values[i];
            }

            xLabels[i]=data.getSeasons()[i].getSeason();
        }

        yLabels[0]=""+Math.round(min);
        yLabels[1]=""+(Math.round(min+max)/2);
        yLabels[2]=""+Math.round(max);

        Xlabels.setValues(xLabels);
        Ylabels.setValues(yLabels);

        lineChart.setValues(values);
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        lineChart.setChartFillColor(Color.TRANSPARENT);
        lineChart.setIndicatorSize(10f);
        lineChart.setIndicatorColor(Color.BLUE);
        lineChart.setAnimInterpolator(new BounceInterpolator());
        lineChart.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_all_seasons, container, false);
        ButterKnife.bind(this, v);

        navigationView.addTab(new BottomNavigationItem("List", getResources().getColor(R.color.posItem), R.drawable.ic_action_list));
        navigationView.addTab(new BottomNavigationItem("Graph", getResources().getColor(R.color.timeItem), R.drawable.ic_action_graph));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

class AllSeasonsAdapter extends RecyclerView.Adapter<AllSeasonsAdapter.ViewHolder>{

    Context context;
    AllTimeStatisticsModel.Season[] data;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.season)TextView season;
        @Bind(R.id.constructor)TextView constructor;
        @Bind(R.id.wins) TextView wins;
        @Bind(R.id.points)TextView points;
        @Bind(R.id.position)TextView position;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setTag(this);
        }
    }

    public AllSeasonsAdapter(AllTimeStatisticsModel.Season[] seasons,Context context) {
        this.data=seasons;
        this.context=context;
    }

    @Override
    public AllSeasonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_all_seasons, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.season.setText(data[position].getSeason());
        holder.position.setText("Pos " + data[position].getPosition());
        holder.points.setText("Points " + data[position].getPoints());
        holder.constructor.setText(data[position].getConstructor());
        holder.wins.setText("Wins " + data[position].getPoints());
    }

    @Override
    public int getItemCount() {
        return this.data.length;
    }
}

class AllSeasonsPagerAdapter extends PagerAdapter
{
    Activity act;

    public AllSeasonsPagerAdapter(Activity act)
    {
        this.act=act;
    }

    public Object instantiateItem(ViewGroup collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.pageOne;
                break;
            case 1:
                resId = R.id.pageTwo;
                break;
        }
        return act.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public CharSequence getPageTitle (int position)
    {
        if(position==0)
        {
            return "Graph";
        }
        else
        {
            return "Data List";
        }
    }
}
