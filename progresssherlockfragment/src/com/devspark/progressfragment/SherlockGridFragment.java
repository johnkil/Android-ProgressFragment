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

package com.devspark.progressfragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * The implementation of the fragment to display grid view. Based on {@link android.support.v4.app.ListFragment}.
 * If you are waiting for the initial data, you'll can displaying during this time an indeterminate progress indicator.
 *
 * @author e.shishkin.
 */
public class SherlockGridFragment extends SherlockFragment {

    final private Handler mHandler = new Handler();
    final private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mGridView.focusableViewAvailable(mGridView);
        }
    };
    final private AdapterView.OnItemClickListener mOnClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onGridItemClick((GridView) parent, v, position, id);
        }
    };
    private ListAdapter mAdapter;
    private GridView mGridView;
    private View mEmptyView;
    private TextView mStandardEmptyView;
    private View mProgressContainer;
    private View mGridContainer;
    private CharSequence mEmptyText;
    private boolean mGridShown;

    /**
     * Provide default implementation to return a simple grid view.  Subclasses
     * can override to replace with their own layout.  If doing so, the
     * returned view hierarchy <em>must</em> have a GridView whose id
     * is {@link com.devspark.progressfragment.R.id#grid R.id.grid} and can optionally
     * have a sibling view id {@link android.R.id#empty android.R.id.empty}
     * that is to be shown when the list is empty.
     * <p/>
     * <p>If you are overriding this method with your own custom content,
     * consider including the standard layout {@link com.devspark.progressfragment.R.layout#fragment_grid}
     * in your layout file, so that you continue to retain all of the standard
     * behavior of SherlockGridFragment. In particular, this is currently the only
     * way to have the built-in indeterminant progress state be shown.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    /**
     * Attach to grid view once the view hierarchy has been created.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureList();
    }

    /**
     * Detach from grid view.
     */
    @Override
    public void onDestroyView() {
        mHandler.removeCallbacks(mRequestFocus);
        mGridView = null;
        mGridShown = false;
        mEmptyView = mProgressContainer = mGridContainer = null;
        mStandardEmptyView = null;
        super.onDestroyView();
    }

    /**
     * This method will be called when an item in the grid is selected.
     * Subclasses should override. Subclasses can call
     * getGridView().getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param gridView The GridView where the click happened
     * @param v        The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     */
    public void onGridItemClick(GridView gridView, View v, int position, long id) {
    }

    /**
     * Set the currently selected grid item to the specified
     * position with the adapter's data
     *
     * @param position
     */
    public void setSelection(int position) {
        ensureList();
        mGridView.setSelection(position);
    }

    /**
     * Get the position of the currently selected grid item.
     */
    public int getSelectedItemPosition() {
        ensureList();
        return mGridView.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected grid item.
     */
    public long getSelectedItemId() {
        ensureList();
        return mGridView.getSelectedItemId();
    }

    /**
     * Get the activity's grid view widget.
     */
    public GridView getGridView() {
        ensureList();
        return mGridView;
    }

    /**
     * The default content for a SherlockGridFragment has a TextView that can
     * be shown when the grid is empty. If you would like to have it
     * shown, call this method to supply the text it should use.
     */
    public void setEmptyText(CharSequence text) {
        ensureList();
        if (mStandardEmptyView == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        mStandardEmptyView.setText(text);
        if (mEmptyText == null) {
            mGridView.setEmptyView(mStandardEmptyView);
        }
        mEmptyText = text;
    }

    /**
     * Control whether the grid is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     * <p/>
     * <p>Applications do not normally need to use this themselves.  The default
     * behavior of SherlockGridFragment is to start with the grid not being shown, only
     * showing it once an adapter is given with {@link #setGridAdapter(android.widget.ListAdapter)}.
     * If the grid at that point had not been shown, when it does get shown
     * it will be do without the user ever seeing the hidden state.
     *
     * @param shown If true, the grid view is shown; if false, the progress
     *              indicator.  The initial value is true.
     */
    public void setGridShown(boolean shown) {
        setGridShown(shown, true);
    }

    /**
     * Like {@link #setGridShown(boolean)}, but no animation is used when
     * transitioning from the previous state.
     */
    public void setGridShownNoAnimation(boolean shown) {
        setGridShown(shown, false);
    }

    /**
     * Control whether the grid is being displayed.  You can make it not
     * displayed if you are waiting for the initial data to show in it.  During
     * this time an indeterminant progress indicator will be shown instead.
     *
     * @param shown   If true, the grid view is shown; if false, the progress
     *                indicator.  The initial value is true.
     * @param animate If true, an animation will be used to transition to the
     *                new state.
     */
    private void setGridShown(boolean shown, boolean animate) {
        ensureList();
        if (mProgressContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        if (mGridShown == shown) {
            return;
        }
        mGridShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                mGridContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mGridContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mGridContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                mGridContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mGridContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mGridContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Get the ListAdapter associated with this activity's GridView.
     */
    public ListAdapter getGridAdapter() {
        return mAdapter;
    }

    /**
     * Provide the cursor for the grid view.
     */
    public void setGridAdapter(ListAdapter adapter) {
        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if (mGridView != null) {
            mGridView.setAdapter(adapter);
            if (!mGridShown && !hadAdapter) {
                // The grid was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setGridShown(true, getView().getWindowToken() != null);
            }
        }
    }

    private void ensureList() {
        if (mGridView != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }
        if (root instanceof GridView) {
            mGridView = (GridView) root;
        } else {
            View emptyView = root.findViewById(android.R.id.empty);
            if (emptyView != null) {
                if (emptyView instanceof TextView) {
                    mStandardEmptyView = (TextView) emptyView;
                } else {
                    mEmptyView = emptyView;
                }
            } else {
                mStandardEmptyView.setVisibility(View.GONE);
            }
            mProgressContainer = root.findViewById(R.id.progress_container);
            mGridContainer = root.findViewById(R.id.grid_container);
            View rawGridView = root.findViewById(R.id.grid);
            if (!(rawGridView instanceof GridView)) {
                throw new RuntimeException(
                        "Content has view with id attribute 'R.id.grid' "
                                + "that is not a GridView class");
            }
            mGridView = (GridView) rawGridView;
            if (mGridView == null) {
                throw new RuntimeException(
                        "Your content must have a GridView whose id attribute is " +
                                "'R.id.grid'");
            }
            if (mEmptyView != null) {
                mGridView.setEmptyView(mEmptyView);
            } else if (mEmptyText != null) {
                mStandardEmptyView.setText(mEmptyText);
                mGridView.setEmptyView(mStandardEmptyView);
            }
        }
        mGridShown = true;
        mGridView.setOnItemClickListener(mOnClickListener);
        if (mAdapter != null) {
            ListAdapter adapter = mAdapter;
            mAdapter = null;
            setGridAdapter(adapter);
        } else {
            // We are starting without an adapter, so assume we won't
            // have our data right away and start with the progress indicator.
            if (mProgressContainer != null) {
                setGridShown(false, false);
            }
        }
        mHandler.post(mRequestFocus);
    }

}
