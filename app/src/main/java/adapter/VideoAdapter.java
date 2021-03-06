package adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.shareapidevs.whatsappdownloader.R;
import com.shareapidevs.whatsappdownloader.VideoViewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import admob.Admob;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private ArrayList<File> filesList;
    private Activity activity;
    Uri videoUri = null;
    InterstitialAd interstitialAd;

    public VideoAdapter(ArrayList<File> filesList, Activity activity) {
        this.filesList = filesList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videos_list, parent, false);
        interstitialAd = Admob.InterstitialCallLoad(activity,String.valueOf(R.string.interstitial));

        return new VideoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoHolder holder, int position) {
        holder.getAdapterPosition();
        final File currentFile = filesList.get(position);
        videoUri = Uri.fromFile(currentFile);

        if (filesList.size()==0){
            holder.textView.setVisibility(View.VISIBLE);
        }


        holder.videoView.setOnClickListener(clickListener(videoUri.toString(),position));
        holder.imageView.setOnClickListener(clickListener(videoUri.toString(),position));
        //get image bitmap from video

       // MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
       // mediaMetadataRetriever.setDataSource(videoUri.getPath(),new HashMap<String, String>());

        //Bitmap bitFrame = mediaMetadataRetriever.getFrameAtTime();
        //holder.videoView.setImageBitmap(bitFrame);
        //Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoUri.getPath(), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
//        Glide.with(activity).
//                load(bitmap)
//                .centerCrop()
//                .into(holder.videoView);

        Glide.with(activity).
                load(videoUri.getPath())
                .centerCrop()
                .into(holder.videoView);

    }

    public void loadInterstitial(final Intent intent,int position){
        if (position%2==0){
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
            }
        }else {
            activity.startActivity(intent);
        }
    }

    //set clicklistener for both views
    public View.OnClickListener clickListener(final String url, final int position){
        return new  View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, VideoViewActivity.class);
                intent.putExtra("filepath",url);
                intent.putExtra("position",String.valueOf(position));
                //activity.startActivity(intent);
                loadInterstitial(intent,position);
            }
        };
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder{

        ImageView videoView;
        ImageView imageView;
        TextView textView;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            videoView = (ImageView) itemView.findViewById(R.id.video);
            imageView = (ImageView) itemView.findViewById(R.id.videoDownload);
            textView = (TextView) itemView.findViewById(R.id.emptyList);
        }
    }
}
