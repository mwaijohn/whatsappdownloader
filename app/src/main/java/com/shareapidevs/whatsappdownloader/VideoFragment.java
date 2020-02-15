package com.shareapidevs.whatsappdownloader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;

import adapter.VideoAdapter;
import admob.Admob;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    private RecyclerView mRecyclerViewMediaList;
    private GridLayoutManager gridLayoutManager;
    private TextView emptyList;
    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video, container, false);
        mRecyclerViewMediaList = (RecyclerView) root.findViewById(R.id.recyclerViewMedia);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerViewMediaList.setLayoutManager(gridLayoutManager);
        mRecyclerViewMediaList.setNestedScrollingEnabled(false);
        //get the files
        ArrayList<File> files = this.getListFiles(
        new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+WHATSAPP_STATUSES_LOCATION));

        VideoAdapter recyclerViewMediaAdapter = new VideoAdapter(files,getActivity());
        mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);

        emptyList = root.findViewById(R.id.emptyList);
        if (files.size()==0){
            emptyList.setVisibility(View.VISIBLE);
        }



        return root;
    }
    private ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {

                if (file.getName().endsWith(".mp4")){
                    if (!inFiles.contains(file)) {
                        inFiles.add(file);
                    }
                }
            }
        }
        return inFiles;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
