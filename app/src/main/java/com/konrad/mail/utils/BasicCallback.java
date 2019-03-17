package com.konrad.mail.utils;

import android.support.annotation.NonNull;

/**
 * Helper class for simple callbacks with success/failure methods.
 * @param <T> the type of the object being returned via the callback for success
 */
public interface BasicCallback<T> {
    void onSuccess(@NonNull T response);
    void onFailure(Throwable t);
}
