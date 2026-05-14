package com.task82hd.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task82hd.R;


public class ViewHistoryFragment extends Fragment {



    public ViewHistoryFragment() {
        // Required empty public constructor
    }


    public static ViewHistoryFragment newInstance(String param1, String param2) {
        ViewHistoryFragment fragment = new ViewHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_history, container, false);
    }
}