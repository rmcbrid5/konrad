package com.konrad.mail.view;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.konrad.mail.utils.IContextProvider;

public interface ILoginView extends IContextProvider {
    void startPermissionActivity(UserRecoverableAuthException e);
    void goToInbox();
}
