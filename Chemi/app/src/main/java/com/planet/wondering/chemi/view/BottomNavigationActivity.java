package com.planet.wondering.chemi.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.AppBaseActivity;
import com.planet.wondering.chemi.util.helper.BottomNavigationViewHelper;
import com.planet.wondering.chemi.view.activity.CategoryActivity;
import com.planet.wondering.chemi.view.activity.ContentActivity;
import com.planet.wondering.chemi.view.activity.DictionaryActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;
import com.planet.wondering.chemi.view.activity.SearchActivity;

public class BottomNavigationActivity extends AppBaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BottomNavigationActivity.class.getSimpleName();
    protected BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(SearchActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_category:
                startActivity(CategoryActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_content:
                startActivity(ContentActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_dictionary:
                startActivity(DictionaryActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_member:
                startActivity(MemberActivity.newIntent(getApplicationContext()));
                break;
        }

        return true;
    }

}