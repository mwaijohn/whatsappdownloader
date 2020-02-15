package adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.shareapidevs.whatsappdownloader.FullScreenViewActivity;
import com.shareapidevs.whatsappdownloader.R;

import java.io.File;
import java.util.ArrayList;

import admob.Admob;
import static com.shareapidevs.whatsappdownloader.StartingActivity.interstitialAd;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.FileHolder> {

    private ArrayList<File> filesList;
    private Activity activity;
    Uri imageUri;
    //InterstitialAd interstitialAd;
    public ImageAdapter(ArrayList<File> filesList, Activity activity) {
        this.filesList = filesList;
        this.activity = activity;
       // this.interstitialAd = Admob.InterstitialCallLoad(activity,String.valueOf(R.string.interstitial));
    }

    @Override
    public ImageAdapter.FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pictures_lists, parent, false);
        return new FileHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.FileHolder holder, int position) {
        holder.getAdapterPosition();
        File currentFile = filesList.get(position);
        imageUri = Uri.fromFile(currentFile);

        holder.imageViewImageMedia.setOnClickListener(clickListener(imageUri.toString(),position));
        holder.buttonImageDownload.setOnClickListener(clickListener(imageUri.toString(),position));

        Glide.with(activity).
                load(imageUri)
                .centerCrop()
                .into(holder.imageViewImageMedia);
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    //set clicklistener for both views
    public View.OnClickListener clickListener(final String url, final int position){
        return new  View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(activity, FullScreenViewActivity.class);
                intent.putExtra("filepath",url);
                intent.putExtra("position",position);

//                Intent i = new Intent(activity, FullScreenViewActivity.class);
//                i.putExtra("position", position);
//
                loadInterstitial(intent,position);

            }
        };
    }

    public void loadInterstitial(final Intent intent,int position){
        if (position%1==0){
            if (interstitialAd.isLoaded()){
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        activity.startActivity(intent);
                    }
                });
            }else {
                activity.startActivity(intent);
                Log.d("I/Ads","Add not loaded");
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }else {
            activity.startActivity(intent);
            //interstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    public static class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewImageMedia;
        ImageView buttonImageDownload;

        public FileHolder(View itemView) {
            super(itemView);
            imageViewImageMedia = (ImageView) itemView.findViewById(R.id.imageViewImageMedia);
            buttonImageDownload = (ImageView) itemView.findViewById(R.id.buttonImageDownload);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //interstitialAd.show();
        }
    }
}
