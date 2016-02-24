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

package com.devspark.progressfragment.sample;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * @author Evgeny Shishkin
 */
public class ProgressActivity extends Activity {
    public static final String EXTRA_TITLE = "com.devspark.progressfragment.sample.extras.EXTRA_TITLE";
    public static final String EXTRA_FRAGMENT = "com.devspark.progressfragment.sample.extras.EXTRA_FRAGMENT";
    public static final int FRAGMENT_DEFAULT = 0;
    public static final int FRAGMENT_EMPTY_CONTENT = 1;
    public static final int FRAGMENT_CUSTOM_LAYOUT = 2;
    public static final int FRAGMENT_LIST = 3;
    public static final int FRAGMENT_GRID = 4;
    public static final int DIALOG_FRAGMENT = 5;
    public static final int DIALOG_EMPTY_CONTENT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new ActionBarHelper().setDisplayHomeAsUpEnabled(true);
        }
        int fragmentId = getIntent().getIntExtra(EXTRA_FRAGMENT, FRAGMENT_DEFAULT);
        if (fragmentId == DIALOG_FRAGMENT) {
            DefaultDialogProgressFragment df = DefaultDialogProgressFragment.newInstance();
            df.show(getFragmentManager(), "DefaultDialogProgressFragment");
            return;
        } else if (fragmentId == DIALOG_EMPTY_CONTENT) {
            EmptyContentDialogProgressFragment df = EmptyContentDialogProgressFragment.newInstance();
            df.show(getFragmentManager(), "EmptyContentDialogProgressFragment");
            return;
        }

        // Check what fragment is shown, replace if needed.
        Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);
        if (fragment == null) {
            // Make new fragment to show.
            switch (fragmentId) {
                case FRAGMENT_DEFAULT:
                    fragment = DefaultProgressFragment.newInstance();
                    break;
                case FRAGMENT_EMPTY_CONTENT:
                    fragment = EmptyContentProgressFragment.newInstance();
                    break;
                case FRAGMENT_CUSTOM_LAYOUT:
                    fragment = CustomLayoutProgressFragment.newInstance();
                    break;
                case FRAGMENT_LIST:
                    fragment = DefaultProgressListFragment.newInstance();
                    break;
                case FRAGMENT_GRID:
                    fragment = DefaultProgressGridFragment.newInstance();
                    break;
                default:
                    fragment = DefaultProgressFragment.newInstance();
                    break;

            }
            getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Helper for fix issue VerifyError on Android 1.6. On Android 1.6 virtual machine
     * tries to resolve (verify) getActionBar function, and since there is no such function,
     * Dalvik throws VerifyError.
     */
    private class ActionBarHelper {

        /**
         * Set whether home should be displayed as an "up" affordance.
         * Set this to true if selecting "home" returns up by a single level in your UI
         * rather than back to the top level or front page.
         *
         * @param showHomeAsUp true to show the user that selecting home will return one
         *                     level up rather than to the top level of the app.
         */
        private void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
            }
        }
    }
}
