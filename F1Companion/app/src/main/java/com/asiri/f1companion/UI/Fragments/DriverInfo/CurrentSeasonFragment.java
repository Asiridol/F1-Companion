package com.asiri.f1companion.UI.Fragments.DriverInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.Models.CurrentSeasonModel;
import com.asiri.f1companion.UI.Activities.DriverInformationActivity;
import com.asiri.f1companion.UI.Activities.ExtendedResultsActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CurrentSeasonFragment extends Fragment {

    CurrentSeasonModel data;
    @Bind(R.id.currentSeasonRecycler) RecyclerView recyclerView;

    private RecyclerView.LayoutManager mLayoutManager;

    public CurrentSeasonFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CurrentSeasonFragment newInstance() {
        CurrentSeasonFragment fragment = new CurrentSeasonFragment();
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

        data=((DriverInformationActivity)getActivity()).currentSeasonModel;

        if(data!=null)
        {
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(new CurrentSeasonAdapter(this.data.getRoundData(),getContext(),data.getDriverId()));
        }
        else
        {
            //getActivity().recreate();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_current_season, container, false);
        ButterKnife.bind(this,v);
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

class CurrentSeasonAdapter extends RecyclerView.Adapter<CurrentSeasonAdapter.ViewHolder> implements  View.OnClickListener{

    Context context;
    CurrentSeasonModel.RoundData[] data;
    String driverId;

    @Override
    public void onClick(View view) {
        String round=((ViewHolder)view.getTag()).round.getText().toString();
        Intent intent=new Intent(context, ExtendedResultsActivity.class);
        intent.putExtra("driverId",driverId);
        intent.putExtra("season","current");
        intent.putExtra("round",round);
        context.startActivity(intent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.currentRaceName) TextView raceName;
        @Bind(R.id.currentRaceDate) TextView raceDate;
        @Bind(R.id.currentRacePoints) TextView points;
        @Bind(R.id.currentRacePosition) TextView position;
        @Bind(R.id.currentRoundNum) TextView round;
        String driverId;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setTag(this);
        }
    }

    public CurrentSeasonAdapter(CurrentSeasonModel.RoundData[] data,Context context, String driverId) {
        this.data=data;
        this.context=context;
        this.driverId=driverId;
    }

    @Override
    public CurrentSeasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_current_status, parent, false);
        v.setOnClickListener(this);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.points.setText(data[position].getPoints());
        holder.round.setText(data[position].getRound());
        holder.position.setText(data[position].getPosition());
        holder.raceDate.setText(data[position].getDate());
        holder.raceName.setText(data[position].getTrack());
    }

    @Override
    public int getItemCount() {
        return this.data.length;
    }
}
