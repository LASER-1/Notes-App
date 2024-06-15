package com.example.notes.fragments;


import static androidx.navigation.Navigation.findNavController;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.SharedPreferencesManager;
import com.example.notes.databinding.FragmentLoginBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    private SignInClient oneTapClient;
    AuthViewModel viewModel;
    private BeginSignInRequest signInRequest;
    private final String TAG  = "LoginResult";
    private static final int REQ_ONE_TAP = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        oneTapClient = viewModel.getOneTapClient(requireActivity());
        signInRequest = viewModel.getSignInRequest();
        binding.googleSignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findNavController(view);
        if(requireActivity().getSharedPreferences("my_pref", Context.MODE_PRIVATE).contains("email")){
            findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_homeFragment);
        }
    }
    private void googleSignIn(){
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0,new Bundle());
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener( requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                        Toast.makeText(requireActivity(), "Sorry Unable To Login", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        Log.d(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = viewModel.oneTapClient.getSignInCredentialFromIntent(data);
                String username = credential.getId();
                String displayName = credential.getDisplayName();
                storeUserInfo(username, displayName);
                findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_homeFragment);
            } catch (ApiException e) {
                Log.d(TAG, e.toString());
            }
        }
    }
    private void  storeUserInfo(String email,String name){
        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(requireActivity());
        preferencesManager.putString("email",email);
        preferencesManager.putString("name",name);

    }

}