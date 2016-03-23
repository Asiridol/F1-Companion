package com.asiri.f1companion.UI.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.ExtendedDetailsService;
import com.asiri.f1companion.Services.Models.LapTimesModel;
import com.hrules.charter.CharterLine;
import com.hrules.charter.CharterXLabels;
import com.hrules.charter.CharterYLabels;
import org.eazegraph.lib.models.ValueLineSeries;
import java.util.StringTokenizer;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ExtendedResultsActivity extends AppCompatActivity
{
    LapTimesModel.LapTime[] lapTimes;
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

    float maxPos;
    float minPos;

    float maxTime;
    float minTime;

    float[] valuesPos;
    float[] valuesTime;
    String[] labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_results);
        ButterKnife.bind(this);

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
        ExtendedDetailsService service=new ExtendedDetailsService();
        service.getLapTimes(season,round,driverId,this);
    }

    public void finishedLoading(LapTimesModel.LapTime[] lapTimes)
    {
        if(lapTimes!=null) {
            if (flipper.getDisplayedChild() != 0) {
                flipper.showNext();
            }

            this.lapTimes = lapTimes;

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

            String[] ylabelsPos = new String[2];
            ylabelsPos[0] = "" + Math.round(minPos);
            ylabelsPos[1] = "" + Math.round(maxPos);

            String[] ylabelsTime = new String[2];
            ylabelsTime[0] = "" + minTime;
            ylabelsTime[1] = "" + Math.round(maxTime)+ "s";

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
            recyclerView.setAdapter(new LapTimesAdapter(lapTimes));

            pager.setAdapter(new ChartPagerAdapter(this));

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
}

class LapTimesAdapter extends RecyclerView.Adapter<LapTimesAdapter.ViewHolder>{

    LapTimesModel.LapTime[] mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lapNumber) TextView lapNumber;
        @Bind(R.id.lapTime) TextView lapTime;
        @Bind(R.id.lapPosition) TextView lapPosition;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setTag(this);
        }
    }

    public LapTimesAdapter(LapTimesModel.LapTime[] mDataset) {
        this.mDataset = mDataset;
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
        holder.lapNumber.setText(mDataset[position].getLap());
        holder.lapPosition.setText("Position : " + mDataset[position].getPosition());
        holder.lapTime.setText(mDataset[position].getTime());
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
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
}
