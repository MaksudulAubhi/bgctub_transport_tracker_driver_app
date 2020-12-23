package com.example.bgctub_transport_tracker_driver_app.ui.tips1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_driver_app.R;
import com.example.bgctub_transport_tracker_driver_app.SignupActivity;
import com.example.bgctub_transport_tracker_driver_app.ui.tips2.Tips2Fragment;

public class Tips1Fragment extends Fragment implements View.OnClickListener {

    private Tips1ViewModel mViewModel;
    private Button cancelButton;
    private Button nextButton;

    public static Tips1Fragment newInstance() {
        return new Tips1Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tips1_fragment, container, false);
        cancelButton = root.findViewById(R.id.tip1CancelButton);
        nextButton = root.findViewById(R.id.tip1NextButton);

        cancelButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Tips1ViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {

        if (v == cancelButton) {
            //goto signup activity
            startActivity(new Intent(getActivity(), SignupActivity.class));
            getActivity().finish();
        }
        if (v == nextButton) {
            // goto tips2 fragment
            Tips2Fragment tips2Fragment = new Tips2Fragment();
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, tips2Fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

}