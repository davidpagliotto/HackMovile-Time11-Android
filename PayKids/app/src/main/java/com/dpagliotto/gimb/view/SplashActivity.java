package com.dpagliotto.gimb.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;

import com.dpagliotto.gimb.R;

public class SplashActivity extends BaseActivity {

    private Intent mMainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        mMainIntent = new Intent(SplashActivity.this, MainActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler(getMainLooper()).
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(mMainIntent);
                        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                        finish();
                    }
                }, 1000);
    }
}
