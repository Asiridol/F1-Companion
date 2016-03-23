package com.asiri.f1companion.UI.Fragments.RaceResultsInfo;


import android.content.Context;
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
import com.asiri.f1companion.Services.Models.QualifyingResultsModel;
import com.asiri.f1companion.Services.Models.RaceResultsModel;
import com.asiri.f1companion.UI.Activities.RaceResultActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QualifyingResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QualifyingResultsFragment extends Fragment {

    QualifyingResultsModel qualifyingData;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind(R.id.qualifying_recycler)RecyclerView recyclerView;

    Bundle savedInstance;

    public QualifyingResultsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QualifyingResultsFragment newInstance() {
        QualifyingResultsFragment fragment = new QualifyingResultsFragment();
        return fragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        qualifyingData=((RaceResultActivity)getActivity()).qualifyingResultsData;

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        if(qualifyingData!=null){
            recyclerView.setAdapter(new QualifyingResultAdapter(getContext(), qualifyingData.getQualifyingResults()));
        }
        else
        {
            ((RaceResultActivity)getActivity()).recreate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_qualifying_results, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}

class QualifyingResultAdapter extends RecyclerView.Adapter<QualifyingResultAdapter.ViewHolder>{

    QualifyingResultsModel.QualifyingResult[] mDataset;
    Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.qualifyingDriver)TextView tDriver;
        @Bind(R.id.qualifyingPosition) TextView tPosition;
        @Bind(R.id.qualifyingTeam) TextView tTeam;
        @Bind(R.id.q1) TextView tQ1;
        @Bind(R.id.q2) TextView tQ2;
        @Bind(R.id.q3) TextView tQ3;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setTag(this);
        }
    }

    public QualifyingResultAdapter(Context c,QualifyingResultsModel.QualifyingResult[] mDataset) {
        this.mDataset = mDataset;
        this.context=c;
    }

    @Override
    public QualifyingResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_qualifying_result, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Realm realm= Realm.getInstance(context);
        Driver driver=realm.where(Driver.class).equalTo("driverId",mDataset[position].getDriverId()).findFirst();
        Constructor constructor=realm.where(Constructor.class).equalTo("constructorId",driver.getConstructorId()).findFirst();

        holder.tDriver.setText(driver.getGivenName() + " " + driver.getFamilyName());
        holder.tTeam.setText(constructor.getName());
        holder.tPosition.setText(position+1 + "");
        holder.tQ1.setText("Q1 " + mDataset[position].getQ1()+"s");
        holder.tQ2.setText("Q2 " + mDataset[position].getQ2()+"s");
        holder.tQ3.setText("Q3" + mDataset[position].getQ3()+"s");

        if(mDataset[position].getQ2()==null)
        {
            holder.tQ2.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.tQ2.setVisibility(View.VISIBLE);
        }

        if(mDataset[position].getQ3()==null)
        {
            holder.tQ3.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.tQ3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
