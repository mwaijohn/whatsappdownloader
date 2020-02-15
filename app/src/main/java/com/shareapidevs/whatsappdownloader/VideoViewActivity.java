package com.shareapidevs.whatsappdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import admob.Admob;
import de.mateware.snacky.Snacky;

public class VideoViewActivity extends AppCompatActivity {

    VideoView videoView;
    Uri videoUri;

    LinearLayout download_L,share_L,repost_L;
    TextView download_T,share_T,repost_T;
    FloatingActionButton download_F,share_F,repost_F,main_fab;
    InterstitialAd interstitialAd;

    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/WSDownloader/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // Admob.InterstitialCall(this,getString(R.string.interstitial));

        videoView = (VideoView) findViewById(R.id.videoView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("filepath");

        //display ad based on item position
//        String position = intent.getStringExtra("position");
//        if (Integer.parseInt(position) %2 != 0){
//            Admob.InterstitialCall(this,getString(R.string.interstitial));
//        }

        interstitialAd = Admob.InterstitialCallLoad(this,getString(R.string.interstitial));
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);


        try {
            URI uri = new URI(url);
            File file = new File(uri.getPath());
            videoUri = Uri.fromFile(file);
            videoView.setVideoURI(videoUri);
            videoView.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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

        final int[] i = {1};
        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i[0] %2 == 0){
                    download_L.setVisibility(View.INVISIBLE);
                    repost_L.setVisibility(View.INVISIBLE);
                    share_L.setVisibility(View.INVISIBLE);
                }else {
                    download_L.setVisibility(View.VISIBLE);
                    repost_L.setVisibility(View.VISIBLE);
                    share_L.setVisibility(View.VISIBLE);

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_open_fab);
                    main_fab.startAnimation(animation);
                }

                i[0] = i[0] + 1;
            }
        });

    }

    public void setLinearLayoutsVisibility(){
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
                File file = new File(videoUri.getPath());
                try {
                    Download.downloadFile(file,
                            new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+DIRECTORY_TO_SAVE_MEDIA_NOW+file.getName()));
                    Snacky.builder().
                            setActivity(VideoViewActivity.this).
                            setText(R.string.save_successful_message).
                            success().
                            show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Snacky.builder().
                            setActivity(VideoViewActivity.this).
                            setText(R.string.save_error_message).
                            success().
                            show();
                }
            }
        };

    }

    //share
    public View.OnClickListener onClickListenerShare(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLinearLayoutsVisibility();
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(VideoViewActivity.this);
                share.setStream(videoUri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(videoUri, "image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share.getIntent(),"Share"));
            }
        };
    }

    //repost
    public View.OnClickListener onClickListenerRepost(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLinearLayoutsVisibility();
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(VideoViewActivity.this);
                share.setStream(videoUri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(videoUri, "image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                share.getIntent().setPackage("com.whatsapp");
                if (share.getIntent().resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(VideoViewActivity.this, "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(share.getIntent());
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }
}
