package com.konrad.mail.fragments;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.SignInButton;
import com.konrad.mail.R;
import com.konrad.mail.main.MainActivity;
import com.konrad.mail.utils.PermissionCodes;
import com.konrad.mail.utils.RequestCodes;
import com.konrad.mail.utils.Utils;
import com.konrad.mail.view.ILoginView;
import com.konrad.mail.viewmodels.LoginViewModel;


public class SignInFragment extends Fragment implements ILoginView {

    private static final int AUTH_CODE_REQUEST_CODE = 0x0123;

    private SignInButton signIn;
    private Button signOut;
    private Button signInDiffAccount;
    private Button continueButton;
    private TextView nameTV;
    private TextView emailTV;
    private LoginViewModel viewModel;

    public SignInFragment() {
        viewModel = new LoginViewModel(this);
    }

    @Nullable
    @Override
    public View onCreateView(@android.support.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(@android.support.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        updateFragmentUi(viewModel.isLoggedIn());
    }

    private void initViews() {
        nameTV = getView().findViewById(R.id.nameTV);
        emailTV = getView().findViewById(R.id.emailTV);
        signIn = getView().findViewById(R.id.signIn);
        signOut = getView().findViewById(R.id.signOut);
        signInDiffAccount = getView().findViewById(R.id.signInDifferentAccount);
        continueButton = getView().findViewById(R.id.continueButton);

        signIn.setOnClickListener(x -> signIn());

        signOut.setOnClickListener(x -> signOut());

        signInDiffAccount.setOnClickListener(x -> {
            signOut();
            signIn();
        });

        continueButton.setOnClickListener(x -> {
            if (viewModel.isLoggedIn()) {
                goToInbox();
            }
        });
    }

    public void goToInbox() {
        ((MainActivity)getActivity()).showInbox();
    }

    private void updateFragmentUi(Boolean loggedIn) {

        if (loggedIn) {
            signIn.setVisibility(View.INVISIBLE);
            signOut.setVisibility(View.VISIBLE);
            signInDiffAccount.setVisibility(View.VISIBLE);
            continueButton.setVisibility(View.VISIBLE);
            emailTV.setText(viewModel.getSelectedAccountName());
        } else {
            signIn.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.INVISIBLE);
            signInDiffAccount.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
            emailTV.setText(getString(R.string.sign_in_logged_out_message));
        }

    }

    private void signIn() {
        if (Utils.checkPermission(getActivity().getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {
            Intent intent = viewModel.newChooseAccountIntent();
            startActivityForResult(intent, RequestCodes.LOGIN);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.GET_ACCOUNTS}, PermissionCodes.ACCOUNTS);
        }
    }

    private void signOut() {
        viewModel.signOut();
        updateFragmentUi(viewModel.isLoggedIn());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int successfulLogin = -1;

        if (requestCode == RequestCodes.LOGIN && resultCode == successfulLogin) {
            if (data.getExtras() != null && data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME) != null) {
                viewModel.signIn(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
            }
        }
    }

    @Override
    public void startPermissionActivity(UserRecoverableAuthException e) {
        getActivity().startActivityForResult(e.getIntent(), AUTH_CODE_REQUEST_CODE);
    }
}
