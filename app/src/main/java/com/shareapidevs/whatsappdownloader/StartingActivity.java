package com.shareapidevs.whatsappdownloader;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


import com.kobakei.ratethisapp.RateThisApp;

import admob.Admob;

public class StartingActivity extends BaseActivity {
    CardView _1,_2;
    Button massage_btn;
    EditText phone;

    public  static InterstitialAd interstitialAd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        MobileAds.initialize(this,getString(R.string.app_id));
        _1 = (CardView) findViewById(R.id.saved);
        _2 = (CardView) findViewById(R.id.recent);
        massage_btn = findViewById(R.id.massage_button);
        phone = findViewById(R.id.phone);

//        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("b195f8dd8ded45fe847ad89ed1d016da")
//                .withLogLevel(MoPubLog.LogLevel.DEBUG)
//                .withLegitimateInterestAllowed(false)
//                .build();
//
//        MoPub.initializeSdk(this, sdkConfiguration, new SdkInitializationListener() {
//            @Override
//            public void onInitializationFinished() {
//                Log.d("Mopub", "SDK initialized");
//            }
//        });

        _1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Saved.class));
                //finish();
                //interstitialAd.show();
            }
        });

        _2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                //finish();
                //interstitialAd.show();
            }
        });

        RateThisApp.Config config = new RateThisApp.Config(2,3);
        RateThisApp.init(config);
        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);

        interstitialAd = Admob.InterstitialCallLoad(this,getString(R.string.interstitial));
        massage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                            phone.getText().toString().trim(),""))));
                        }
                    });
                }else{
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                    phone.getText().toString().trim(),""))));
                }
            }
        });

//        AdView mAdView = new AdView(this);
//        mAdView.setAdSize(AdSize.SMART_BANNER);
//        mAdView.setAdUnitId(getString(R.string.banner));
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("335AB1B488DA376C259B9DC3E9B56BBE").build();
//        mAdView.loadAd(adRequest);
//        Admob.banner(mAdView);
//
//        LinearLayout linearLayout = findViewById(R.id.banner_container);
//        linearLayout.addView(mAdView);
//        MoPubView moPubView = new MoPubView(getApplicationContext());
//        moPubView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da"); // Enter your Ad Unit ID from www.mopub.com
//        moPubView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50); // Call this if you are not setting the ad size in XML or wish to use an ad size other than what has been set in the XML. Note that multiple calls to `setAdSize()` will override one another, and the MoPub SDK only considers the most recent one.
//        moPubView.loadAd(MoPubView.MoPubAdSize.HEIGHT_50); // Call this if you are not calling setAdSize() or setting the size in XML, or if you are using the ad size that has not already been set through either setAdSize() or in the XML
//        moPubView.loadAd();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_rate_us:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details/?id=" + getPackageName())));
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(" https://play.google.com?id=" + getPackageName())));
                }
                return true;
            case R.id.action_policy:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy))));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (showAdd==true){
//            Admob.InterstitialCall(this,getString(R.string.interstitial));
//        }
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        showAdd=false;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        interstitialAd = Admob.InterstitialCallLoad(this,getString(R.string.interstitial));
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(new AdRequest.Builder().build());

            }
        });
    }

//    @Override
//    public void onStateUpdate(InstallState state) {
//        if (state.installStatus() == InstallStatus.DOWNLOADED) {
//            // After the update is downloaded, show a notification
//            // and request user confirmation to restart the app.
//            popupSnackbarForCompleteUpdate(Update.checkUpdate(getApplicationContext(),getParent()));
//        }
//    }

//    /* Displays the snackbar notification and call to action. */
//    private void popupSnackbarForCompleteUpdate(AppUpdateManager appUpdateManager) {
//        Snackbar snackbar =
//                Snackbar.make(
//                        findViewById(R.id.starting_layout),
//                        "An update has just been downloaded.",
//                        Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
//        snackbar.setActionTextColor(
//                getResources().getColor(R.color.snack));
//        snackbar.show();
//    }
}

