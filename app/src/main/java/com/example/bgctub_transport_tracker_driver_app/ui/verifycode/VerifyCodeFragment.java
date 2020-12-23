package com.example.bgctub_transport_tracker_driver_app.ui.verifycode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_driver_app.AppMainActivity;
import com.example.bgctub_transport_tracker_driver_app.MainActivity;
import com.example.bgctub_transport_tracker_driver_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyCodeFragment extends Fragment implements View.OnClickListener {

    private VerifyCodeViewModel mViewModel;
    private EditText verifyCodeEditText;
    private Button verifyCodeButton;
    private TextView resendCodeTextView;
    private String mVerifiedId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private String phoneNumber;
    private Handler handler;


    public static VerifyCodeFragment newInstance() {
        return new VerifyCodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.verify_code_fragment, container, false);
        verifyCodeEditText = root.findViewById(R.id.verifyCodeEditText);
        verifyCodeButton = root.findViewById(R.id.verifyCodeButton);
        resendCodeTextView = root.findViewById(R.id.resendCodetextView);
        mAuth = FirebaseAuth.getInstance();
        handler = new Handler();

        verifyCodeButton.setOnClickListener(this);
        resendCodeTextView.setOnClickListener(this);

        //get phone number from sendCodeFragment**
        phoneNumber = getArguments().getString("phoneNumber");
        //send sms to phone **
        sendVerificationCode(phoneNumber);

        //make visible resend code option**
        visibleResendCodeOption();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VerifyCodeViewModel.class);
        // TODO: Use the ViewModel
    }


    //send verification code to phone**
    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallbacks
        );
    }

    //resend verification code to phone**
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken resendToken) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallbacks,
                resendToken
        );

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCodeEditText.setText(code);
                verifyPhoneNumberWithcode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerifiedId = s;
            mResendToken = forceResendingToken;
        }
    };

    private void verifyPhoneNumberWithcode(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerifiedId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    //Signin with phone authentication**

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getActivity(), AppMainActivity.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(getActivity(), "দুঃখিত আপনার কোডটি সঠিক নয়।", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    //resend code option visible after 60 seconds........
    private void visibleResendCodeOption() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resendCodeTextView.setVisibility(View.VISIBLE);
            }
        }, 58000); //show after 58 seconds
    }


    @Override
    public void onClick(View v) {

        if (v == verifyCodeButton) {
            try {
                //manual check if OTP not work
                String code = verifyCodeEditText.getText().toString().trim();

                if (TextUtils.isEmpty(code)) {
                    verifyCodeEditText.setError("দয়া করে ভেরিফিকেশান কোড দিন");
                    return;
                }
                verifyPhoneNumberWithcode(code);
            }catch (Exception exception){
                Toast.makeText(getActivity(), "দুঃখিত, দয়া করে পরে আবার চেষ্টা করুন। ", Toast.LENGTH_LONG).show();
            }
        }
        if (v == resendCodeTextView) {
            resendVerificationCode(phoneNumber, mResendToken);
            Toast.makeText(getActivity(), "দয়া করে অপেক্ষা করুন আবার চেষ্টা করা হচ্ছে।", Toast.LENGTH_LONG).show();
        }

    }
}