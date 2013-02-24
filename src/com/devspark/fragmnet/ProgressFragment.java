/*
 * Copyright (C) 2013 Evgeny Shishkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devspark.fragmnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * 
 * @author Evgeny Shishkin
 * 
 */
public class ProgressFragment extends Fragment {

    private View mEmptyView;
    private TextView mStandardEmptyView;
    private View mProgressContainer;
    private View mContentContainer;
    private View mContentView;
    private CharSequence mEmptyText;
    private boolean mContentShown;

    /**
     * Provide default implementation to return a simple list view.  Subclasses
     * can override to replace with their own layout.  If doing so, the
     * returned view hierarchy <em>must</em> have a ListView whose id
     * is {@link android.R.id#list android.R.id.list} and can optionally
     * have a sibling view id {@link android.R.id#empty android.R.id.empty}
     * that is to be shown when the list is empty.
     * 
     * <p>If you are overriding this method with your own custom content,
     * consider including the standard layout {@link android.R.layout#list_content}
     * in your layout file, so that you continue to retain all of the standard
     * behavior of ListFragment.  In particular, this is currently the only
     * way to have the built-in indeterminant progress state be shown.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    /**
     * Attach to content view once the view hierarchy has been created.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureContent();
    }

    /**
     * Detach from list view.
     */
    @Override
    public void onDestroyView() {
        mContentShown = false;
        mEmptyView = mProgressContainer = mContentContainer = mContentView = null;
        mStandardEmptyView = null;
        super.onDestroyView();
    }

    /**
     * 
     * @param contentView
     */
    public void setContentView(View contentView) {
        ensureContent();
        if (mContentContainer instanceof ViewGroup) {
            ViewGroup contentContainer = (ViewGroup) mContentContainer;
            if (mContentView == null) {
                contentContainer.addView(contentView);
            } else {
                int index = contentContainer.indexOfChild(mContentView);
                // replace content view
                contentContainer.removeView(mContentView);
                contentContainer.addView(contentView, index);
            }
            mContentView = contentView;
        } else {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
    }

    /**
     * The default content for a ListFragment has a TextView that can be shown when the list is empty. If you would like to have it shown, call this method to
     * supply the text it should use.
     * 
     * @param text
     */
    public void setEmptyText(CharSequence text) {
        ensureContent();
        if (mStandardEmptyView == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        mStandardEmptyView.setText(text);
        mEmptyText = text;
    }
    
    /**
     * Control whether the content is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     * 
     * @param shown If true, the content view is shown; if false, the progress
     * indicator.  The initial value is true.
     */
    public void setContentShown(boolean shown) {
        setContentShown(shown, true);
    }
    
    /**
     * Like {@link #setContentShown(boolean)}, but no animation is used when
     * transitioning from the previous state.
     */
    public void setContentShownNoAnimation(boolean shown) {
        setContentShown(shown, false);
    }
    
    /**
     * Control whether the list is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     * 
     * @param shown If true, the list view is shown; if false, the progress
     * indicator.  The initial value is true.
     * @param animate If true, an animation will be used to transition to the
     * new state.
     */
    private void setContentShown(boolean shown, boolean animate) {
        ensureContent();
        if (mContentShown == shown) {
            return;
        }
        mContentShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mContentContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mContentContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mContentContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mContentContainer.setVisibility(View.GONE);
        }
    }

    private void ensureContent() {
        if (mContentContainer != null && mProgressContainer != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }
        mStandardEmptyView = (TextView) root.findViewById(R.id.empty_view);
        if (mStandardEmptyView == null) {
            mEmptyView = root.findViewById(android.R.id.empty);
        } else {
            // mStandardEmptyView.setVisibility(View.GONE);
            if (mEmptyText != null) {
                mStandardEmptyView.setText(mEmptyText);
            }
        }
        mProgressContainer = root.findViewById(R.id.progress_container);
        if (mProgressContainer == null) {
            throw new RuntimeException("Your content must have a ViewGroup whose id attribute is 'R.id.progress_container'");
        }
        mContentContainer = root.findViewById(R.id.content_container);
        if (mContentContainer == null) {
            throw new RuntimeException("Your content must have a ViewGroup whose id attribute is 'R.id.content_container'");
        }
        mContentShown = true;
        // We are starting without an adapter, so assume we won't
        // have our data right away and start with the progress indicator.
        if (mContentView == null) {
            setContentShown(false, false);
        }
    }

}
