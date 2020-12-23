package com.example.bgctub_transport_tracker_driver_app.ui.tips3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_driver_app.R;
import com.example.bgctub_transport_tracker_driver_app.SignupActivity;

public class Tips3Fragment extends Fragment implements View.OnClickListener {

    private Tips3ViewModel mViewModel;
    private Button startButton;

    public static Tips3Fragment newInstance() {
        return new Tips3Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tips3_fragment, container, false);
        startButton = root.findViewById(R.id.tip3StartButton);

        startButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Tips3ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {

        if (v == startButton) {
            //goto signup activity
            startActivity(new Intent(getActivity(), SignupActivity.class));
            getActivity().finish();
        }

    }


}