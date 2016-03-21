package com.asiri.f1companion.UI.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Leaderboard;
import com.asiri.f1companion.Models.Race;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.ExtendedDetailsService;
import com.yalantis.phoenix.PullToRefreshView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    @Bind(R.id.pull_to_refresh_leaderboard) PullToRefreshView mPullToRefreshView;
    @Bind(R.id.leader_recycler) RecyclerView mRecyclerView;
    @Bind(R.id.homeRaceName)TextView tNextRace;
    @Bind(R.id.daysToRace)TextView tDaysToRace;
    @Bind(R.id.flipper)ViewFlipper flipper;

    private OnFragmentInteractionListener mListener;
    List<Leaderboard> leaders;
    Realm realm;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Date raceDate;
    Date today;
    final Handler h=new Handler();

    Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home_layout, container, false);

        ButterKnife.bind(this,v);

        context=getActivity().getBaseContext();
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        realm=Realm.getInstance(this.getActivity().getBaseContext());
        leaders=realm.where(Leaderboard.class).findAll();
        //leaders.add(0,new Leaderboard());
        // specify an adapter (see also next example)
        mAdapter = new LeaderBoardAdapter(getActivity(),leaders);
        mRecyclerView.setAdapter(mAdapter);

        getNextRace();

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        leaders=null;
                        mPullToRefreshView.setRefreshing(false);
                        new ExtendedDetailsService(context).updateLeaderboard();
                        mAdapter.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void getNextRace()
    {
        today=new Date();

        DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        RealmResults<Race> races=realm.allObjects(Race.class);

        boolean found=false;

        for (Race r:races) {
            try {
                //today=(Date)formatter.parse("2015-05-11 12:00:00");
                raceDate = (Date) formatter.parse(r.getDate() + " " + r.getTime());

                if(raceDate.after(today))
                {
                    tNextRace.setText(r.getRaceName() + " - " + r.getCircuitName() + " in " + r.getCountry() + " on " + r.getDate());
                    found=true;
                    break;
                }
            }
            catch(ParseException ex)
            {
                ex.printStackTrace();
            }
        }

        if(found)
        {
            if(flipper.getDisplayedChild()==1)
            {
                flipper.showNext();
            }

            tDaysToRace.setText(calculateTime());

            h.postDelayed(new Runnable() {
                private long time = 0;

                @Override
                public void run() {
                    tDaysToRace.setText(calculateTime());
                    //time += 1000;
                    h.postDelayed(this, 60000);
                }
            }, 60000); // 1 second delay (takes millis)
        }
        else
        {
            if(flipper.getDisplayedChild()==0)
            {
                flipper.showNext();
            }
        }
    }

    public String calculateTime()
    {
        today.setTime(today.getTime() +60000);

        long daysLeft=(long)(raceDate.getTime()-today.getTime())/(1000*60*60*24);
        long daysLeftremainder=(long)(raceDate.getTime()-today.getTime())%(1000*60*60*24);
        long hoursLeft=(long)daysLeftremainder/3600000;
        long hoursLeftremainder=(long)daysLeftremainder%3600000;
        long minutesLeft=(long)hoursLeftremainder/60000;
        long minutesLeftremainder=(long)hoursLeftremainder%60000;

        return daysLeft + " : " + hoursLeft + " : " + minutesLeft;
    }
}

class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> implements View.OnClickListener{
    List<Leaderboard> mDataset;
    Context context;

    static int HEADER_TYPE=0;
    static int LIST_TYPE=1;

    @Override
    public void onClick(View view) {
        Toast.makeText(context,"item : " + ((ViewHolder)view.getTag()).id,Toast.LENGTH_SHORT).show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tPos)TextView tPos;
        @Bind(R.id.tDriver)TextView tName;
        @Bind(R.id.tTeam)TextView tTeam;
        @Bind(R.id.tWins)TextView tWins;
        @Bind(R.id.tPoints)TextView tPoints;
        @Bind(R.id.leaderHeader)TextView tHeader;
        String id;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setTag(this);
        }
    }

    public LeaderBoardAdapter(Context c,List<Leaderboard> myDataset) {
        mDataset = myDataset;
        this.context=c;
    }

    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_leaderboard, parent, false);

        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position!=0) {
            holder.tPos.setText(mDataset.get(position-1).getPosition());
            holder.tName.setText(mDataset.get(position-1).getDriver().getGivenName() + " " + mDataset.get(position-1).getDriver().getFamilyName());
            holder.tWins.setText(mDataset.get(position-1).getWins() + " - wins");

            holder.tPoints.setText(mDataset.get(position-1).getPoints() + " - pts");

            Constructor constructor;
            constructor = Realm.getInstance(context).where(Constructor.class).equalTo("constructorId", mDataset.get(position-1).getDriver().getConstructorId()).findFirst();
            holder.tTeam.setText(constructor.getName());

            holder.tHeader.setVisibility(View.GONE);
            holder.id=mDataset.get(position-1).getDriver().getDriverId();
        }
        else
        {
            holder.tPos.setVisibility(View.GONE);
            holder.tPoints.setVisibility(View.GONE);
            holder.tTeam.setVisibility(View.GONE);
            holder.tWins.setVisibility(View.GONE);
            holder.tName.setVisibility(View.GONE);
            holder.tHeader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        } else {
            return LIST_TYPE;

        }
    }
}

