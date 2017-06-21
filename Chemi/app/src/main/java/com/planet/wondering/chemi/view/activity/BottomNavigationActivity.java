package com.planet.wondering.chemi.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.helper.BottomNavigationViewHelper;

public class BottomNavigationActivity extends AppBaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BottomNavigationActivity.class.getSimpleName();
    protected BottomNavigationView mBottomNavigationView;
    public LinearLayout mBottomNavigationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationLayout = (LinearLayout) findViewById(R.id.bottom_navigation_layout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(HomeActivity.newIntent(getApplicationContext()));
                break;
//            case R.id.action_category:
//                startActivity(CategoryActivity.newIntent(getApplicationContext()));
//                break;
            case R.id.action_content:
                startActivity(ContentListActivity.newIntent(getApplicationContext()));
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

    protected void setupBottomNavigation(int menuIndex) {
        mBottomNavigationView.getMenu().getItem(menuIndex).setChecked(true);
        mBottomNavigationView.getMenu().getItem(menuIndex).setEnabled(false);

        MenuItem menuItem = mBottomNavigationView.getMenu().getItem(menuIndex);
        View menuItemView = mBottomNavigationView.findViewById(menuItem.getItemId());
        Log.i(TAG, menuItem.getTitle().toString());

    }

    public void showBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    public void hideBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(mBottomNavigationLayout.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }

    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}