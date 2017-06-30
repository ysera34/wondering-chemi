package com.planet.wondering.chemi.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
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

        /*
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
//        TextView disableTextView = (TextView) menuView.getChildAt(menuIndex).findViewById(R.id.smallLabel);
//        TextView enableTextView = (TextView) menuView.getChildAt(menuIndex).findViewById(R.id.largeLabel);



        for (int i = 0; i < 4; i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            if (i == menuIndex) {
                TextView enableTextView = (TextView) itemView.findViewById(R.id.largeLabel);
                enableTextView.setVisibility(View.VISIBLE);
            } else {
//                ViewGroup.LayoutParams params = itemView.getLayoutParams();
//
//                itemView.setLayoutParams(params);
                itemView.getChildAt(1).setVisibility(View.GONE);
            }
        }
        */
    }

    public void showBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    public void hideBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(mBottomNavigationLayout.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }
}