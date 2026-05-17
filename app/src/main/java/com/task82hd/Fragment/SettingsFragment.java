package com.task82hd.Fragment;

import static android.content.Context.ACTIVITY_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.task82hd.Database.AppDatabase;
import com.task82hd.Database.Entity.Misc;
import com.task82hd.LLMProvider.LLMProvider;
import com.task82hd.R;

public class SettingsFragment extends Fragment {

    TextView offlineModeText;
    SwitchMaterial offlineModeToggle;
    SwitchMaterial offDeviceProcessing;




    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        offlineModeText = view.findViewById(R.id.offline_mode_text);
        offlineModeToggle = view.findViewById(R.id.offline_mode_toggle);
        offDeviceProcessing = view.findViewById(R.id.off_device_processing);

        if(canRunLLMOnDevice()) {
            offlineModeText.setText(R.string.can_run_on_device);
            offlineModeText.setBackgroundResource(R.drawable.background);
        }
        else {
            offlineModeToggle.setClickable(false);
        }

        AppDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        Misc misc = db.miscDAO().getMisc();

        if(LLMProvider.PROVIDERS.values()[misc.mode] == LLMProvider.PROVIDERS.WEB) {
            offlineModeToggle.setChecked(false);
        }
        else {
            offlineModeToggle.setChecked(true);
        }

        if(misc.hasAgreedToOnline) {
            offDeviceProcessing.setChecked(true);

        }
        else {
            offDeviceProcessing.setChecked(false);
        }

        offlineModeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    db.miscDAO().setMode(LLMProvider.PROVIDERS.LOCAL.ordinal());
                }
                else {
                    db.miscDAO().setMode(LLMProvider.PROVIDERS.WEB.ordinal());
                }
            }
        });

        offDeviceProcessing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    db.miscDAO().setConsent(true);
                }
                else {
                    db.miscDAO().setConsent(false);
                }
            }
        });

    }

    public boolean canRunLLMOnDevice() {
        Context ctx = requireContext();

        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(memoryInfo);

        if(8 > memoryInfo.totalMem/(1024*1024*1024)) {
            return false;
        }

        return true;
    }
}