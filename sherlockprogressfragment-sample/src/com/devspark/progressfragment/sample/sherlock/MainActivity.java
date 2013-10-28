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

package com.devspark.progressfragment.sample.sherlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;

/**
 * @author Evgeny Shishkin
 */
public class MainActivity extends SherlockListActivity {

    private String[] examples = new String[]{"Default", "Empty content", "Custom layout"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples);
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ProgressActivity.class);
        intent.putExtra(ProgressActivity.EXTRA_TITLE, examples[position]);
        switch (position) {
            case 0:
                intent.putExtra(ProgressActivity.EXTRA_FRAGMENT, ProgressActivity.FRAGMENT_DEFAULT);
                break;
            case 1:
                intent.putExtra(ProgressActivity.EXTRA_FRAGMENT, ProgressActivity.FRAGMENT_EMPTY_CONTENT);
                break;
            case 2:
                intent.putExtra(ProgressActivity.EXTRA_FRAGMENT, ProgressActivity.FRAGMENT_CUSTOM_LAYOUT);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
