package com.devspark.progressfragment.sample;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.progressfragment.ProgressDialogFragment;

public class EmptyContentDialogProgressFragment extends ProgressDialogFragment {
    private View mHeaderView;
    private View mContentView;
    private Handler mHandler;
    private Runnable mShowContentRunnable = new Runnable() {

        @Override
        public void run() {
            setContentEmpty(true);
            setContentShown(true);
        }

    };

    public static EmptyContentDialogProgressFragment newInstance() {
        EmptyContentDialogProgressFragment fragment = new EmptyContentDialogProgressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHeaderView = inflater.inflate(R.layout.view_header, container, false);
        mContentView = inflater.inflate(R.layout.view_content, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Setup header view
        setHeaderView(mHeaderView);
        // Setup content view
        setContentView(mContentView);
        // Setup text for empty content
        setEmptyText(R.string.empty);
        obtainData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mShowContentRunnable);
    }

    private void obtainData() {
        // Show indeterminate progress
        setContentShown(false);

        mHandler = new Handler();
        mHandler.postDelayed(mShowContentRunnable, 3000);
    }
}
