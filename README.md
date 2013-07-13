[![Stories in Ready](http://badge.waffle.io/johnkil/Android-ProgressFragment.png)](http://waffle.io/johnkil/Android-ProgressFragment)  
Android-ProgressFragment
========================

Implementation of the fragment with the ability to display indeterminate progress indicator when you are waiting for the initial data. Based on [ListFragment](http://developer.android.com/reference/android/app/ListFragment.html).


Sample
------

A sample application is available on Google Play:

<a href="http://play.google.com/store/apps/details?id=com.devspark.progressfragment.sample">
  <img alt="Get it on Google Play"
       src="http://www.android.com/images/brand/get_it_on_play_logo_small.png" />
</a>

![screenshot][1]


Compatibility
-------------

This library is compatible from API 4 (Android 1.6).


Usage
-----

To display the progress fragment you need the following code:

* Create your implementation of progress fragment

``` java
public class MyProgressFragment extends ProgressFragment {
	// your code of fragment
}
```

or if you use [ActionBarSherlock](https://github.com/JakeWharton/ActionBarSherlock)

``` java
public class MyProgressFragment extends ProgressSherlockFragment {
	// your code of fragment
}
```

* Setup content view and empty text (optional) in `onActivityCreate()` method.

``` java
@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Setup content view
    setContentView(R.layout.content);
    // Setup text for empty content
    setEmptyText(R.string.empty);
    // ...
}
```

* Display of indeterminate progress indicator

``` java
setContentShown(false);
```


* When the data is loaded to set whether the content is empty and show content

``` java
setContentEmpty(/* true if content is empty else false */);
setContentShown(true);
```


Developed By
------------
* Evgeny Shishkin - <johnkil78@gmail.com>


License
-------

    Copyright 2013 Evgeny Shishkin
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: http://i44.tinypic.com/34ffncx.png
