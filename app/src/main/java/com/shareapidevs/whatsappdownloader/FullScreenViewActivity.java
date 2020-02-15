package com.shareapidevs.whatsappdownloader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ShareCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import adapter.FullScreenImageAdapter;
import adapter.Utils;
import admob.Admob;
import de.mateware.snacky.Snacky;

public class FullScreenViewActivity extends Activity {

    private Utils utils;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    public static final String PHOTO_ALBUM_STATUS = "WhatsApp/Media/.Statuses";
    public static final String PHOTO_ALBUM_SAVED = "WSDownloader";
    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/WSDownloader/";

    LinearLayout download_L,share_L,repost_L;
    TextView download_T,share_T,repost_T;
    FloatingActionButton download_F,share_F,repost_F,main_fab;
    ImageView imageView;
    URI uri;
    String url;
    Uri androidUri;
    ArrayList<String> file_paths = null;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);

        utils = new Utils(getApplicationContext());
        file_paths = utils.getFilePaths(PHOTO_ALBUM_STATUS);

        interstitialAd = Admob.InterstitialCallLoad(this,String.valueOf(R.string.interstitial));


        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                file_paths);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);

        final String[] url = {file_paths.get(viewPager.getCurrentItem())};

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                url[0] = file_paths.get(position);
                androidUri = Uri.parse(url[0]);
                Log.d("utyty",url[0]);

                if (position% 12 ==0){
                    if (interstitialAd.isLoaded()){
                        interstitialAd.show();
                        interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });
                    }else {
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Log.d("not loaded","ad not loaded");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        androidUri = Uri.parse(url[0]);
        //uri = Uri.parse(url[0]);

        //imageView = (ImageView) findViewById(R.id.image_view_view);
       // Intent intent = getIntent();
        //url = intent.getStringExtra("filepath");

        //androidUri = Uri.parse(url);

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getString(R.string.banner));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("335AB1B488DA376C259B9DC3E9B56BBE").build();
        mAdView.loadAd(adRequest);
        //Admob.banner(mAdView);

        LinearLayout linearLayout = findViewById(R.id.banner_container);
        linearLayout.addView(mAdView);

//        try {
//            uri = new URI(url);
//            Glide.with(this).
//                    load(new File(uri.getPath()))
//                    .into(imageView);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            Log.d("errorimage",e.getMessage());
//        }


        download_L = findViewById(R.id.downloadlayout);
        share_L = findViewById(R.id.sharelayout);
        repost_L = findViewById(R.id.repostlayout);


        download_F = findViewById(R.id.fab_download);
        download_F.setOnClickListener(onClickListenerDownload());

        share_F = findViewById(R.id.fab_share);
        share_F.setOnClickListener(onClickListenerShare());

        repost_F = findViewById(R.id.fab_repost);
        repost_F.setOnClickListener(onClickListenerRepost());

        download_T = findViewById(R.id.fab_text_download);
        download_T.setClickable(true);
        download_T.setOnClickListener(onClickListenerDownload());

        repost_T = findViewById(R.id.fab_text_repost);
        repost_T.setOnClickListener(onClickListenerRepost());

        share_T = findViewById(R.id.fab_text_share);
        share_T.setOnClickListener(onClickListenerShare());

        main_fab = findViewById(R.id.basefab);


        final int[] ii = {1};
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ii[0] %2 == 0){
                    download_L.setVisibility(View.INVISIBLE);
                    repost_L.setVisibility(View.INVISIBLE);
                    share_L.setVisibility(View.INVISIBLE);

                    //Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_open_fab);
                    //main_fab.startAnimation(animation);
                }else {
                    download_L.setVisibility(View.VISIBLE);
                    repost_L.setVisibility(View.VISIBLE);
                    share_L.setVisibility(View.VISIBLE);

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_open_fab);
                    main_fab.startAnimation(animation);
                }

                ii[0] = ii[0] + 1;
            }
        });
    }

    private void setLinearLayoutsVisibility(){
        download_L.setVisibility(View.INVISIBLE);
        share_L.setVisibility(View.INVISIBLE);
        repost_L.setVisibility(View.INVISIBLE);
    }

    //download file
    public View.OnClickListener onClickListenerDownload(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLinearLayoutsVisibility();
                try {
                    File file = new File(androidUri.getPath());
                    Download.downloadFile(file,
                            new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+DIRECTORY_TO_SAVE_MEDIA_NOW+file.getName()));
                    Snacky.builder().
                            setActivity(FullScreenViewActivity.this).
                            setText(R.string.save_successful_message).
                            success().
                            show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("errpath",e.getMessage());
                }
            }
        };

    }

    //share
    public View.OnClickListener onClickListenerShare(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(FullScreenViewActivity.this);
                share.setStream(androidUri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(androidUri, "image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setLinearLayoutsVisibility();
                startActivity(Intent.createChooser(share.getIntent(),"Share"));
            }
        };
    }

    //repost
    public View.OnClickListener onClickListenerRepost(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(FullScreenViewActivity.this);
                share.setStream(androidUri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(androidUri, "image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                share.getIntent().setPackage("com.whatsapp");
                if (share.getIntent().resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(FullScreenViewActivity.this, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                    return;
                }
                setLinearLayoutsVisibility();
                startActivity(share.getIntent());
            }
        };
    }
}