package com.asiri.f1companion.UI;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Leaderboard;
import com.asiri.f1companion.R;

import org.w3c.dom.Text;

import java.util.List;

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
    private OnFragmentInteractionListener mListener;
    List<Leaderboard> leaders;
    Realm realm;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        /*
        list=(ListView)v.findViewById(R.id.Home_LeaderboardList);
        list.setNestedScrollingEnabled(true);*/
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        realm=Realm.getInstance(this.getActivity().getBaseContext());
        leaders=realm.where(Leaderboard.class).findAll();
        //leaders.add(0,new Leaderboard());
        // specify an adapter (see also next example)
        mAdapter = new LeaderBoardAdapter(getActivity(),leaders);
        mRecyclerView.setAdapter(mAdapter);
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

    }
}

class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
    List<Leaderboard> mDataset;
    Context context;

    static int HEADER_TYPE=0;
    static int LIST_TYPE=1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tPos;
        TextView tName;
        TextView tTeam;
        TextView tWins;
        TextView tPoints;
        TextView tHeader;

        public ViewHolder(View v) {
            super(v);

            tPos=(TextView)v.findViewById(R.id.tPos);
            tName=(TextView)v.findViewById(R.id.tDriver);
            tWins=(TextView)v.findViewById(R.id.tWins);
            tTeam=(TextView)v.findViewById(R.id.tTeam);
            tPoints=(TextView)v.findViewById(R.id.tPoints);
            tHeader=(TextView)v.findViewById(R.id.leaderHeader);
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
                    .inflate(R.layout.leaderboardrow, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position!=0) {
            holder.tPos.setText(mDataset.get(position-1).getPosition());
            holder.tName.setText(mDataset.get(position-1).getDriver().getGivenName() + " " + mDataset.get(position-1).getDriver().getFamilyName());
            holder.tWins.setText(mDataset.get(position-1).getWins());
            holder.tPoints.setText(mDataset.get(position-1).getPoints());

            Constructor constructor;
            constructor = Realm.getInstance(context).where(Constructor.class).equalTo("constructorId", mDataset.get(position-1).getDriver().getConstructorId()).findFirst();
            holder.tTeam.setText(constructor.getName());

            holder.tHeader.setVisibility(View.GONE);
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

