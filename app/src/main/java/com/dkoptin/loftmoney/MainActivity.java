package com.dkoptin.loftmoney;


import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private Toolbar mToolBar;
    private FloatingActionButton addCellExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabs);
        mToolBar = findViewById(R.id.toolbar);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

            addCellExpenses = findViewById(R.id.addCellExpeneses);
            addCellExpenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int activeFragmentIndex = viewPager.getCurrentItem();
                    String tag;
                    if (activeFragmentIndex == 0) {
                        tag = "expense";
                    } else {
                        tag = "income"; 
                    }
                    startActivity(new Intent(MainActivity.this, AddItemActivity.class).putExtra("tag", tag));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 2) {
                        addCellExpenses.hide();
                    } else {
                        addCellExpenses.show();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.getTabAt(0).setText(R.string.expenses);
        mTabLayout.getTabAt(1).setText(R.string.income);
        mTabLayout.getTabAt(2).setText(R.string.balance);
    }

    @Override
    public void onActionModeStarted(final ActionMode mode) {
        super.onActionModeStarted(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
        addCellExpenses.hide();
    }

    @Override
    public void onActionModeFinished(final ActionMode mode) {
        super.onActionModeFinished(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        addCellExpenses.show();
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BudgetFragment();
                case 1:
                    return new IncomeFragment();
                case 2:
                    return new DiagramFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}


