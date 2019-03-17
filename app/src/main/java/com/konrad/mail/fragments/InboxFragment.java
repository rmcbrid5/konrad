package com.konrad.mail.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.konrad.mail.R;
import com.konrad.mail.adapters.InboxAdapter;
import com.konrad.mail.main.MainActivity;
import com.konrad.mail.models.Label;
import com.konrad.mail.models.Message;
import com.konrad.mail.utils.Utils;
import com.konrad.mail.view.IInboxView;
import com.konrad.mail.viewmodels.InboxViewModel;

import java.util.List;
import java.util.Locale;

public class InboxFragment extends Fragment implements IInboxView {

    private Toolbar toolbar;
    private ConstraintLayout emptyStateImageView;
    private TextView emptyStateText;
    private ConstraintLayout loadingStateImageView;
    private DrawerLayout drawer;
    private NavigationView navView;
    private RecyclerView inboxRecyclerView;
    private MenuItem selectAllButton;
    private InboxViewModel viewModel;
    private InboxAdapter adapter;

    public InboxFragment() {
        viewModel = new InboxViewModel(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        viewModel.init();
        showLoadingView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.inbox_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.message_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        selectAllButton = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.message_menu_change_labels) {
            if (!adapter.areSomeMessagesSelected()) {
                Utils.showToast(getContext(), R.string.toast_error_nothing_selected);
            } else {
                changeLabelsClicked();
            }
            return true;
        } else if (item.getItemId() == R.id.message_menu_select_all) {
            /*
             * TODO: For Question #1, this is the entry point into handling the select all functionality. You don't need to add any code here, it's just called out here for your convenience
             */
            adapter.handleSelectAll();
            return true;
        }
        return false;
    }

    private void initViews() {

        emptyStateImageView = getView().findViewById(R.id.empty_label_state_view);
        emptyStateText = getView().findViewById(R.id.empty_state_text);

        drawer = getView().findViewById(R.id.inbox_drawer);
        loadingStateImageView = getView().findViewById(R.id.inbox_loading_state_view);

        inboxRecyclerView = getView().findViewById(R.id.inbox_main_recyclerview);
        inboxRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        inboxRecyclerView.addItemDecoration(divider);
        adapter = new InboxAdapter();
        adapter.setOnMessageSelectionChangedListener(() -> {
            /*
             * TODO: For Question #1, this is the listener we're passing to the adapter to receive updates on when the selection state for messages changes - you'll want to update the selectAll checkbox here when the selection state of the messages changes in the adapter.
             *
             * So that you don't need to dig around for what the ui should look like for the checkbox in the 3 possible states, Selection states for the icon are
             * All checked: R.drawable.checkbox_checked
             * Some checked: R.drawable.checkbox_indeterminate
             * None checked: R.drawable.checkbox_unchecked
             */
            if(adapter.areAllMessagesSelected()){
                selectAllButton.setIcon(R.drawable.checkbox_checked);
            }
            else if (adapter.areSomeMessagesSelected()){
                selectAllButton.setIcon(R.drawable.checkbox_indeterminate);
            }
            else{
                selectAllButton.setIcon(R.drawable.checkbox_unchecked);
            }
        });
        inboxRecyclerView.setAdapter(adapter);

        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(@NonNull View view) {
                ((NavigationMenuView) navView.getChildAt(0)).scrollToPosition(0);
            }
        });

        toolbar = getView().findViewById(R.id.inboxToolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getContext(), R.drawable.options_menu_icon_white));
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> openDrawer());

        navView = getView().findViewById(R.id.inbox_nav_view);
        navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getGroupId() == R.id.inbox_group) {
                updateTitle(getString(R.string.inbox_drawer_menu_item_primary));
                showLoadingView();
                viewModel.init();
            } else if (menuItem.getGroupId() == R.id.filter_group) {
                ((MainActivity) getActivity()).showFilterByLabel(viewModel.getSortedUserLabelList(), viewModel.getFilteredLabelIds());
            } else if (menuItem.getGroupId() == R.id.create_new_group) {
                ((MainActivity) getActivity()).showAddLabel();
            } else {
                showLoadingView();
                viewModel.handleUserLabelNavItemOnClick(menuItem.getTitle());
            }
            closeDrawer();
            return true;
        });
    }

    public void setupMenu() {
        Menu menu = navView.getMenu();
        MenuItem containerItem = menu.findItem(R.id.labels_group_main_item);
        SubMenu sub = containerItem.getSubMenu();
        sub.clear();

        List<Label> userLabelList = viewModel.getSortedUserLabelList();

        for (int index = 0; index < userLabelList.size(); index++) {
            MenuItem newItem = sub.add(Menu.NONE, index, Menu.NONE, userLabelList.get(index).getName());
            newItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            newItem.setActionView(R.layout.drawer_menu_item_with_numbers);
            newItem.setIcon(R.drawable.label_icon_dark);

            //TODO - For Question #7 - This method gets called whenever the inbox is updated, so you can SET THE LABEL COUNT HERE
        }

        MenuItem inboxItem = menu.getItem(0);
        inboxItem.setActionView(R.layout.drawer_menu_item_with_numbers);
        TextView t = inboxItem.getActionView().findViewById(R.id.drawer_menu_title_text);
        t.setText(String.format(Locale.getDefault(), "%d", viewModel.getInboxMessageCount()));
    }

    @Override
    public void updateTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void showLoadingView() {
        loadingStateImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        loadingStateImageView.setVisibility(View.GONE);
    }


    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void setLabels(List<Label> labels) {
        adapter.setLabels(labels);
    }

    @Override
    public void showLabelFetchError() {
        Utils.showToast(getContext(), R.string.toast_error_fetch_label_fail);
    }

    @Override
    public void showInboxFetchError() {
        Utils.showToast(getContext(), R.string.toast_error_fetch_inbox_fail);
    }

    public void changeLabelsClicked() {
        ((MainActivity) getActivity()).showChangeLabel(adapter.getCurrentlySelectedMessages(), viewModel.getSortedUserLabelList());
    }

    @Override
    public void showNoLabels() {
        Utils.showToast(getContext(), R.string.toast_message_no_labels);
    }

    @Override
    public void showInboxEmpty() {
        String textStr = getString(R.string.empty_folder_prefix, viewModel.getSelectedLabelTitle());
        emptyStateText.setText(textStr);
        emptyStateImageView.setVisibility(View.VISIBLE);
        inboxRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void updateMessages(List<Message> messages) {
        adapter.updateMessages(messages);
        if (messages.isEmpty()) {
            showInboxEmpty();
        } else {
            emptyStateImageView.setVisibility(View.GONE);
            inboxRecyclerView.setVisibility(View.VISIBLE);
        }
        setupMenu();
    }

    @Override
    public void setFilteredLabelIds(List<String> labelIds) {
        viewModel.setFilteredLabelIds(labelIds);
    }

}
