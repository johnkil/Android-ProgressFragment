package com.devspark.fragmnet;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Evgeny Shishkin
 * 
 */
public class SampleProgressFragment extends ProgressFragment {
    private static final String LOG_TAG = SampleProgressFragment.class.getSimpleName();
    private View mContentView;

    public static SampleProgressFragment newInstance() {
        SampleProgressFragment fragment = new SampleProgressFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_main, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onActivityCreated() called");
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Пусто");
        setContentShown(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setContentShown(true);
                setContentView(mContentView);
            }
        }, 3000);
    }

}
