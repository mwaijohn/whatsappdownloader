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

import java.io.File;
import java.util.ArrayList;

import adapter.ImageAdapter;


public class PicturesFragment extends Fragment {

    private static final String WHATSAPP_STATUSES_LOCATION = "/WhatsApp/Media/.Statuses";
    private RecyclerView mRecyclerViewMediaList;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_pictures, container, false);
        mRecyclerViewMediaList = (RecyclerView) root.findViewById(R.id.recyclerViewMedia);

        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerViewMediaList.setLayoutManager(gridLayoutManager);
        ArrayList<File> files = this.getListFiles(
                new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+WHATSAPP_STATUSES_LOCATION));
        ImageAdapter recyclerViewMediaAdapter = new ImageAdapter(files, getActivity());


        mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);

        TextView emptyList = root.findViewById(R.id.emptyList);
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

                if (file.getName().endsWith(".jpg") ||
                        file.getName().endsWith(".gif") ||
                        file.getName().endsWith(".jpeg")
                        || file.getName().endsWith(".png")) {
                    if (!inFiles.contains(file)) {
                        inFiles.add(file);
                    }
                }
            }
        }
        return inFiles;
    }
}
