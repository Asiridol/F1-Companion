package com.asiri.f1companion.UI.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.asiri.f1companion.Commons.Defaults;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.ExtendedDetailsService;
import com.asiri.f1companion.Services.Interfaces.LoadDataListener;
import com.asiri.f1companion.Services.Models.LapTimesModel;
import com.asiri.f1companion.Services.Models.PitStopsModel;
import com.hrules.charter.CharterLine;
import com.hrules.charter.CharterXLabels;
import com.hrules.charter.CharterYLabels;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;

import org.eazegraph.lib.models.ValueLineSeries;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ExtendedResultsActivity extends AppCompatActivity implements LoadDataListener
{
    LapTimesModel.LapTime[] lapTimes;
    PitStopsModel.PitStop[] pitStops;
    private RecyclerView.LayoutManager mLayoutManager;
    String round;
    String season;
    String driverId;

    @Bind(R.id.resultsFlipper)ViewFlipper flipper;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.spinner)ProgressBar spinner;

    @Bind(R.id.charter_linePos)CharterLine lineChartPos;
    @Bind(R.id.charter_line_YMarkerPos)CharterYLabels YlabelsPos;

    @Bind(R.id.xLabels)CharterXLabels XlabelsPos;

    @Bind(R.id.charter_lineTime)CharterLine lineChartTime;
    @Bind(R.id.charter_line_YMarkerTime)CharterYLabels YlabelsTime;

    @Bind(R.id.lapTimes_recylcer)RecyclerView recyclerView;
    @Bind(R.id.pager)ViewPager pager;
    @Bind(R.id.bottomNavigation)BottomNavigationView navigationView;

    float maxPos;
    float minPos;

    float maxTime;
    float minTime;

    float[] valuesPos;
    float[] valuesTime;
    String[] labels;

    int i=0;

    ExtendedDetailsService service=new ExtendedDetailsService();

    ArrayList<Object> UIList=new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_results);
        ButterKnife.bind(this);

        BottomNavigationItem item1=new BottomNavigationItem("Time",getResources().getColor(R.color.timeItem),R.drawable.ic_action_stopwatch);
        BottomNavigationItem item2=new BottomNavigationItem("Place",getResources().getColor(R.color.posItem),R.drawable.ic_action_place);
        navigationView.addTab(item2);
        navigationView.addTab(item1);

        navigationView.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                pager.setCurrentItem(index);
            }
        });

        round=getIntent().getStringExtra("round");
        season=getIntent().getStringExtra("season");
        driverId=getIntent().getStringExtra("driverId");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Extended Results");
        loadUI();
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

    public void loadUI()
    {
        if(flipper.getDisplayedChild()!=1) {
            flipper.showNext();
        }

        service.getLapTimes(season, round, driverId, this);
    }

    public void finishLoadingUI()
    {
        if(lapTimes!=null) {
            if (flipper.getDisplayedChild() != 0) {
                flipper.showNext();
            }

            ValueLineSeries series = new ValueLineSeries();
            series.setColor(Color.GRAY);

            valuesPos = new float[lapTimes.length];
            valuesTime=new float[lapTimes.length];

            labels = new String[lapTimes.length / 3];
            minPos = 1;
            minTime=0;

            for (int i = 0; i < lapTimes.length; i++)
            {
                float pos = Float.parseFloat(lapTimes[i].getPosition());

                StringTokenizer tokenizer=new StringTokenizer(lapTimes[i].getTime(),":");

                float seconds=Float.parseFloat(tokenizer.nextToken());
                seconds*=60;
                if(seconds>80)
                {
                    seconds=0;
                }
                else {
                    seconds += Float.parseFloat(tokenizer.nextToken());
                }
                if (minTime > seconds) {
                    minTime = seconds;
                }

                if (maxTime < seconds) {
                    maxTime = seconds;
                }

                if(minPos>pos)
                {
                    minPos=pos;
                }

                if(maxPos<pos)
                {
                    maxPos=pos;
                }

                if (i % 3 == 0) {
                    if ((i / 3) < labels.length) {
                        labels[i / 3] = lapTimes[i].getLap();
                    }
                }

                valuesTime[i] = seconds;
                valuesPos[i]=pos;
            }

            String[] ylabelsPos = new String[3];
            ylabelsPos[0] = "" + Math.round(minPos);
            ylabelsPos[1] = "" + Math.round((maxPos+minPos)/2);
            ylabelsPos[2] = "" + Math.round(maxPos);

            String[] ylabelsTime = new String[3];
            ylabelsTime[0] = "" + minTime;
            ylabelsTime[1] = "" + Math.round((maxTime+minTime)/2)+ "s";
            ylabelsTime[2] = "" + Math.round(maxTime)+ "s";

            // start Pos chart
            lineChartPos.setValues(valuesPos);
            lineChartPos.setBackgroundColor(Color.TRANSPARENT);
            lineChartPos.setChartFillColor(Color.TRANSPARENT);
            lineChartPos.setIndicatorSize(2f);
            lineChartPos.setAnimInterpolator(new BounceInterpolator());
            lineChartPos.show();

            YlabelsPos.setValues(ylabelsPos);
            XlabelsPos.setValues(labels);
            recyclerView.setHasFixedSize(true);

            // start Time chart
            lineChartTime.setValues(valuesTime);
            lineChartTime.setBackgroundColor(Color.TRANSPARENT);
            lineChartTime.setChartFillColor(Color.TRANSPARENT);
            lineChartTime.setIndicatorSize(2f);
            lineChartTime.setAnimInterpolator(new BounceInterpolator());
            lineChartTime.show();

            YlabelsTime.setValues(ylabelsTime);
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(new LapTimesAdapter(UIList, getBaseContext()));

            pager.setAdapter(new ChartPagerAdapter(this));

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
                    lineChartPos.replayAnim();
                    lineChartTime.replayAnim();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
        else
        {
            this.finish();
        }
    }

    @Override
    public void finishedLoadingData() {

    }

    @Override
    public void finishedLoadingDataWith(Object object, Defaults.RequestType requestType) {
        if(requestType==Defaults.RequestType.PitStops)
        {
            PitStopsModel.PitStop[] pitStops=(PitStopsModel.PitStop[])object;
            if(pitStops==null)
            {
                Toast.makeText(this, "Error loading Pit stops", Toast.LENGTH_LONG).show();
                this.finish();
            }
            else {
                this.pitStops = pitStops;
                UIList.add("Pit Stops");

                for (PitStopsModel.PitStop pitStop:pitStops){
                    UIList.add(pitStop);
                }

                finishLoadingUI();
            }
        }
        else if(requestType==Defaults.RequestType.LapTimes)
        {
            LapTimesModel.LapTime[] lapTimes=(LapTimesModel.LapTime[])object;
            if(lapTimes==null)
            {
                Toast.makeText(this, "Error loading Lap times", Toast.LENGTH_LONG).show();
                this.finish();
            }
            else
            {
                this.lapTimes = lapTimes;

                UIList.add("Lap Times");
                for (LapTimesModel.LapTime lapTime:lapTimes){
                    UIList.add(lapTime);
                }

                service.getPitStops(season, round, driverId, this);
            }

        }
    }

    @Override
    public void finishedLoadingDataWithError(String error) {

    }
}

