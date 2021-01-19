package com.jaysonjose.smartgreenhouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioGroup radioGroup;
    private RadioButton radioButtonManual,radioButtonAuto;
    private TextView textViewMinFan,textViewMinPump;
    private SeekBar seekBarFan,seekBarPump;
    private Button btnSave;
    private ProgressBar progressBarSetting;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String modeRadio;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_setting, null);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioButtonManual = view.findViewById(R.id.radioButton);
        radioButtonAuto = view.findViewById(R.id.radioButton2);
        textViewMinFan = view.findViewById(R.id.textViewMinFan);
        textViewMinPump = view.findViewById(R.id.textViewMinPump);
        seekBarPump = view.findViewById(R.id.seekBarPump);
        seekBarFan = view.findViewById(R.id.seekBarFan);
        progressBarSetting = view.findViewById(R.id.progressBar3);
        btnSave = view.findViewById(R.id.button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                         modeRadio = "manual";
                        break;
                    case  R.id.radioButton2:
                         modeRadio = "automatic";
                        break;

                }
            }
        });

        getDataSetting();
        getDataFanSetting();
        getDataPumpSetting();
        btnSaveSetting();
        seekBarProgress();

        return view;
    }

    private void seekBarProgress() {
        seekBarFan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewMinFan.setText(progress+" minutes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {




            }
        });

        seekBarPump.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewMinPump.setText(progress+" minutes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {




            }
        });
    }

    private void btnSaveSetting() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSetting.setVisibility(View.VISIBLE);
                int valueTimeFan = seekBarFan.getProgress();
                int valueTimePump = seekBarPump.getProgress();

                reference.child(userID).child("mode").setValue(""+modeRadio);
                reference.child(userID).child("control").child("fan").child("time").setValue(valueTimeFan);
                reference.child(userID).child("control").child("sprinkler").child("time").setValue(valueTimePump);
                Toast.makeText(getActivity(), "Save Successfully", Toast.LENGTH_LONG).show();
                progressBarSetting.setVisibility(View.GONE);



            }
        });
    }

    private void getDataPumpSetting() {
        reference.child(userID).child("control").child("sprinkler").child("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String timePump = snapshot.getValue().toString();
                    textViewMinPump.setText(timePump+" minutes");
                    seekBarPump.setProgress(Integer.parseInt(timePump));
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getDataFanSetting() {
        reference.child(userID).child("control").child("fan").child("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String timeFan = snapshot.getValue().toString();
                    textViewMinFan.setText(timeFan+" minutes");
                    seekBarFan.setProgress(Integer.parseInt(timeFan));
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataSetting() {
        reference.child(userID).child("mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String mode = snapshot.getValue().toString();
                    if(mode.equals("manual")){
                        modeRadio = "manual";
                        radioButtonManual.setChecked(true);
                        radioButtonAuto.setChecked(false);
                    }else{
                        radioButtonAuto.setChecked(true);
                        radioButtonManual.setChecked(false);
                        modeRadio = "automatic";
                    }
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}