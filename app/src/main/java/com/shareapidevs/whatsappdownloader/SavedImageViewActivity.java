package com.shareapidevs.whatsappdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import admob.Admob;
import de.mateware.snacky.Snacky;

public class SavedImageViewActivity extends AppCompatActivity {

    LinearLayout download_L,share_L,repost_L;
    TextView download_T,share_T,repost_T;
    FloatingActionButton download_F,share_F,repost_F,main_fab;
    ImageView imageView;
    URI uri;
    Uri androidUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_image_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.image_view_view);
        Intent intent = getIntent();
        String url = intent.getStringExtra("filepath");
        androidUri = Uri.parse(url);

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getString(R.string.banner));
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("335AB1B488DA376C259B9DC3E9B56BBE").build();
        mAdView.loadAd(adRequest);
        //Admob.banner(mAdView);

        LinearLayout linearLayout = findViewById(R.id.banner_container);
        linearLayout.addView(mAdView);


        try {
            uri = new URI(url);
            Glide.with(this).
                    load(new File(uri.getPath()))
                    .into(imageView);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d("errorimage",e.getMessage());
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

    //download file
    public View.OnClickListener onClickListenerDelete(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLinearLayoutsVisibility();
                try {
                    File file = new File(uri.getPath());
                    file.delete();
                    Snacky.builder().
                            setActivity(SavedImageViewActivity.this).
                            setText(R.string.file_deleted).
                            success().
                            show();
                    startActivity(new Intent(SavedImageViewActivity.this,Saved.class));
                    finish();
                } catch (Exception e) {
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
                //Intent intent = new Intent(Intent.ACTION_SEND);
                setLinearLayoutsVisibility();
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(SavedImageViewActivity.this);
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
                ShareCompat.IntentBuilder share = ShareCompat.IntentBuilder.from(SavedImageViewActivity.this);
                share.setStream(androidUri) // uri from FileProvider
                        .setType("text/html")
                        .getIntent()
                        .setAction(Intent.ACTION_SEND) //Change if needed
                        .setDataAndType(androidUri, "image/*")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                share.getIntent().setPackage("com.whatsapp");
                if (share.getIntent().resolveActivity(getPackageManager()) == null) {
                    Toast.makeText(SavedImageViewActivity.this, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
                    return;
                }
                //intent.setType(URLConnection.guessContentTypeFromName(file.getName()));
                //intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(share.getIntent());
            }
        };
    }
}
