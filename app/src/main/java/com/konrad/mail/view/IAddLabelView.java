package com.konrad.mail.view;

import com.konrad.mail.utils.IContextProvider;

public interface IAddLabelView extends IContextProvider {
    void close();
    void showError();
}
