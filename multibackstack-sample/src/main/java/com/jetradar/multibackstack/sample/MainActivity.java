/*
 * Copyright (C) 2016 JetRadar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jetradar.multibackstack.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jetradar.multibackstack.BackStackActivity;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener;

public class MainActivity extends BackStackActivity implements OnTabSelectedListener {
  private static final String STATE_CURRENT_TAB_ID = "current_tab_id";
  private static final int MAIN_TAB_ID = 0;

  private BottomNavigationBar bottomNavBar;
  private Fragment curFragment;
  private int curTabId;

  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.activity_main);
    setUpBottomNavBar();

    if (state == null) {
      bottomNavBar.selectTab(MAIN_TAB_ID, false);
      showFragment(rootTabFragment(MAIN_TAB_ID));
    }
  }

  private void setUpBottomNavBar() {
    bottomNavBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation);
    bottomNavBar
        .addItem(new BottomNavigationItem(R.drawable.dock_home_whiteout,R.string.home))
        .addItem(new BottomNavigationItem(R.drawable.dock_search_whiteout, R.string.search))
        .addItem(new BottomNavigationItem(R.drawable.dock_camera_whiteout, R.string.camera))
        .addItem(new BottomNavigationItem(R.drawable.dock_news_whiteout, R.string.favorites))
        .addItem(new BottomNavigationItem(R.drawable.dock_profile_whiteout, R.string.profile))
        .initialise();
    bottomNavBar.setTabSelectedListener(this);
  }

  @NonNull
  private Fragment rootTabFragment(int tabId) {
    switch (tabId) {
      case 0:
        BlankFragment blankFragment1 = new BlankFragment();
        return blankFragment1;
      case 1:
        BlankFragment2 blankFragment2 = new BlankFragment2();
        return blankFragment2;
      case 2:
        BlankFragment3 blankFragment3 = new BlankFragment3();
        return blankFragment3;
      case 3:
        BlankFragment4 blankFragment4 = new BlankFragment4();
        return blankFragment4;
      case 4:
        BlankFragment5 blankFragment5 = new BlankFragment5();
        return blankFragment5;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    curFragment = getSupportFragmentManager().findFragmentById(R.id.content);
    curTabId = savedInstanceState.getInt(STATE_CURRENT_TAB_ID);
    bottomNavBar.selectTab(curTabId, false);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putInt(STATE_CURRENT_TAB_ID, curTabId);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onBackPressed() {
    Pair<Integer, Fragment> pair = popFragmentFromBackStack();
    if (pair != null) {
      backTo(pair.first, pair.second);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onTabSelected(int position) {
    if (curFragment != null) {
      pushFragmentToBackStack(curTabId, curFragment);
    }
    curTabId = position;
    Fragment fragment = popFragmentFromBackStack(curTabId);
    if (fragment == null) {
      fragment = rootTabFragment(curTabId);
    }
    replaceFragment(fragment);
  }

  @Override
  public void onTabReselected(int position) {
    backToRoot();
  }

  @Override
  public void onTabUnselected(int position) {}

  public void showFragment(@NonNull Fragment fragment) {
    showFragment(fragment, true);
  }

  public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
    if (curFragment != null && addToBackStack) {
      pushFragmentToBackStack(curTabId, curFragment);
    }
    replaceFragment(fragment);
  }

  private void backTo(int tabId, @NonNull Fragment fragment) {
    if (tabId != curTabId) {
      curTabId = tabId;
      bottomNavBar.selectTab(curTabId, false);
    }
    replaceFragment(fragment);
    getSupportFragmentManager().executePendingTransactions();
  }

  private void backToRoot() {
    if (isRootTabFragment(curFragment, curTabId)) {
      return;
    }
    resetBackStackToRoot(curTabId);
    Fragment rootFragment = popFragmentFromBackStack(curTabId);
    assert rootFragment != null;
    backTo(curTabId, rootFragment);
  }

  private boolean isRootTabFragment(@NonNull Fragment fragment, int tabId) {
    return fragment.getClass() == rootTabFragment(tabId).getClass();
  }

  private void replaceFragment(@NonNull Fragment fragment) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction tr = fm.beginTransaction();
    tr.replace(R.id.content, fragment);
    tr.commitAllowingStateLoss();
    curFragment = fragment;
  }
}