package com.konrad.mail.main;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.konrad.mail.R;
import com.konrad.mail.api.GoogleServicesManager;
import com.konrad.mail.fragments.AddLabelFragment;
import com.konrad.mail.fragments.ApplyLabelsFragment;
import com.konrad.mail.fragments.FilterByLabelFragment;
import com.konrad.mail.fragments.InboxFragment;
import com.konrad.mail.fragments.SignInFragment;
import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.PermissionCodes;
import com.konrad.mail.view.IMainView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView {

    private AddLabelFragment addLabelFragment;
    private ApplyLabelsFragment changeLabelsFragment;
    private InboxFragment inboxFragment;
    private FilterByLabelFragment filterByLabelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionCodes.STORAGE_WRITE);

        GoogleServicesManager.getInstance().init(this);
        showSignIn();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionCodes.STORAGE_WRITE:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void showSignIn() {
        SignInFragment frag = new SignInFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .commit();
    }

    @Override
    public void showInbox() {
        if (inboxFragment == null) {
            inboxFragment = new InboxFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, inboxFragment)
                .addToBackStack("InboxFragment")
                .commit();
    }

    @Override
    public void showAddLabel() {
        if (addLabelFragment == null) {
            addLabelFragment = new AddLabelFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, addLabelFragment)
                .addToBackStack("AddLabelFragment")
                .commit();
    }

    @Override
    public void hideAddLabel() {
        if (addLabelFragment != null && addLabelFragment.isVisible()) {
            getSupportFragmentManager().popBackStackImmediate();
            addLabelFragment = null;
        }
    }

    @Override
    public void showChangeLabel(List<Message> selectedMessages, List<Label> labels) {
        changeLabelsFragment = new ApplyLabelsFragment();
        changeLabelsFragment.setSelectedMessages(selectedMessages);
        changeLabelsFragment.setLabels(labels);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, changeLabelsFragment)
                .addToBackStack("ChangeLabelsFragment")
                .commit();
    }

    @Override
    public void hideChangeLabel() {
        if (changeLabelsFragment != null && changeLabelsFragment.isVisible()) {
            getSupportFragmentManager().popBackStackImmediate();
            changeLabelsFragment = null;
        }
    }

    @Override
    public void showFilterByLabel(List<Label> labels, List<String> filteredLabels) {
        filterByLabelFragment = new FilterByLabelFragment();
        filterByLabelFragment.setLabels(labels);
        filterByLabelFragment.setFilteredLabelIds(filteredLabels);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, filterByLabelFragment)
                .addToBackStack("FilterByLabelsFragment")
                .commit();
    }

    @Override
    public void hideFilterByLabel() {
        if (filterByLabelFragment != null && filterByLabelFragment.isVisible()) {
            inboxFragment.setFilteredLabelIds(filterByLabelFragment.getSelectedLabels());
            getSupportFragmentManager().popBackStackImmediate();
            filterByLabelFragment = null;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

}