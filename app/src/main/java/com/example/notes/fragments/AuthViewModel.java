package com.example.notes.fragments;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;

public class AuthViewModel extends ViewModel {
    public SignInClient oneTapClient;

    public SignInClient getOneTapClient(Context context){
        if(oneTapClient==null){
            oneTapClient = Identity.getSignInClient(context);
        }
        return oneTapClient;
    }
    public BeginSignInRequest getSignInRequest(){
        return BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("454649956165-5ne6qmcktcjesi077160hfthkn810vsg.apps.googleusercontent.com") // TODO
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();
    }
}
