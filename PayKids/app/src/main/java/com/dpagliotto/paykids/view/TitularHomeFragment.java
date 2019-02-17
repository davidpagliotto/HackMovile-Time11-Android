package com.dpagliotto.paykids.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpagliotto.paykids.R;

public class TitularHomeFragment extends BaseFragment {

    private static TitularHomeFragment instance;

    public static TitularHomeFragment getInstance() {
        if (instance == null)
            instance = new TitularHomeFragment();
        return instance;
    }

    public TitularHomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_titular_home, container, false);
        return view;
    }

}