class LapTimesAdapter extends RecyclerView.Adapter<LapTimesAdapter.ViewHolder>{

    ArrayList<Object> UIList;
    Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lapNumber) TextView lapNumber;
        @Bind(R.id.lapTime) TextView lapTime;
        @Bind(R.id.lapPosition) TextView lapPosition;
        @Bind(R.id.pitDuration) TextView pitDuration;
        @Bind(R.id.pitLap) TextView pitLap;
        @Bind(R.id.pitNumber) TextView pitNumber;
        @Bind(R.id.LapTimeRow) RelativeLayout lapTimeRow;
        @Bind(R.id.PitStopRow) RelativeLayout pitStopRow;
        @Bind(R.id.heading)TextView header;
        @Bind(R.id.flipperLapTimes) ViewFlipper flipper;
        @Bind(R.id.parentView)CardView parent;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setTag(this);
        }
    }

    public LapTimesAdapter(ArrayList<Object> UIList,Context context) {
        this.UIList = UIList;
        this.context=context;
    }

    @Override
    public LapTimesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_laptime, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(UIList.get(position).getClass()==String.class)
        {
            holder.header.setText((String)UIList.get(position));
            holder.parent.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

            if(holder.flipper.getDisplayedChild()!=1)
            {
                holder.flipper.showNext();
            }
        }else
        {
            holder.parent.setBackgroundColor(Color.WHITE);
            if(UIList.get(position).getClass()== LapTimesModel.LapTime.class)
            {
                holder.lapTimeRow.setVisibility(View.VISIBLE);
                holder.pitStopRow.setVisibility(View.GONE);

                holder.lapNumber.setText(((LapTimesModel.LapTime) UIList.get(position)).getLap());
                holder.lapPosition.setText("Position : " + ((LapTimesModel.LapTime)UIList.get(position)).getPosition());
                holder.lapTime.setText(((LapTimesModel.LapTime)UIList.get(position)).getTime());

                if(holder.flipper.getDisplayedChild()!=0)
                {
                    holder.flipper.showNext();
                }
            }
            else
            {
                holder.lapTimeRow.setVisibility(View.GONE);
                holder.pitStopRow.setVisibility(View.VISIBLE);

                holder.pitNumber.setText(((PitStopsModel.PitStop) UIList.get(position)).getStop());
                holder.pitLap.setText("During Lap " + ((PitStopsModel.PitStop)UIList.get(position)).getLap());
                holder.pitDuration.setText("Duration " + ((PitStopsModel.PitStop) UIList.get(position)).getDuration());

                if(holder.flipper.getDisplayedChild()!=0)
                {
                    holder.flipper.showNext();
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return UIList.size();
    }
}

class ChartPagerAdapter extends PagerAdapter
{

    Activity act;

    public ChartPagerAdapter(Activity act)
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
            return "Position against lap";
        }
        else
        {
            return "Time against lap";
        }
    }
}
