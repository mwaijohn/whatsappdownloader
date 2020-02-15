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
import com.shareapidevs.whatsappdownloader.FullScreenSavedActivity;
import com.shareapidevs.whatsappdownloader.R;
import com.shareapidevs.whatsappdownloader.SavedImageViewActivity;

import java.io.File;
import java.util.ArrayList;

import admob.Admob;
import static com.shareapidevs.whatsappdownloader.StartingActivity.interstitialAd;

public class SavedImageAdapter extends RecyclerView.Adapter<SavedImageAdapter.FileHolder> {
    private ArrayList<File> filesList;
    private Activity activity;
    Uri imageUri;
    //InterstitialAd interstitialAd;

    public SavedImageAdapter(ArrayList<File> filesList, Activity activity) {
        this.filesList = filesList;
        this.activity = activity;
    }

    @Override
    public SavedImageAdapter.FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pictures_lists, parent, false);
//        interstitialAd = Admob.InterstitialCallLoad(activity,String.valueOf(R.string.interstitial));

        return new FileHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final SavedImageAdapter.FileHolder holder, int position) {
        holder.getAdapterPosition();
        File currentFile = filesList.get(position);
        imageUri = Uri.fromFile(currentFile);

        holder.imageViewImageMedia.setOnClickListener(clickListener(imageUri.toString(),position));
        holder.buttonImageDownload.setOnClickListener(clickListener(imageUri.toString(),position));

        Glide.with(activity).
                load(imageUri)
                .centerCrop()
                .into(holder.imageViewImageMedia);

        holder.setIsRecyclable(false);
        holder.getAdapterPosition();
    }


    @Override
    public int getItemCount() {
        return filesList.size();
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

        }

    }

    //set clicklistener for both views
    public View.OnClickListener clickListener(final String url,final int position){
        return new  View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FullScreenSavedActivity.class);
                intent.putExtra("filepath",url);
                intent.putExtra("position",position);
                loadInterstitial(intent,position);

                Log.d("filepath",url);
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
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Log.d("I/Ads","Add not loaded");
            }
        }else {
            activity.startActivity(intent);
        }
    }
}
