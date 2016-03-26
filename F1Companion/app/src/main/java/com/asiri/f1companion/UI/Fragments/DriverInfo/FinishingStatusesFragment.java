package com.asiri.f1companion.UI.Fragments.DriverInfo;

import android.content.Context;
import android.graphics.Color;
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
import com.asiri.f1companion.Services.Models.AllStatusesModel;
import com.asiri.f1companion.Services.Models.AllTimeStatisticsModel;
import com.asiri.f1companion.UI.Activities.DriverInformationActivity;

import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FinishingStatusesFragment extends Fragment {

    AllStatusesModel data;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.statusesRecycler)RecyclerView recyclerView;

    ArrayList<Object> UIList=new ArrayList<Object>();

    public FinishingStatusesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FinishingStatusesFragment newInstance() {
        FinishingStatusesFragment fragment = new FinishingStatusesFragment();
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
        View v=inflater.inflate(R.layout.fragment_finishing_statuses, container, false);
        ButterKnife.bind(this,v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.data = ((DriverInformationActivity) getActivity()).statusesModel;
        if(this.data!=null) {
            UIList.add("Finished");

            HashMap<String, Integer>[] inner = data.getFinished();
            Iterator it = inner[0].entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry<String, Integer> pair = (HashMap.Entry<String, Integer>) it.next();
                UIList.add(new KeyValuePair(pair.getKey(), pair.getValue()));
            }

            UIList.add("Car Faults");
            inner = data.getCarFaults();
            it = inner[0].entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry<String, Integer> pair = (HashMap.Entry<String, Integer>) it.next();
                UIList.add(new KeyValuePair(pair.getKey(), pair.getValue()));
            }

            UIList.add("Driver Faults");
            inner = data.getDriverFaults();
            it = inner[0].entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry<String, Integer> pair = (HashMap.Entry<String, Integer>) it.next();
                UIList.add(new KeyValuePair(pair.getKey(), pair.getValue()));
            }

            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(new StatusesAdapter(UIList, getContext()));
        }
        else
        {
            getActivity().recreate();
        }
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

class KeyValuePair
{
    public String key;
    public int value;

    public KeyValuePair(String key, int value)
    {
        this.key=key;
        this.value=value;
    }
}

class StatusesAdapter extends RecyclerView.Adapter<StatusesAdapter.ViewHolder>{

    Context context;
    ArrayList<Object> data;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.statusFlipper)ViewFlipper flipper;
        @Bind(R.id.statusCount)TextView count;
        @Bind(R.id.status)TextView status;
        @Bind(R.id.statusHeader)TextView header;
        @Bind(R.id.parentView)CardView parentView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setTag(this);
        }
    }

    public StatusesAdapter(ArrayList<Object> data,Context context) {
        this.data=data;
        this.context=context;
        System.out.println("Size : " + data.size());
    }

    @Override
    public StatusesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_status, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println(data.get(position).getClass());
        if(data.get(position).getClass()==String.class)
        {
            holder.header.setText(data.get(position).toString());

            if(holder.flipper.getDisplayedChild()!=0)
            {
                holder.flipper.showNext();
            }

            holder.parentView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            KeyValuePair pair=(KeyValuePair)data.get(position);
            holder.status.setText(pair.key);
            holder.count.setText("" + pair.value);
            holder.parentView.setBackgroundColor(Color.WHITE);

            if(holder.flipper.getDisplayedChild()!=1)
            {
                holder.flipper.showNext();
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}
