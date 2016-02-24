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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Evgeny Shishkin
 */
public class MainActivity extends ListActivity {

    private String[] examples = new String[]{"Default", "Empty content", "Custom layout", "List", "Grid",
            "DialogFragment", "Empty content DialogFragment"};

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
            case 3:
                intent.putExtra(ProgressActivity.EXTRA_FRAGMENT, ProgressActivity.FRAGMENT_LIST);
                break;
            case 4:
                intent.putExtra(ProgressActivity.EXTRA_FRAGMENT, ProgressActivity.FRAGMENT_GRID);
                break;
            case 5:
                intent.putExtra(ProgressActivity.EXTRA_FRAGMENT, ProgressActivity.DIALOG_FRAGMENT);
                break;
            case 6:
                intent.putExtra(ProgressActivity.EXTRA_FRAGMENT, ProgressActivity.DIALOG_EMPTY_CONTENT);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
