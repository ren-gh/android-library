
package com.rengh.app.star.fragment;

import com.r.library.common.util.LogUtils;
import com.r.library.common.util.ToastUtils;
import com.r.library.common.util.UIUtils;
import com.rengh.app.star.R;
import com.rengh.app.star.activity.WebActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TabFragment extends BaseFragment {
    private final String TAG = "TabFragment";
    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_dialog_map);
        actionBar.setDisplayHomeAsUpEnabled(true);
        UIUtils.setTranslationStatusBar(activity);

        drawerLayout = view.findViewById(R.id.dl_root);
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);
        navigationView = view.findViewById(R.id.nv_slide_menu);

        initViewPager();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                ToastUtils.showToast(activity, String.valueOf(menuItem.getTitle()));
                Observable.timer(200, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                menuItem.setChecked(false);
                                drawerLayout.closeDrawers();
                            }
                        });
                Intent intent = new Intent();
                intent.setClass(activity, WebActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        LogUtils.i(TAG, "onDetach()");
        super.onDetach();
        activity = null;
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("推荐");
        titles.add("搞笑");
        titles.add("新闻");
        titles.add("动漫");
        titles.add("体育");
        titles.add("法制");
        titles.add("民生");
        titles.add("军事");
        titles.add("农业");
        titles.add("科技");

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
            fragments.add(new TabListFragment());
        }

        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager(), fragments, titles);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;

        public FragmentAdapter(FragmentManager manager, List fragments, List titles) {
            super(manager);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return titles.get(i);
        }
    }

}
