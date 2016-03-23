package com.asiri.f1companion.UI.Fragments.RaceResultsInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.Models.RaceResultsModel;
import com.asiri.f1companion.UI.Activities.DriverInformationActivity;
import com.asiri.f1companion.UI.Activities.ExtendedResultsActivity;
import com.asiri.f1companion.UI.Activities.RaceResultActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RaceResultsFragment extends Fragment {

    @Bind(R.id.raceResult_recycler)RecyclerView recyclerView;
    RaceResultsModel resultsData;
    private RecyclerView.LayoutManager mLayoutManager;
    String round;

    public RaceResultsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RaceResultsFragment newInstance() {
        RaceResultsFragment fragment = new RaceResultsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_race_results, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.resultsData=((RaceResultActivity)getActivity()).raceResultsData;
        this.round=((RaceResultActivity)getActivity()).round;
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        if(resultsData!=null)
            recyclerView.setAdapter(new RaceResultAdapter(getActivity(), resultsData.getRaceResult(),round));
    }
}

class RaceResultAdapter extends RecyclerView.Adapter<RaceResultAdapter.ViewHolder> implements View.OnClickListener{

    RaceResultsModel.RaceResult[] mDataset;
    Activity context;
    String round;

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(context, ExtendedResultsActivity.class);
        intent.putExtra("driverId", ((ViewHolder) view.getTag()).driverId);
        intent.putExtra("season","current");
        intent.putExtra("round",round);
        context.startActivity(intent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.raceResultDriver) TextView tDriver;
        @Bind(R.id.raceResultPoints) TextView tPoints;
        @Bind(R.id.raceResultPosition) TextView tPosition;
        @Bind(R.id.raceResultGrid) TextView tGrid;
        @Bind(R.id.raceResultStatus)TextView tStatus;
        @Bind(R.id.raceResultTeam) TextView tTeam;
        @Bind(R.id.raceResultTime) TextView tTime;

        String driverId;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setTag(this);
        }
    }

    public RaceResultAdapter(Activity c,RaceResultsModel.RaceResult[] mDataset,String round) {
        this.mDataset = mDataset;
        this.context=c;
        this.round=round;
    }

    @Override
    public RaceResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_race_result, parent, false);

        ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Realm realm= Realm.getInstance(context);
        Driver driver=realm.where(Driver.class).equalTo("driverId",mDataset[position].getDriverId()).findFirst();
        Constructor constructor=realm.where(Constructor.class).equalTo("constructorId",driver.getConstructorId()).findFirst();

        holder.tDriver.setText(driver.getGivenName() + " " + driver.getFamilyName());
        holder.tGrid.setText("Grid " + mDataset[position].getGrid());
        holder.tPoints.setText("Points " + mDataset[position].getPoints());
        holder.tPosition.setText(mDataset[position].getPosition());
        holder.tTeam.setText(constructor.getName());
        holder.tStatus.setText(mDataset[position].getStatus());
        holder.driverId=mDataset[position].getDriverId();
        if(mDataset[position].getTime().equals("0"))
        {
            holder.tTime.setVisibility(View.INVISIBLE);
        }
        else {
            holder.tTime.setVisibility(View.VISIBLE);
            holder.tTime.setText(mDataset[position].getTime() + "s");
        }

        if(mDataset[position].getStatus().equalsIgnoreCase("finished") || mDataset[position].getStatus().contains("+"))
        {
            holder.tStatus.setTextColor(Color.BLACK);
        }
        else
        {
            holder.tStatus.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
