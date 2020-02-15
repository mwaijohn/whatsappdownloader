package com.shareapidevs.whatsappdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import admob.Admob;
import de.mateware.snacky.Snacky;

public class SavedVideoViewActivity extends AppCompatActivity {

    VideoView videoView;
    LinearLayout download_L,share_L,repost_L;
    TextView download_T,share_T,repost_T;
    FloatingActionButton download_F,share_F,repost_F,main_fab;
    InterstitialAd interstitialAd;

    URI uri;
    Uri androidUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_video_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Admob.InterstitialCall(this,getString(R.string.interstitial));

        videoView = (VideoView) findViewById(R.id.videoView);
        Intent intent = getIntent();
        String url = intent.getStringExtra("filepath");

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        interstitialAd = Admob.InterstitialCallLoad(this,getString(R.string.interstitial));


        try {
            uri = new URI(url);
            File file = new File(uri.getPath());
            Uri videoUri = Uri.fromFile(file);
            videoView.setVideoURI(videoUri);
            videoView.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        download_L = findViewById(R.id.downloadlayout);
        share_L = findViewById(R.id.sharelayout);
        repost_L = findViewById(R.id.repostlayout);


        download_F = findViewById(R.id.fab_download);
        download_F.setOnClickListener(onClickListenerDelete());

        share_F = findViewById(R.id.fab_share);
        share_F.setOnClickListener(onClickListenerShare());

        repost_F = findViewById(R.id.fab_repost);
        repost_F.setOnClickListener(onClickListenerRepost());

        download_T = findViewById(R.id.fab_text_download);
        download_T.setClickable(true);
        download_T.setOnClickListener(onClickListenerDelete());

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

    //delete file
    public View.OnClickListener onClickListenerDelete(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLinearLayoutsVisibility();
                try {
                    File file = new File(uri.getPath());
                    file.delete();
                    Snacky.builder().
                            setActivity(SavedVideoViewActivity.this).
                            setText(R.string.file_deleted).
                            success().
                            show();
                    startActivity(new Intent(SavedVideoViewActivity.this,Saved.class));
                    finish();
                } catch (Exception e) {
                    Snacky.builder().
                            setActivity(SavedVideoViewActivity.this).
                            setText(e.getMessage()).
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
                //Intent intent = new Intent(Intent.ACTION_SEND);
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(SavedVideoViewActivity.this);
                share.setStream(androidUri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(androidUri, "image/*")
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
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(SavedVideoViewActivity.this);
                share.setStream(androidUri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(androidUri, "image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                share.getIntent().setPackage("com.whatsapp");
                if (share.getIntent().resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(SavedVideoViewActivity.this, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
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
