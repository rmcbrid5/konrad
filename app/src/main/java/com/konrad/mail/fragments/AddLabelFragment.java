package com.konrad.mail.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.konrad.mail.R;
import com.konrad.mail.main.MainActivity;
import com.konrad.mail.utils.Utils;
import com.konrad.mail.view.IAddLabelView;
import com.konrad.mail.viewmodels.AddLabelViewModel;


public class AddLabelFragment extends Fragment implements IAddLabelView {

    private AddLabelViewModel viewModel;

    private EditText addLabelEditText;
    private Button submitButton;

    public AddLabelFragment() {
        viewModel = new AddLabelViewModel(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.add_label_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        addLabelEditText.requestFocus();
        showKeyboard();

        submitButton.setOnClickListener(v -> {
            viewModel.createLabel(addLabelEditText.getText().toString());
            hideKeyboard();
        });

        addLabelEditText.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        submitButton.callOnClick();
                        return true;
                    }
                    return false;
                });
    }

    private void initViews() {
        addLabelEditText = getView().findViewById(R.id.add_label_edit_text);
        addLabelEditText.setSingleLine();
        addLabelEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        submitButton = getView().findViewById(R.id.add_label_submit_button);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(addLabelEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(addLabelEditText.getWindowToken(), 0);
    }

    @Override
    public void showError() {
        Utils.showToast(getContext(), R.string.toast_error_add_label_fail);
    }

    @Override
    public void close() {
        ((MainActivity)getActivity()).hideAddLabel();
    }
}
