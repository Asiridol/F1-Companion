package com.asiri.f1companion.UI.Activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asiri.f1companion.Models.Driver;
import com.asiri.f1companion.R;
import com.asiri.f1companion.Services.ExtendedDetailsService;
import com.asiri.f1companion.Services.Models.AllStatusesModel;
import com.asiri.f1companion.Services.Models.AllTimeStatisticsModel;
import com.asiri.f1companion.Services.Models.CurrentSeasonModel;
import com.asiri.f1companion.UI.Fragments.DriverInfo.AllSeasonsFragment;
import com.asiri.f1companion.UI.Fragments.DriverInfo.CurrentSeasonFragment;
import com.asiri.f1companion.UI.Fragments.DriverInfo.FinishingStatusesFragment;
import com.asiri.f1companion.UI.Fragments.DriversFragment;
import com.asiri.f1companion.UI.Support.Animation.DepthPageTransformer;
import com.asiri.f1companion.UI.Support.CircleImage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by asiri on 3/21/2016.
 */
public class DriverInformationActivity extends AppCompatActivity {

    @Bind(R.id.driverImage)ImageView driverImage;
    @Bind(R.id.driverName)TextView driverName;
    @Bind(R.id.driverNationality)TextView driverNationality;
    @Bind(R.id.dateOfBirth)TextView driverDOB;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.pager)ViewPager pager;
    @Bind(R.id.tab_layout)TabLayout tablayout;

    String driverId;
    Realm realm;
    Driver driver;

    // service and service returns
    final ExtendedDetailsService service=new ExtendedDetailsService();
    public AllStatusesModel statusesModel;
    public AllTimeStatisticsModel allTimeStatisticsModel;
    public CurrentSeasonModel currentSeasonModel;

    public ProgressDialog mDialog;

    DriverPagerAdapter adapter;
    ArrayList<Fragment> fragments=new ArrayList<Fragment>();

    CharSequence[] titles={"Finishing Statuses","All Seasons","Current Season"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Driver Information");
        toolbar.setTitleTextColor(Color.WHITE);
        tablayout.setTabTextColors(Color.LTGRAY, Color.WHITE);
        driverId=this.getIntent().getStringExtra("driverId");
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
        realm= Realm.getInstance(getBaseContext());
        driver=realm.where(Driver.class).equalTo("driverId",driverId).findFirst();

        driverName.setText(driver.getGivenName() + " " + driver.getFamilyName() + " - " + driver.getCode() + "(" + driver.getPermanentNumber() + ")");
        driverDOB.setText("Date of birth : " + driver.getDateOfBirth());
        driverNationality.setText("Nationality : " + driver.getNationality());

        AsyncTask getImage=new DriverImageGetter().execute(driver.getUrl());

        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Loading ...");
        mDialog.setMessage("Loading Statuses");
        mDialog.setCancelable(false);
        mDialog.show();
        service.getAllStatuses(driverId, this);
    }

    public void finishedLoadingStatuses(AllStatusesModel model)
    {
        if(model!=null) {
            this.statusesModel = model;
            mDialog.setMessage("Loading All Seasons Statistics");
            service.getAllSeasonsStatistics(driverId, this);
        }else
        {
            Snackbar.make(this.getCurrentFocus(),"Error Loading finishing statuses",Snackbar.LENGTH_LONG).show();
            this.finish();
        }
    }

    public void finishedLoadingAllSeasonsStatistics(AllTimeStatisticsModel model)
    {
        if(model!=null) {
            this.allTimeStatisticsModel = model;
            System.out.println("All sesasons : " + model.getSeasons().length);
            mDialog.setMessage("Loading Current Season Data");
            service.getCurrentSeason(driverId,this);
        }
        else
        {
            Snackbar.make(this.getCurrentFocus(),"Error loading All Seasons statistics",Snackbar.LENGTH_LONG).show();
            this.finish();
        }
    }

    public void finishedLoadingCurrentSeason(CurrentSeasonModel model)
    {
        if(model!=null) {
            this.currentSeasonModel = model;
            mDialog.dismiss();

            Fragment tab1 = FinishingStatusesFragment.newInstance();
            Fragment tab2 = AllSeasonsFragment.newInstance();
            Fragment tab3 = CurrentSeasonFragment.newInstance();

            fragments.add(tab1);
            fragments.add(tab2);
            fragments.add(tab3);

            adapter = new DriverPagerAdapter(getSupportFragmentManager(), titles, titles.length, fragments);
            pager.setAdapter(adapter);
            pager.setPageTransformer(true, new DepthPageTransformer());
            pager.clearAnimation();
            pager.setOffscreenPageLimit(3);
            tablayout.setupWithViewPager(pager);
        }else
        {
            Snackbar.make(this.getCurrentFocus(),"Error Loading current season statistis",Snackbar.LENGTH_LONG).show();
            this.finish();
        }
    }

    // This class is used to obtain drivers photo from wikipedia link
    class DriverImageGetter extends AsyncTask<String,String,Bitmap>
    {
        String urlString;

        //show spinner and start animating it
        @Override
        protected void onPreExecute()
        {
            //spinner.setVisibility(View.VISIBLE);
            //spinner.animate();
        }

        //the method to obtain the image
        @Override
        protected Bitmap doInBackground(String... ins)
        {
            urlString=(String)ins[0];
            System.out.println(urlString);
            Bitmap mPic=null;

            try {

                // Connect to wikipedia page of the driver
                Document document = Jsoup.connect(urlString).get();

                // Get the image src attribute
                Elements description = document.select("a[class=image] img[src]");
                Elements image=description.select("img");
                String imageLink= image.attr("src");

                StringTokenizer tokenizer=null;

                tokenizer =new StringTokenizer(imageLink,",");

                imageLink=tokenizer.nextToken();
                imageLink=imageLink.subSequence(0,(imageLink.length())).toString();

                imageLink="https:" + imageLink;

                InputStream in = new java.net.URL(imageLink).openStream();
                mPic= BitmapFactory.decodeStream(in);

            }
            catch(NoSuchElementException e1)
            {
                //Toast.makeText(this,"Error fetching image",Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return mPic;
        }

        //show obtained image on ImageView or notify user if there's a problem obtaining the image
        @Override
        protected void onPostExecute(Bitmap result)
        {
            if(result==null)
            {
                Bitmap bmp;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                bmp = BitmapFactory.decodeByteArray(driver.getDriverImage(), 0, driver.getDriverImage().length, options);
                //driverImage.setBackground(null);
                driverImage.setImageBitmap(CircleImage.getRoundBitmap(bmp));
            }
            else {
                //driverImage.setBackground(null);
                driverImage.setImageBitmap(CircleImage.getRoundBitmap(result));

                realm.beginTransaction();
                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                result.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
                byte[] b = byteArrayBitmapStream.toByteArray();
                driver.setDriverImage(b);
                realm.commitTransaction();
            }
        }
    }
}

class DriverPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    ArrayList<Fragment> fragments;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public DriverPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb,ArrayList<Fragment> fragments) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.fragments=fragments;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
