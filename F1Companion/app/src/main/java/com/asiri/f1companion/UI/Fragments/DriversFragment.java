package com.asiri.f1companion.UI.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.DriverListService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriversFragment extends Fragment{

    @Bind(R.id.driversList) ListView list;

    private OnFragmentInteractionListener mListener;
    ArrayList<DriverListService.RowModel> data;
    Realm realm;

    public DriversFragment() {
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
        View v=inflater.inflate(R.layout.fragment_drivers_layout, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        realm=Realm.getInstance(this.getActivity().getBaseContext());
        data=new DriverListService().getTableUIList(this.getActivity().getBaseContext());
        list.setAdapter(new DriverListAdapter(getActivity(),data));
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
}

class DriverListAdapter extends ArrayAdapter implements View.OnClickListener
{
    ArrayList<DriverListService.RowModel> data;

    LayoutInflater inflater;
    Context context;

    public DriverListAdapter(Context context,ArrayList<DriverListService.RowModel> data) {
        super(context, R.layout.row_driver);
        this.data=data;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final ViewHolder holder;
        if(view==null)
        {
            view=inflater.inflate(R.layout.row_driver,viewGroup,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)view.getTag();
        }

        if(data.get(i).getRowType()==data.get(i).HEADER)
        {
            holder.tTeam.setText(data.get(i).getData().get("Title") + " - " + data.get(i).getData().get("Nationality"));
            if(holder.flipper.getDisplayedChild()==0)
            {
                holder.flipper.showNext();
            }
            view.setClickable(false);
        }
        else
        {
            view.setOnClickListener(this);
            holder.tDriver.setText((data.get(i).getData().get("Name")));
            holder.tNumber.setText((data.get(i).getData().get("Num")));
            holder.tCode.setText((data.get(i).getData().get("Code")));
            holder.driverId=data.get(i).getData().get("Id");

            if(holder.flipper.getDisplayedChild()==1)
            {
                holder.flipper.showNext();
            }
        }

        return view;
    }

    @Override
        public void onClick(View view) {
            ViewHolder holder;
            holder=(ViewHolder)view.getTag();

            Toast.makeText(context,"Driver Id : " + holder.driverId,Toast.LENGTH_SHORT).show();
        }

    static class ViewHolder
    {
        @Bind(R.id.tDriver)TextView tDriver;
        @Bind(R.id.tCode)TextView tCode;
        @Bind(R.id.tNumber)TextView tNumber;
        @Bind(R.id.tTeam)TextView tTeam;
        @Bind(R.id.flipperDriver)ViewFlipper flipper;
        String driverId;

        ViewHolder(View view)
        {
            ButterKnife.bind(this,view);
        }
    }
}

