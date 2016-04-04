package com.asiri.f1companion.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Leaderboard;
import com.asiri.f1companion.Models.Race;
import com.asiri.f1companion.R;
import com.asiri.f1companion.UI.Activities.DriverInformationActivity;
import com.asiri.f1companion.UI.Activities.RaceResultActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RacesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RacesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Realm realm;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.race_recylcer)RecyclerView raceRecycler;

    public RacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RacesFragment newInstance(String param1, String param2) {
        RacesFragment fragment = new RacesFragment();

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
        View v = inflater.inflate(R.layout.fragment_races, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        raceRecycler.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        raceRecycler.setLayoutManager(mLayoutManager);
        realm=Realm.getInstance(getContext());
        raceRecycler.setAdapter(new RaceListAdapter(getContext(),realm.where(Race.class).findAll()));
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

class RaceListAdapter extends RecyclerView.Adapter<RaceListAdapter.ViewHolder> implements View.OnClickListener{
    List<Race> mDataset;
    Context context;
    Date today=new Date();
    DateFormat formatter;
    Date raceDate;

    @Override
    public void onClick(View view) {
        if(((ViewHolder)view.getTag()).finished)
        {
            Intent raceIntent=new Intent(context, RaceResultActivity.class);
            raceIntent.putExtra("round",((ViewHolder)view.getTag()).round);
            context.startActivity(raceIntent);
        }
        else
        {
            Snackbar.make(view,((ViewHolder)view.getTag()).raceName.getText() + " hasn't finished yet",Snackbar.LENGTH_LONG).show();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.raceDate) TextView raceDate;
        @Bind(R.id.raceName) TextView raceName;
        @Bind(R.id.roundNum) TextView raceRound;
        @Bind(R.id.parentView)CardView parent;
        String round;
        boolean finished;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setTag(this);
        }
    }

    public RaceListAdapter(Context c,List<Race> myDataset) {
        mDataset = myDataset;
        this.context=c;
        formatter =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public RaceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_race, parent, false);

        ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.raceRound.setText(String.format("%2s",mDataset.get(position).getRound()).replace(" ","0"));
        holder.raceDate.setText(mDataset.get(position).getDate() + " - " + mDataset.get(position).getTime());
        holder.raceName.setText(mDataset.get(position).getRaceName());
        holder.round=mDataset.get(position).getRound();

        try {
            raceDate=formatter.parse(mDataset.get(position).getDate() + " " + mDataset.get(position).getTime());
            Calendar cal=Calendar.getInstance();
            cal.setTime(raceDate);
            cal.add(Calendar.HOUR,6);
            raceDate=cal.getTime();
            if(today.after(raceDate))
            {
                holder.parent.setCardBackgroundColor(Color.LTGRAY);
                holder.finished=true;
            }
            else
            {
                holder.parent.setCardBackgroundColor(Color.WHITE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
