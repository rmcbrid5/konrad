package com.konrad.mail.viewmodels;

import android.content.Intent;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.konrad.mail.api.GoogleServicesManager;
import com.konrad.mail.view.ILoginView;

import java.util.function.Function;

public class LoginViewModel {

    private GoogleServicesManager googleServicesManager;
    private ILoginView loginView;

    public LoginViewModel(ILoginView view) {
        loginView = view;
        googleServicesManager = GoogleServicesManager.getInstance();
    }

    public Boolean isLoggedIn() {
        return googleServicesManager.isLoggedIn();
    }

    public void signIn(String accountName) {
        googleServicesManager.login(accountName, onRetrofitCreatedCallback, authFailedCallback);
    }

    public void signOut() {
        googleServicesManager.getCredential().setSelectedAccountName("");
    }

    public String getSelectedAccountName() {
        return googleServicesManager.getCredential().getSelectedAccountName();
    }

    public Intent newChooseAccountIntent() {
        return googleServicesManager.getCredential().newChooseAccountIntent();
    }

    Function<Boolean, Void> onRetrofitCreatedCallback = isLoggedIn -> {
        if (isLoggedIn) {
            loginView.goToInbox();
        }
        return null;
    };

    Function<UserRecoverableAuthException, Void> authFailedCallback = x -> {
        loginView.startPermissionActivity(x);
        return null;
    };
}
