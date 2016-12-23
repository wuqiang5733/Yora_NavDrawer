package com.tuchangwei.yora.activities;

import android.os.Bundle;
import android.util.Log;

import com.tuchangwei.yora.R;
import com.tuchangwei.yora.views.MainNavDrawer;

/**
 * Created by vale on 1/4/16.
 */
public class MainActivity extends BaseAuthenticatedActivity {
    @Override
    protected void onYoraCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Inbox");
        Log.e("MainActivity","Before");
        setNavDrawer(new MainNavDrawer(this));
        Log.e("MainActivity","After");
    }
}
