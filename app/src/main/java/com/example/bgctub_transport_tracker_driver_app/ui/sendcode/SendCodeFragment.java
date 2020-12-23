package com.example.bgctub_transport_tracker_driver_app.ui.sendcode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_driver_app.HelpActivity;
import com.example.bgctub_transport_tracker_driver_app.R;
import com.example.bgctub_transport_tracker_driver_app.ui.verifycode.VerifyCodeFragment;

public class SendCodeFragment extends Fragment implements View.OnClickListener {

    private SendCodeViewModel mViewModel;
    private EditText phoneNumberEditText;
    private Button sendCodeButton;
    private TextView helpTextView;

    public static SendCodeFragment newInstance() {
        return new SendCodeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.send_code_fragment, container, false);
        phoneNumberEditText = root.findViewById(R.id.sendCodePhoneNumberEditText);
        sendCodeButton = root.findViewById(R.id.send_code_button);
        helpTextView = root.findViewById(R.id.helpRequestTextView);

        sendCodeButton.setOnClickListener(this);
        helpTextView.setOnClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SendCodeViewModel.class);
        // TODO: Use the ViewModel
    }


    // check data is valid
    private void dataValidation() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("দয়া করে শূন্য সহ আপনার ফোন নম্বর দিন");
            return;
        }
        if (phoneNumber.length() != 11) {
            phoneNumberEditText.setError("আপনার ফোন নম্বরের ফরমেটটি সঠিক নয়");
            return;
        } else {
            sendData("+88" + phoneNumber);
        }
        //with country code

    }

    // data send to verify code fragment
    private void sendData(String phoneNumber) {

        //send phone number to verify fragment
        VerifyCodeFragment verifyCodeFragment = new VerifyCodeFragment();
        Bundle args = new Bundle();
        args.putString("phoneNumber", phoneNumber);
        verifyCodeFragment.setArguments(args);

        //replace fragment with verify code fragment

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_signup, verifyCodeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(getActivity(), "দয়া করে অপেক্ষা করুন। আপনার ফোনে একটি ভেরিফিকেশান কোড পাঠানো হবে।", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {

        if (v == sendCodeButton) {
            dataValidation();
        }
        if (v == helpTextView) {
            startActivity(new Intent(getActivity(), HelpActivity.class));
        }

    }

}