package kat.android.com.movielist;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import kat.android.com.movielist.common.PreferencesUtils;

//Activity which responsible for placing TabsFragments or SearchFragment
public class MovieListActivity extends ActionBarActivity implements MenuItemCompat.OnActionExpandListener {

    private PreferencesUtils utils;

    private static final int POPULAR_FRAGMENT = 0;
    private static final int UPCOMING_FRAGMENT = 1;
    private static final int TOP_RATED_FRAGMENT = 2;
    private static final int SEARCH_FRAGMENT = 3;
    private static final int DISCOVER_FRAGMENT = 4;
    private static final int FAVORITE_FRAGMENT = 5;
    private static final int WATCHLIST_FRAGMENT = 6;
    private static final int LOG_IN_OUT_FRAGMENT = 7;

    private static final int FRAGMENT_COUNT = WATCHLIST_FRAGMENT + 1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    private Drawer.Result drawerResult = null;
    private Toolbar toolbar;

    private FragmentManager fm;

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.menu_tabs);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        utils = PreferencesUtils.get(getApplicationContext());

        fm = getSupportFragmentManager();
        fragments[POPULAR_FRAGMENT] = fm.findFragmentById(R.id.fragment_popular);
        fragments[UPCOMING_FRAGMENT] = fm.findFragmentById(R.id.fragment_upcoming);
        fragments[TOP_RATED_FRAGMENT] = fm.findFragmentById(R.id.fragment_toprated);
        fragments[SEARCH_FRAGMENT] = fm.findFragmentById(R.id.fragment_search);
        fragments[DISCOVER_FRAGMENT] = fm.findFragmentById(R.id.fragment_discover);
        fragments[FAVORITE_FRAGMENT] = fm.findFragmentById(R.id.fragment_favorite);
        fragments[WATCHLIST_FRAGMENT] = fm.findFragmentById(R.id.fragment_watchlist);
        //init login fragment

        //hide all fragments
        hideFragment();

        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_popular).withIcon(FontAwesome.Icon.faw_film).withIdentifier(0),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_upcoming).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_top_rated).withIcon(FontAwesome.Icon.faw_star).withIdentifier(2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_discover).withIcon(FontAwesome.Icon.faw_search).withIdentifier(4),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_favorites).withIcon(FontAwesome.Icon.faw_heart),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_watch_list).withIcon(FontAwesome.Icon.faw_list_alt),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_login).withIcon(FontAwesome.Icon.faw_sign_in)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long index, IDrawerItem iDrawerItem) {
                        switch (position - 1) {
                            case POPULAR_FRAGMENT:
                                showFragment(POPULAR_FRAGMENT);
                                break;
                            case UPCOMING_FRAGMENT:
                                showFragment(UPCOMING_FRAGMENT);
                                break;
                            case TOP_RATED_FRAGMENT:
                                showFragment(TOP_RATED_FRAGMENT);
                                break;
                            case DISCOVER_FRAGMENT:
                                showFragment(DISCOVER_FRAGMENT);
                                break;
                            case FAVORITE_FRAGMENT + 1:
                                showFragment(FAVORITE_FRAGMENT);
                                break;
                            case WATCHLIST_FRAGMENT + 1:
                                showFragment(WATCHLIST_FRAGMENT);
                                break;
                            //log in out button
                            case LOG_IN_OUT_FRAGMENT + 2:
                                if (utils.isGuest()) {
                                    //login
                                    utils.logoutGuestSessionUser();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                } else {
                                    //logout
                                    utils.logoutSessionUser();
                                    startActivity(new Intent(getApplicationContext(), MovieListActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }
                                break;
                        }
                    }
                }).withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        if (utils.isGuest()) {
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_favorites).withIcon(FontAwesome.Icon.faw_heart).setEnabled(false), 7);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_watch_list).withIcon(FontAwesome.Icon.faw_list_alt).setEnabled(false), 8);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_login).withIcon(FontAwesome.Icon.faw_sign_in), 10);
                        } else {
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_favorites).withIcon(FontAwesome.Icon.faw_heart).setEnabled(true), 7);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_watch_list).withIcon(FontAwesome.Icon.faw_list_alt).setEnabled(true), 8);
                            drawerResult.updateItem(new SecondaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_sign_out), 10);
                        }
                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }
                }).withSelectedItem(POPULAR_FRAGMENT).build();


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.search) {
                    //if search menu item is clicked , show SearchFragment
                    showFragment(SEARCH_FRAGMENT);
                }
                return true;
            }
        });
    }

    private void hideFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 1; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
        transaction.commit();
    }

    private void showFragment(int fragmentIndex) {
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else transaction.hide(fragments[i]);
        }
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabs, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackgroundResource(R.drawable.appcompat_textfield_activated_holo_dark);

        MenuItem menuItem = menu.findItem(R.id.search);
        if (menuItem != null) {
            MenuItemCompat.setOnActionExpandListener(menuItem, this);
            MenuItemCompat.setActionView(menuItem, searchView);
        }
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == R.id.search) {
//            //if search menu item is clicked , show SearchFragment
//            showFragment(SEARCH_FRAGMENT);
//            return true;
//
//        } else if (item.getItemId() == R.id.logInOut) {
//            //Log In/Out logic
//            if (utils.isGuest()) {
//                utils.setGuest(false);
//                utils.logoutGuestSessionUser();
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            } else {
//                utils.setGuest(true);
//                utils.logoutSessionUser();
//                startActivity(new Intent(getApplicationContext(), TabsActivity.class));
//            }
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    //When search menu item closed , TabsFragment replace SearchFragment
    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        showFragment(TOP_RATED_FRAGMENT);
        return true;
    }
}
