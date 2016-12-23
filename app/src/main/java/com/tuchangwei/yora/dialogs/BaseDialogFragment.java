package com.tuchangwei.yora.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import com.tuchangwei.yora.infrastructure.YoraApplication;

/**
 * Created by WuQiang on 12/23/16.
 */
public abstract class BaseDialogFragment extends DialogFragment{
    protected YoraApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (YoraApplication) getActivity().getApplication();
    }
}
