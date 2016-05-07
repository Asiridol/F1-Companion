package com.asiri.f1companion.UI.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.asiri.f1companion.Commons.Defaults;
import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Leaderboard;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.DriverListService;
import com.yalantis.phoenix.PullToRefreshView;

import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    @Bind(R.id.newsList)ListView list;
    @Bind(R.id.pull_to_refresh)PullToRefreshView mPullToRefreshView;

    private OnFragmentInteractionListener mListener;
    AlertDialog dialog;
    List<RSSItem> items;
    SimpleRss2Parser parser;
    NewsListAdapter mAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        dialog=new ProgressDialog(getActivity());
        dialog.setTitle("Loading ...");
        dialog.setMessage("Loading ");
        dialog.setCancelable(false);
        dialog.show();
        getNews();

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        items = null;
                        mPullToRefreshView.setRefreshing(false);
                        parser.parseAsync();
                    }
                }, 800);
            }
        });
    }

    public void getNews()
    {
        parser=new SimpleRss2Parser(Defaults.URL_NEWS,
                new SimpleRss2ParserCallback() {
                    @Override
                    public void onFeedParsed(List<RSSItem> feed) {
                        dialog.dismiss();
                        items=feed;
                        mAdapter=new NewsListAdapter(getContext(),items);
                        list.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Exception ex) {
                        dialog.dismiss();
                        ex.printStackTrace();
                    }
                });
        parser.parseAsync();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

class NewsListAdapter extends ArrayAdapter implements View.OnClickListener
{
    List<RSSItem> items;

    LayoutInflater inflater;
    Context context;

    DateFormat formatter=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzzz");
    DateFormat output=new SimpleDateFormat("dd/MM\nEEE");

    public NewsListAdapter(Context context,List<RSSItem> data) {
        super(context, R.layout.row_news);
        this.items=data;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
        this.formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final ViewHolder holder;

        if(view==null)
        {
            view=inflater.inflate(R.layout.row_news,viewGroup,false);
            holder=new ViewHolder(view);
            holder.i=i;
            view.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)view.getTag();
        }

        view.setOnClickListener(this);
        Date postDate=null;
        try {
            postDate = (Date) formatter.parse(items.get(i).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tDate.setText(output.format(postDate));

        holder.tTitle.setText(items.get(i).getTitle());
        String text= new StringTokenizer(items.get(i).getDescription(),"\n").nextToken();
        holder.tDesc.setWebViewClient(new WebViewClient());
        holder.tDesc.loadDataWithBaseURL(null,(Html.fromHtml(text)).toString(), "text/html", "UTF-8", null);
        holder.tDesc.setBackgroundColor(Color.TRANSPARENT);
        holder.tDesc.setEnabled(false);
        holder.tDesc.setTag(holder);
        holder.tDesc.setOnClickListener(this);
        //holder.tDesc.setText(Html.fromHtml(text));

        return view;
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder;
        holder=(ViewHolder)view.getTag();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(items.get(holder.i).getLink().toString()));
        context.startActivity(intent);
    }

    static class ViewHolder
    {
        @Bind(R.id.newsTitle)TextView tTitle;
        @Bind(R.id.newsDesc)WebView tDesc;
        @Bind(R.id.newsDate)TextView tDate;
        int i;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this,view);
        }
    }
}

