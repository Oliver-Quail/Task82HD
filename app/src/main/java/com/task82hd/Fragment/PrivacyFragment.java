package com.task82hd.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.task82hd.Database.AppDatabase;
import com.task82hd.R;


public class PrivacyFragment extends Fragment {

    Button cancelButton;
    Button acceptButton;
    Button closeButton;

    public PrivacyFragment() {
        // Required empty public constructor
    }

    public static PrivacyFragment newInstance(String param1, String param2) {
        PrivacyFragment fragment = new PrivacyFragment();
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
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancelButton = view.findViewById(R.id.cancel_button);
        acceptButton = view.findViewById(R.id.accept_button);
        closeButton = view.findViewById(R.id.close_button);

        AppDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.miscDAO().setConsent(false);
                Toast.makeText(requireContext(), "You rejected our privacy policy", Toast.LENGTH_SHORT).show();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.miscDAO().setConsent(true);
                Toast.makeText(requireContext(), "You accepted our privacy policy", Toast.LENGTH_SHORT).show();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        });

    }
}