package com.dpagliotto.gimb.view;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dpagliotto.gimb.R;
import com.dpagliotto.gimb.view.helper.Helper;

public class DefaultFragmentContainerActivity extends BaseActivity {

    private FrameLayout mContent;

    private RelativeLayout mLayoutProgress;
    private TextView mProgressMessage;

    private BaseFragment mCurrentFragment;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_fragment_container);

        Toolbar toolbar = findViewById(R.id.fragmentContainerToolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        mContent = findViewById(R.id.defaultContainerFragment);

        mHandler = new Handler(getMainLooper());

        mLayoutProgress = findViewById(R.id.defaultLayoutProgress);
        mLayoutProgress.setVisibility(View.GONE);

        mProgressMessage = findViewById(R.id.defaulttxtMainProgressMessage);
        mProgressMessage.setText("");

        selectFragment(Helper.OPT_LOGIN_FRAGMENT);
    }

    public void selectFragment(Integer option) {
        BaseFragment fragment = null;

        if (option.equals(Helper.OPT_LOGIN_FRAGMENT)) {
            setTitle("Login");
            fragment = LoginFragment.getInstance();
        }

        if (mCurrentFragment != fragment) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (mCurrentFragment != null) {
                ft.remove(mCurrentFragment);
            }

            mCurrentFragment = fragment;

            ft.add(mContent.getId(), mCurrentFragment).commit();
        }
    }

    public void esconderStatus() {
        mostrarStatus(false, "");
    }

    public void mostrarStatus(final boolean show, final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mLayoutProgress.setVisibility(View.GONE);
                mProgressMessage.setText("");

                if (show) {
                    mLayoutProgress.setVisibility(View.VISIBLE);
                    mProgressMessage.setText(message);
                }
            }
        };

        mHandler.post(runnable);
    }

    public void showMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Helper.customAlertDialog(DefaultFragmentContainerActivity.this, message, Helper.positiveBtnOK(null));
            }
        };

        mHandler.post(runnable);
    }
}
