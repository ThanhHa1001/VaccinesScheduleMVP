package com.pimo.thea.vaccinesschedulemvp.home;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.listchild.ListChildFragment;
import com.pimo.thea.vaccinesschedulemvp.listmoreinformation.ListMoreInformationFragment;
import com.pimo.thea.vaccinesschedulemvp.utils.ActivityUtils;
import com.pimo.thea.vaccinesschedulemvp.view.DialogLoading;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private static final String LOG_TAG = "HomeActivity";

    private DrawerLayout drawerLayout;

    private ActionBar actionBar;

    private HomeContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_tool_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // set up the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.home_nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        presenter = new HomePresenter(this);

        showAllChild();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        presenter.openViewListChild();
                        break;
                    case R.id.nav_vaccine:
                        presenter.openViewListVaccines();
                        break;
                    case R.id.nav_disease:
                        presenter.openViewListDiseases();
                        break;
                    case R.id.nav_childcare:
                        presenter.openViewListChildcare();
                        break;
                    case R.id.nav_information:
                        presenter.openViewAppInfo();
                    default:
                        break;
                }
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void showAllChild() {
        presenter.openViewListChild();
    }

    @Override
    public void showListChild() {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                ListChildFragment.newInstance(),
                R.id.home_frame_layout,
                ListChildFragment.TAG);
    }

    @Override
    public void showTitleToolbarHome() {
        actionBar.setTitle(getString(R.string.home_title_home));
    }

    @Override
    public void showTitleToolbarVaccines() {
        actionBar.setTitle(getString(R.string.home_title_vaccine));
    }

    @Override
    public void showListVaccines(Bundle bundle) {
        showListVDC(bundle);
    }

    @Override
    public void showTitleToolbarDisease() {
        actionBar.setTitle(getString(R.string.home_title_disease));
    }

    @Override
    public void showListDiseases(Bundle bundle) {
        showListVDC(bundle);
    }

    @Override
    public void showTitleToolbarChildcare() {
        actionBar.setTitle(getString(R.string.home_title_childcare));
    }

    @Override
    public void showListChildcare(Bundle bundle) {
        showListVDC(bundle);
    }

    private void showListVDC(Bundle bundle) {
        ListMoreInformationFragment listMoreInformationFragment = ListMoreInformationFragment.newInstance();
        listMoreInformationFragment.setArguments(bundle);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                listMoreInformationFragment,
                R.id.home_frame_layout,
                ListMoreInformationFragment.TAG);
    }

    @Override
    public void showAppInfo() {
        Log.d(LOG_TAG, "show list app info () method");
    }
}
