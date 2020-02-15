package com.shareapidevs.whatsappdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import admob.Admob;


public class Saved extends AppCompatActivity {
    int showAdd=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        ViewPager viewPager = findViewById(R.id.view_pager);
        //viewPager.setAdapter(sectionsPagerAdapter);
        setupViewPager(viewPager);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//        AdView mAdView = findViewById(R.id.adView);
//        Admob.banner(mAdView);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getString(R.string.banner));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("335AB1B488DA376C259B9DC3E9B56BBE").build();
        mAdView.loadAd(adRequest);
        Admob.banner(mAdView);

        LinearLayout linearLayout = findViewById(R.id.banner_container);
        linearLayout.addView(mAdView);
    }

    private void setupViewPager(ViewPager viewPager) {
        Saved.ViewPagerAdapter adapter = new Saved.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SavedImagesFragment(), "Images");
        adapter.addFrag(new SavedVideoFragment(), "videos");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
